# Sat 31 Oct 2026 - practice

One focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the invariant or recurrence in one sentence each; mark "needed hints" in the score card.

## Order and time boxes

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 nearby duplicate, stock profit I/II | 10 | a1/ArrayBasics.java | ArrayBasicsTest |
| 2 | A2 two sum (sorted), container with most water | 10 | a2/TwoPointers.java | TwoPointersTest |
| 3 | C1 BST: validate, k-th smallest, LCA, sorted array to BST | 20 | c1/Bst.java | BstTest |
| 4 | C2 grid graphs: islands, max area, Pacific Atlantic | 20 | c2/Grids.java | GridsTest |
| 5 | D1 first missing positive (O(n), O(1) space) | 15 | d1/MissingPositive.java | MissingPositiveTest |
| 6 | E Spring Boot: soft delete + `@Version` + JPQL search + paging + `@ConfigurationProperties` + ProblemDetail (`e-spring-notes/`) | 30 | that project | NoteRepositoryTest, NoteControllerTest, NoteServiceTest, NotesPropsTest |
| 7 | B1 streams: filtering, teeing, flatMapping, summarizingInt | 15 | b1/MovieStats.java | MovieStatsTest |
| 8 | H1 predict the output | 15 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 immutable value object + Builder (OOP) | 20 | h2/Request.java | RequestTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review each) | 34 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1: read-through cache, TTL + LRU + single flight (sketch 8, code 22) | 30 | my-designs/G-classes-sketch.md, then g/ | TtlLruCacheTest |

Core set if short on time: 3, 5, 6, 11 and F1 out loud (about 1.5 h). Code is under `src/main/java/practice/d20261031/`. Run one test class: `mvn test -Dtest=BstTest`. Reference solutions: `answers/` (Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities).

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

- **A1 / A2:** the nearby-duplicate map grows to n; a sliding `HashSet` of the last k values caps memory at O(min(n, k)). Stock I is Kadane on the deltas; the "at most k transactions" version is the DP follow-up. Two sum on sorted input is O(n) and O(1) only because of the order: on unsorted input the hash map is the answer, and for a 3-sum you fix one element and run this scan. Container: the proof is an exchange argument, not a heuristic; be ready to state it. Always widen to `long` before multiplying.
- **B1:** `filtering` inside `groupingBy` keeps the group, `filter` before it drops it: say which one the requirement needs. `teeing` gives two results in one traversal; the averages map is built once and reduced twice. Building `Agg` records per movie allocates; for a hot path a single loop with primitive arrays is cheaper. Top-k only: a bounded heap is O(m log k) against a full sort.
- **C1:** the in-order walk uses O(h) space, Morris traversal makes it O(1) at the price of temporarily mutating the tree (unsafe for concurrent readers). For repeated k-th queries store subtree sizes (order-statistic tree) and descend in O(h). A skewed tree makes h = n: that is why self-balancing trees (AVL, red-black, B-tree on disk) exist. LCA on a general tree needs parent pointers or Euler tour + RMQ; the BST ordering is what makes it O(1) space.
- **C2:** flood fill is O(R*C) with a visited mark; keep the mark in place only if the caller allows mutation, otherwise a `BitSet`. For a stream of cells turning to land use union-find (near O(1) per update). A grid of 10^8 cells does not fit one machine: tile it, flood each tile, then merge components across tile borders. Pacific/Atlantic is "reverse reachability": compute from the sinks instead of from every source.
- **D1:** the cycle-sort placement is O(n) because each swap fixes one value. It destroys the order, so say so; if the input must stay intact, a `BitSet` of size n+1 costs n/8 bytes and is usually the better engineering answer. Variant: "find the duplicate / all duplicates" uses the same index-as-hash idea.
- **E:** `@SQLRestriction` is appended to JPQL and `find`, not to native SQL, and not to the unique index: a soft-deleted title blocks reuse. Fix with a partial unique index (`where deleted = false`, PostgreSQL) or by putting a `deleted_at` into the key. `@SQLDelete` on a versioned entity must accept id and version. The `@Version` token in the PUT body gives lost-update protection across HTTP calls (the entity version alone only protects within one transaction); an `ETag` / `If-Match` header is the standard carrier. `count then insert` for the quota is racy: two parallel creates can both pass at `max - 1`; fix with a lock on the owner row, a counter row updated conditionally, or accept soft limits. Offset paging degrades with deep pages and shifts under inserts: use keyset (seek) paging on `id`. `lower(...) like '%q%'` cannot use a B-tree index: use PostgreSQL `pg_trgm` or a search engine at scale. Soft-deleted rows need a retention job.
- **F1 / G:** the cache is a read-through layer with TTL as the safety net and events as the fast path; TTL jitter avoids synchronised expiry. Single flight protects the database from a stampede per node; across N nodes the DB still sees N loads, so put a shared L2 (or a request-coalescing tier) in front for the hottest keys, and serve stale while one caller refreshes. `invalidate` must also cancel in-flight loads or an old value is resurrected: the `inflight.get(key) == flight` check does that. One monitor is correct and simple; for contention stripe by `hash(key) % N`, or use Caffeine (W-TinyLFU beats plain LRU on scan-heavy traffic). Negative caching needs a short TTL, and creation events should evict the negative entry. Monitor hit ratio, load latency, evictions per second and in-flight count.
- **F2:** serving from a precomputed top-k per prefix (up to length 3-4) in memory makes the request path a hash lookup; longer prefixes go to a trie shard or a sorted index. Shard by the first two characters plus a hash for hot prefixes, since short prefixes carry most traffic. Rank with a time-decayed count so yesterday's spike fades; personalise by re-ranking the top 20-50, never by indexing per user. Rebuild the index offline, ship it as an immutable snapshot, hot-swap atomically; stream increments for trending. Client side: debounce 100-150 ms, cancel stale requests, cache by prefix.
- **H1:** rules behind each result: JLS 5.1.7/15.25 (boxing, ternary typing), 15.12.2 (overload phases: no boxing/varargs first), 8.3 and 15.12 (fields are not virtual, statics are hidden), 15.28 and `String.intern` (constant strings), `List.remove(int)` vs `remove(Object)`, the `ArrayList` iterator (`hasNext` is `cursor != size`), `HashMap` bucket placement by the old hash, JLS 14.20.3 (try-with-resources: reverse close, suppressed).
- **H2:** immutability is a checklist: final class, final fields, copy in, copy out, unmodifiable views, no `this` escape. The Builder is the mutable twin and `build()` validates in one place. The `TreeMap(CASE_INSENSITIVE_ORDER)` trap: `equals` is case-insensitive but `hashCode` is not, which breaks the contract; hash the lower-cased keys instead. A `record` gives `equals` for free but compares arrays by identity, so records holding `byte[]` need a manual `equals`. For many optional parameters a Builder beats telescoping constructors; for 2-3 required fields a record is enough.
