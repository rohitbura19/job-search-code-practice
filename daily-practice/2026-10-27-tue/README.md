# Tue 27 Oct 2026 - practice

One focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the recurrence or invariant in one sentence each; mark "needed hints" in the score card.

## Order and time boxes (about 3 h)

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 max subarray sum + product (`questions/A1-max-subarray.md`) | 15 | a1/Subarrays.java | SubarraysTest |
| 2 | A2 level order + zigzag + right-side view (iterative BFS) | 15 | a2/LevelOrder.java | LevelOrderTest |
| 3 | C1 3Sum + 3Sum closest | 25 | c1/ThreeSum.java | ThreeSumTest |
| 4 | C2 permutations II + next permutation | 25 | c2/Permutations.java | PermutationsTest |
| 5 | D1 count smaller after self + inversions (Fenwick tree) | 25 | d1/CountSmaller.java | CountSmallerTest |
| 6 | E Spring Boot: custom constraint + embedded + bulk update + ProblemDetail (`e-spring-devices/`) | 35 | that project | DeviceRepositoryTest, DeviceControllerTest, DeviceServiceTest, SerialValidatorTest |
| 7 | B1 streams and collectors on shipments | 20 | b1/ShipmentStats.java | ShipmentStatsTest |
| 8 | H1 predict the output | 20 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 Command pattern: undo / redo (OOP) | 30 | h2/*.java | UndoManagerTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review) | 30 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1 (sketch 10, code 25) | 35 | my-designs/G-classes-sketch.md, then g/ | InMemoryLockServiceTest |

Core set if short on time: 3, 5, 6, 8 and F1 out loud (about 1.5 h). Code paths are under `src/main/java/practice/d20261027/`. Run one test class: `mvn test -Dtest=CountSmallerTest`. Reference solutions: `answers/` (the Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities).

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

- **A1 / A2:** Kadane is a one-state DP; the divide-and-conquer version (best left, best right, best crossing) is the one that parallelises and the segment-tree node for range-max-subarray queries. For the product, zeros split the array into independent segments and the answer within a segment is decided by the first/last negative: a two-pass prefix/suffix product also works. For A2, BFS holds a whole level in memory (O(w), up to n/2); DFS with a depth parameter gets the same lists with O(h) stack but is unsafe for deep chains unless made iterative. In a distributed tree (sharded graph) level-order becomes a frontier expansion with a per-level barrier.
- **B1:** eight methods mean eight passes; one custom `Collector` (or a loop) computing all aggregates in a single pass is faster when profiling says so. `groupingBy` into a `TreeMap` costs O(log k) per element; group first, sort the keys once if the groups are few. Cost as `long` paise avoids float error; `IntSummaryStatistics` sums into a `long` but `getAverage` is a double (fine at the edge). `parallelStream` needs associative, non-interfering collectors and is only worth it on large CPU-bound work, never on small lists or blocking IO.
- **C1:** O(n^2) is the known bound for 3Sum under standard conjectures; constants improve with an early exit when `a[i] > 0`, skipping when `a[i] + 2*max < 0`, or a hash-set variant when memory is cheap. k-Sum generalises to O(n^(k-1)) by recursion over 2Sum. For 3Sum Closest an early return on an exact hit is the only pruning; with values bounded (|x| <= 10^5) a bitset convolution/FFT solves the count of triplets faster than quadratic for dense inputs.
- **C2:** P results cost O(n*P) anyway, so the duplicate skip (not a `HashSet` of lists, which allocates and hashes every duplicate) is the efficiency point. Heap's algorithm generates permutations with one swap per step but not in lexicographic order. For the k-th permutation use the factorial number system in O(n^2) (or O(n log n) with a Fenwick tree) without generating the previous ones. `nextPermutation` as an iterator gives O(1) amortised work per element and no list at all: return a lazy stream in a real API.
- **D1:** the Fenwick tree and merge sort are both O(n log n); merge sort needs no coordinate compression and is cache-friendlier, the Fenwick tree extends to online queries and to "count in value range" with a second pass. With values already in a small range (here a rank space) a plain counting array is O(n * range) so only wins for tiny ranges. An order-statistic tree (a balanced BST with subtree sizes) answers the same online but has worse constants. `inversions` as a `long` matters: 200000 elements already exceed `Integer.MAX_VALUE` / 2 pairs.
- **E:** a custom constraint belongs in a validation layer shared by REST and messaging inputs (it is plain Bean Validation, no Spring needed); keep validators stateless and cheap, and do not hit the database in one (the uniqueness check stays in the service plus the constraint). The bulk update skips lifecycle callbacks, `@Version` and the cache: use it for housekeeping, not for domain changes, and batch by id range with a limit if the table is huge (one giant UPDATE holds locks and bloats the WAL). `ContainingIgnoreCase` cannot use a B-tree index: add a trigram or functional index, or a search engine, once the table grows. Dirty-checking `patch` loads the whole row; for hot counters use a targeted `UPDATE`. A `RETIRED` flag read on every request wants an index on `(status, last_seen)`. `existsBy` + insert is two round trips: with a unique constraint alone you can skip the check and translate the violation, trading a friendlier error for fewer queries.
- **F1 / G:** the in-memory service is a single point of failure and is only correct inside one JVM; the same interface over etcd/ZooKeeper/Raft gives linearizable grants at ~ms latency and a few thousand writes per second per cluster, so lock per coarse resource (a job, an account shard), never per request. Lease length is a trade-off: short leases detect crashes quickly but force frequent renewals and risk false expiry on GC pauses; hence fencing instead of trusting the clock. Sweeping expired entries lazily (here) keeps reads cheap but leaks memory for one-off resources: add a periodic cleanup of slots with no live lease and an old counter only if tokens can be persisted elsewhere (dropping the counter would break monotonicity).
- **F2:** keyset pagination on an indexed, stable sort key makes each page O(page) instead of O(offset); for tens of millions of rows consider exporting from a CDC-fed analytical store (columnar, e.g. ClickHouse/BigQuery) so the OLTP database never sees the scan. Compress (gzip/zstd) on the fly and upload parts in parallel for throughput; sizing parts at 8-64 MB balances request count and retry cost. Admission control (queue length per tenant, max concurrent exports) matters more than raw worker speed: one tenant should not starve the rest. Cache identical exports by `filterHash` + data version when the underlying data changes rarely.
- **H1:** the follow-up is the rule behind each result: JLS 14.20.2 (try-finally completion), 5.1.3 (narrowing primitive conversion), 15.12.2 (overload phases), 8.4.8 (hiding vs overriding), 14.20.3.1 (try-with-resources translation and suppressed exceptions), 15.25 (conditional operator typing).
- **H2:** storing the inverse operation (or the removed text) is cheap but breaks when commands interleave on a shared document by other users: collaborative editors move to operational transformation or CRDTs. Coalesce rapid typing into one command (timestamp or word-boundary rule) so undo steps match what a user perceives. A bounded history is memory safety; for big documents prefer commands that store diffs, not snapshots (memento) and persist the history for crash recovery. Make the manager thread-confined (UI thread) rather than synchronised: the commands mutate shared state anyway.
