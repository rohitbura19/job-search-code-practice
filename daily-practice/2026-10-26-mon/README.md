# Mon 26 Oct 2026 - practice

One focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the recurrence or invariant in one sentence each; mark "needed hints" in the score card.

## Order and time boxes (about 3 h)

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 merge sorted array + sort colors (three-way partition) (`questions/A1-merge-sorted.md`) | 15 | a1/MergeSorted.java | MergeSortedTest |
| 2 | A2 invert + symmetric tree (iterative) | 15 | a2/Trees.java | TreesTest |
| 3 | C1 word search + count paths | 25 | c1/WordSearch.java | WordSearchTest |
| 4 | C2 decode ways + list all | 25 | c2/DecodeWays.java | DecodeWaysTest |
| 5 | D1 edit distance + banded version | 25 | d1/EditDistance.java | EditDistanceTest |
| 6 | E Spring Boot: idempotency key + `@Version` + paging + ProblemDetail (`e-spring-refunds/`) | 35 | that project | RefundRepositoryTest, RefundControllerTest, RefundServiceTest, RefundPropertiesTest |
| 7 | B1 streams and collectors on payments | 20 | b1/PaymentStats.java | PaymentStatsTest |
| 8 | H1 predict the output | 20 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 Observer pattern as a typed event bus (OOP) | 30 | h2/EventBus.java | EventBusTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review) | 30 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1 (sketch 10, code 25) | 35 | my-designs/G-classes-sketch.md, then g/ | ResumableUploadServiceTest |

Core set if short on time: 3, 5, 6, 8 and F1 out loud (about 1.5 h). Code paths are under `src/main/java/practice/d20261026/`. Run one test class: `mvn test -Dtest=EditDistanceTest`. Reference solutions: `answers/` (the Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities).

## Score card

| Section | Done? | Tests green without hints? | Minutes | Weak point to revisit |
|---|---|---|---|---|
| Warm-up | | - | | |
| A1 / A2 | | | | |
| B1 | | | | |
| C1 / C2 | | | | |
| D1 | | | | |
| E | | | | |
| F1 / F2 | | - | | |
| G | | | | |
| H1 / H2 | | | | |

## Principal-engineer review: follow-ups and how to make each design more efficient

- **A1 / A2:** (a) is the merge step of merge sort run backwards; for k sorted arrays use a heap (O(N log k)) or pairwise merging. The three-way partition is the fix for quicksort on inputs with many equal keys (it never recurses into the `==` zone) and the template for "group by a small key in place". For A2, an iterative traversal with an explicit stack is the only safe option for adversarial depth; Morris traversal gets O(1) space at the price of mutating the tree temporarily (not safe with concurrent readers). Symmetry can also be checked as "tree equals its own mirror" by hashing subtrees (O(n), canonical form) when comparing many trees.
- **B1:** eight passes could be one pass with a custom collector or a loop updating several maps; keep streams for clarity and loop when profiling says so. `toMap` throws on a duplicate key without a merge function; parallel streams need associative, non-interfering collectors (never collect into a shared `ArrayList`). `topIds` sorts everything: for large input and small `n` keep a bounded min-heap (O(N log n)). Money as `long` paise avoids floating point; report `average` as a double only at the edge.
- **C1:** worst case is exponential, so prune: letter counts, searching from the rarer end, early exit on the first hit. For MANY words on the same board build a trie and walk the board once (Word Search II), removing trie branches as words are found. `countPaths` can overflow `long` on big boards of one letter: return `BigInteger` or cap. Mutating the board is not thread-safe: copy it per thread or use a `visited` bitset.
- **C2:** O(n) time, O(1) space; with `*` wildcards (Decode Ways II) the same DP gets a multiplier table and results are taken modulo 1e9+7. For streaming input the two rolling values are the whole state, so it works on a stream. `decodeAll` output is exponential: return an iterator/stream, never a list, in a real API.
- **D1:** O(n*m) time is the known lower bound under the strong exponential time hypothesis, so improve constants: bit-parallel Myers algorithm (O(n*m/64)), banding for small distances (here), and Hirschberg's trick to recover the edit script in linear space. Damerau-Levenshtein (transposition = 1) changes one transition. For a dictionary lookup within distance k use a BK-tree or a Levenshtein automaton instead of comparing against every word.
- **E:** the idempotency key turns a non-idempotent POST into a safely retryable one, but it must be stored with a fingerprint of the request (here payment + amount) and survive for the retry window; add a TTL job and index. The unique constraint is the guard, the pre-check only saves a failed insert. `@Version` costs one extra column and a failed update turns into a 409 the client resolves by re-reading; for a hot row consider a pessimistic `SELECT ... FOR UPDATE` or a single-writer queue per refund. `Page` runs a COUNT on every request: use `Slice` or keyset (cursor) pagination for deep pages (`OFFSET 100000` scans and discards 100000 rows). `findStalePending` needs an index on `(status, created_at)`.
- **F1 / G:** direct-to-storage uploads keep large bytes off your servers and make the service stateless; the cost is a more complex client and trusting storage for the part list. Per-chunk checksums catch corruption early and allow resending only that chunk. The in-memory `ResumableUploadService` holds chunk bytes for the compare in `putChunk`; with real storage compare checksums instead. A cleanup sweeper (or storage lifecycle rule) is mandatory: abandoned uploads are the main cost leak. Adaptive chunk size (start 1 MB, grow on a good connection) improves throughput on mobile.
- **F2:** a timeline is an append-only log per customer: a partition key of `customerId` with time-descending clustering makes the common read a single-partition range scan. Cursor pagination `(time, eventId)` is stable under inserts, offset is not. Hot customers need partition splitting (by day or hash suffix) and a read-time merge. Keep the raw event stream (Kafka, compacted by `eventId`) as the source of truth so timelines can be rebuilt after a bug; privacy deletes become a compaction/tombstone job.
- **H1:** the follow-up is the rule behind each result: JLS 5.1.7 / 15.21.3 (boxing cache and reference equality), 12.4 and 12.5 (class and instance initialisation order), the `hashCode`/`equals` contract (mutable keys), 15.26.2 (compound assignment casts), 10.5 (array store checks), 15.25 and 5.1.8 (conditional typing and unboxing), JEP 441 (pattern switch ordering and dominance).
- **H2:** the snapshot-per-publish makes dispatch safe against re-entrancy at the cost of a copy per subscribe (fine when subscribes are rare, bad for per-request subscriptions: use a persistent/immutable list or a linked list with tombstones). In-process Observer has no durability, ordering across processes or back-pressure: that is when you move to a broker. Weak references (or scoped subscriptions closed by their owner, `AutoCloseable`) prevent the classic listener leak. Async delivery needs an executor per subscriber (ordering per subscriber) and a decision about what a slow subscriber does (drop, block, buffer with a bound).
