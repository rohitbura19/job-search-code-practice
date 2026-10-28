# Wed 28 Oct 2026 - practice

One focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the recurrence or invariant in one sentence each; mark "needed hints" in the score card.

## Order and time boxes (about 3 h)

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 single number I, II, III (bit tricks) (`questions/A1-single-number.md`) | 15 | a1/SingleNumber.java | SingleNumberTest |
| 2 | A2 in-place compaction: move zeroes, remove element, at most twice | 15 | a2/Compact.java | CompactTest |
| 3 | C1 trie: wildcard dictionary + top-3 autocomplete | 25 | c1/WordDictionary.java, Autocomplete.java | TrieTest |
| 4 | C2 network delay (Dijkstra) + cheapest flight within k stops (Bellman-Ford) | 25 | c2/ShortestPaths.java | ShortestPathsTest |
| 5 | D1 burst balloons (interval DP) | 25 | d1/BurstBalloons.java | BurstBalloonsTest |
| 6 | E Spring Boot: one-to-many aggregate + entity graph + aggregate JPQL + nested validation + paging + ProblemDetail (`e-spring-orders/`) | 35 | that project | OrderRepositoryTest, OrderControllerTest, OrderServiceTest, OrderPropsTest |
| 7 | B1 streams and collectors on library loans | 20 | b1/LoanStats.java | LoanStatsTest |
| 8 | H1 predict the output | 20 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 Iterator pattern: peeking decorator + lazy combinators (OOP) | 30 | h2/*.java | IteratorsTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review) | 30 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1: hash-chained audit log (sketch 10, code 30) | 40 | my-designs/G-classes-sketch.md, then g/ | InMemoryAuditLogTest |

Core set if short on time: 4, 5, 6, 8 and F1 out loud (about 1.5 h). Code paths are under `src/main/java/practice/d20261028/`. Run one test class: `mvn test -Dtest=BurstBalloonsTest`. Reference solutions: `answers/` (the Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities and the paper answers).

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

- **A1 / A2:** the `ones`/`twos` pair is a mod-3 counter per bit; for "every value k times" use `ceil(log2 k)` counters, or count bits in 32 slots (simple, O(32n), the version to write first in an interview). XOR tricks are O(1) space but unreadable: say so, and prefer a `HashMap` when n is small and clarity matters. The write-pointer pattern is the same invariant in all three A2 tasks ("the prefix is already the answer"); generalising "at most k" is `x != a[w-k]`. Both are single-pass and cache-friendly; for very large arrays the cost is memory bandwidth, so avoid a second pass (moveZeroes with zero-fill writes each cell once).
- **B1:** six methods mean six passes; one custom `Collector` (or a loop) computing all aggregates in a single pass is faster when profiling says so. `summingLong` then sort the entries is O(m log m) over members, not over loans. A `takeWhile` on an unordered or parallel stream has no useful meaning: it needs an ordered source. Dates as `LocalDate` and `ChronoUnit.DAYS.between` avoid hand-rolled day arithmetic; inject `today` (a parameter or `Clock`) so the method is testable.
- **C1:** array children are fastest but cost 26 pointers per node (about 100 bytes with the header); a `HashMap` per node is slower and bigger per node but adaptive for sparse tries; production tries use compressed (radix) edges or a double-array/FST. `search` with dots is exponential in the number of dots only in a dense trie; a pattern with a leading dot touches 26 subtrees, so index words by length too if wildcards come first. For `Autocomplete`, caching the top-3 in every node makes `suggest` O(len) and `record` O(len * 3) but needs care when counts change (decrease/delete needs a recompute); at scale suggestions are computed offline (batch top-k per prefix, refreshed hourly) and served from a cache, with the live trie only for the long tail. Prefixes sharded by first characters balance poorly; shard by hash of the first 2-3 characters or by a dedicated routing table.
- **C2:** Dijkstra with a binary heap is O((V+E) log V); a lazy-deletion heap (stale entries skipped) is simpler than decrease-key and as fast in practice; for small integer weights a bucket queue (Dial) gives O(V + E + W). Bidirectional search roughly halves the explored area for a single pair; A* with an admissible heuristic (geo distance) is the road-network answer; real routing uses contraction hierarchies after preprocessing. For "at most k stops" Bellman-Ford costs O(kE) and copies the array per round; the alternative is Dijkstra over the state `(node, stops)` which is O(kE log) but can stop early at the destination. Negative weights break Dijkstra (a settled node can still improve); SPFA/Bellman-Ford handle them and detect negative cycles on round V.
- **D1:** O(n^3) is the known bound for the general problem; n=300 is why the constraint is small. The key move is choosing the LAST element of a range, which fixes the neighbours (the borders) and makes the halves independent; the same pattern solves matrix-chain multiplication, optimal BST and "remove boxes". The sentinel `1` borders remove all edge cases. Memory O(n^2) ints is 360 KB here; with a memoised recursion only reachable intervals are filled, which is not faster in the worst case but is easier to derive. Mention that the greedy "burst the smallest first" is wrong (counterexample from the brute-force test).
- **E:** the entity graph on `findByOrderNo` avoids N+1 for the one aggregate you render in full; for lists return a projection (here `OrderSummary`) or fetch lines in a second `IN` query / `@BatchSize`, never a collection fetch join with paging. Offset paging costs O(offset); for deep lists switch to keyset on `(createdAt, id)`. `existsByOrderNo` + insert is two round trips and a race: skip the check, insert, and translate the unique violation, or keep the check for the friendly message only. The aggregate query reads every line of every non-cancelled order: for a dashboard maintain a summary table updated on order events (or a materialised view) instead of recomputing; index `(status)` and `(customer_id, created_at)`. Using `Long` money in paise avoids floating-point error; add currency if multi-currency. Cap `size` server-side (done) and cap the number of lines (done) so one request cannot allocate unbounded memory.
- **F1 / G:** hash chains give integrity, not confidentiality or completeness; completeness comes from anchoring heads outside the writer's control and from sequence numbers without gaps per shard. A single chain serialises appends (in memory it is one monitor; in a database one row lock on the head) - chain per shard, or batch events into blocks (Merkle tree per block, chain the block roots) so verification of one event is O(log n) with a proof. In `InMemoryAuditLog` queries by actor are O(log n + page) thanks to the per-actor index; the price is memory (two index lists of ints) and that an unbounded log needs segmenting and archival. A read-write lock helps if reads dominate; for the write path a lock-free design needs the hash of the previous event, which makes appends inherently sequential: batch them.
- **F2:** the expensive dimension is cardinality and retention, not ingest rate: decide rollup resolutions per query pattern, drop raw early. Downsampling with `count/sum/min/max` is mergeable (associative), so rollups can be recomputed from smaller rollups and late data fixes only the affected buckets; percentiles need sketches (t-digest, HDR histogram) that are also mergeable. Event-time windows with watermarks bound state; accept very late data only into the raw store. Use per-device sequence numbers to detect gaps and duplicates instead of relying on timestamps (device clocks drift).
- **H1:** the follow-up is the rule behind each result: JLS 12.4/12.5 (class and instance initialisation), 15.26.2 (compound assignment implicit cast), 15.19 (shift distance masking), 5.1.3, 5.1.7 (boxing, Integer cache), 15.28 (constant expressions and interning), 4.6 (erasure), 14.11.3 (switch fall-through), 15.23 (short-circuit operators).
- **H2:** the one-element look-ahead is the same trick as `PeekingIterator` in guava and the `Spliterator.tryAdvance` shape; implementing `hasNext()` as "advance if needed" makes it idempotent. Lazy combinators compose without intermediate collections (a pipeline over a billion rows uses O(k) memory), which is what `Stream` does with its own spliterator machinery - prefer `Stream` for real code, write iterators when you need pull-based control (merging sorted sources, cursors over a database page API). Iterators are not thread-safe; share the source through a queue, not the iterator. `windows` copies k elements per window (O(k)); a persistent/circular structure with views would avoid the copy at the cost of the "earlier windows do not change" guarantee.
