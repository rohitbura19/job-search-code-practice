# Mon 2 Nov 2026 - practice

One focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the invariant or recurrence in one sentence each; mark "needed hints" in the score card.

## Order and time boxes

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 Excel columns, add binary, count bits | 10 | a1/BitsAndStrings.java | BitsAndStringsTest |
| 2 | A2 monotonic stack: temperatures, circular next greater, k-duplicate removal | 10 | a2/MonotonicStack.java | MonotonicStackTest |
| 3 | C1 heaps: k-th largest stream, k closest, merge k sorted | 20 | c1/Heaps.java | HeapsTest |
| 4 | C2 DP over an amount: coin change, combinations, equal-sum partition | 20 | c2/DpBasics.java | DpBasicsTest |
| 5 | D1 sliding window maximum (monotonic deque, O(n)) | 20 | d1/SlidingWindow.java | SlidingWindowTest |
| 6 | E Spring Boot: pessimistic lock + idempotency key + validation + `@ConfigurationProperties` + ProblemDetail (`e-spring-wallet/`) | 30 | that project | AccountRepositoryTest, WalletControllerTest, WalletServiceTest, WalletPropsTest |
| 7 | B1 streams/collections: grouping, Optional, partitioningBy, LRU `LinkedHashMap` | 15 | b1/StaffStats.java | StaffStatsTest |
| 8 | H1 predict the output | 15 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 State pattern: vending machine | 20 | h2/VendingMachine.java | VendingMachineTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review each) | 34 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1: token bucket + sliding window log limiters (sketch 8, code 22) | 30 | my-designs/G-classes-sketch.md, then g/TokenBucketLimiter.java, g/SlidingWindowLimiter.java | TokenBucketLimiterTest, SlidingWindowLimiterTest |

Core set if short on time: 3, 5, 6, 11 and F1 out loud (about 1.5 h). Code is under `src/main/java/practice/d20261102/`. Run one test class: `mvn test -Dtest=HeapsTest`. Spring project: `cd e-spring-wallet && mvn test`. Reference solutions: `answers/` (Java files mirror `src/main/java`; `answers/e-spring-wallet/` holds the completed gap files; the explanation is in each file's header; `answers/README.md` lists complexities).

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

- **A1 / A2:** bijective base 26 is "subtract one, then mod": say why. `addBinary` is the carry loop that big-number add reduces to; for very long inputs process 32 or 64 bits per step. `countBits` has a second O(n) form `out[i] = out[i & (i - 1)] + 1`; for a single number use `Integer.bitCount` (POPCNT). The monotonic stack is O(n) because each index is pushed and popped once; the follow-up is "previous smaller element" and the histogram/stock-span family. For k-duplicate removal the run-length stack stays O(n) with no re-scan; the naive repeat-until-stable loop is O(n^2/k).
- **B1:** `groupingBy(..., TreeMap::new, ...)` costs O(log m) per element; if you only need ordered output, group into a `HashMap` and sort the keys once. `Collectors.toMap` throws on duplicate keys: supply the merge function. `averagingDouble` uses Kahan summation; for money use `BigDecimal` and `reducing`. `LinkedHashMap` access order is not thread-safe, and even `get` mutates it: wrap with `Collections.synchronizedMap` for light use, or use Caffeine (W-TinyLFU, striped, async eviction) for real traffic.
- **C1:** a bounded min-heap of size k is O(log k) per add against O(n) for re-sorting. For k closest, quickselect gives O(n) expected but mutates the array and is not streaming; a max-heap of size k keeps memory O(k). Merge k sorted: heap is O(N log k); pairwise merge in a tournament is the same, and on disk you read blocks per cursor. For a stream of percentiles use two heaps or a t-digest.
- **C2:** both coin problems are unbounded knapsacks, O(amount * coins); `canPartition` is the 0/1 knapsack and a `BitSet` shift (`bits |= bits << v`) makes it 64x faster. Loop order decides combinations vs permutations. For a huge amount and few coins, the minimum can be found with a BFS over residues (Dijkstra on the smallest coin's residues). Answers can overflow: say whether you use `long`, modulus or `BigInteger`.
- **D1:** deque of indices is O(n) and O(k) memory; the heap with lazy deletion is O(n log n). Equal values are popped because the newer one outlives the older one in every later window. Alternatives: block decomposition with prefix and suffix maxima (two passes, branch-free, easy to vectorise). For a true stream, keep only the deque of (value, expiry time).
- **E:** `PESSIMISTIC_WRITE` holds row locks until commit, so keep the transaction short and lock in a global order (ascending id) or two opposite transfers deadlock. Optimistic `@Version` with retry is better when contention is low; here hot accounts justify the lock. The idempotency check-then-insert is racy: the unique index on the key is the real guard, and the handler maps the violation to a retryable 409. Store the response (status + body) with the key if the replay must be byte-identical, and expire keys after 24 h. Use `BigDecimal` with explicit scale and never `double` for money; compare with `compareTo`, not `equals`. Replace the `@Transactional` single-DB transfer by a ledger of double-entry rows (append-only, balance = sum or a maintained snapshot) when audit and cross-service sagas matter. `open-in-view=false` keeps lazy loading out of the controller.
- **F1 / G:** the token bucket stores two numbers per key and allows a burst up to capacity; the sliding log is exact but O(limit) memory per key; the sliding-window counter (two adjacent fixed windows weighted by overlap) is O(1) with about 1% error and is usually the best default. Distributed: one Redis Lua script per check keeps it atomic (about 0.2 ms); shard by key hash; hot keys get local pre-allocation of tokens (each node takes a slice of the budget and refills it periodically), trading exactness for latency. Decide fail-open (availability) or fail-closed (protection) per endpoint class. Time comes from the store, not from node clocks. Rejections should be cheap and must not consume the store. In the LLD, `compute` gives per-key atomicity without a global lock, a `Clock` makes the tests deterministic, and idle-key eviction is mandatory or the map grows with every key ever seen.
- **F2:** geohash/H3 cells make "near me" a lookup of the cell plus its neighbours; cell size must match the search radius (query 9 cells, then filter by exact distance). Updates overwrite one record per driver (latest wins) with a TTL, in memory, sharded by city; durable history goes to a stream for analytics, not into the hot path. Matching is a small state machine per request with offers that expire; use a per-driver "reserved" flag set with compare-and-set (or a single owner per city shard) so a driver is never offered two rides. Rank by road ETA, not straight-line distance, but pre-filter by distance. Under load drop location updates (stale ones are worthless) before dropping match requests.
- **H1:** rules behind each result: JLS 8.3 (fields are not overridden), 8.4.8.2 (static methods are hidden), 15.12.2 (overload phases: strict/widening, then boxing, then varargs), 14.20.3 (try-with-resources closes in reverse order and suppresses), `java.util.stream` laziness (pull-based, `sorted` is a barrier, the source is read at the terminal operation), IEEE 754 (NaN, -0.0) with `Double.equals`, `Math.round` = floor(x + 0.5), narrowing of floating to integral saturates, `subList` is a view with fail-fast checks, `Iterator.remove`, the for-each/`hasNext` quirk, `TreeMap` uses the comparator for key equality, record equals with array components, pattern-switch order and `case null`, switch fall-through.
- **H2:** State vs Strategy: the state object changes itself (transitions) while a strategy is chosen from outside. Benefits: each state's rules in one class, new state without touching the others. Cost: more classes and shared context (here the credit and stock live in the machine). Make states stateless singletons (or enum constants with behaviour) when they hold no data; make the public methods `synchronized` or use a single-threaded actor for the machine. Persisting the machine means storing just the `Status` enum plus credit, and rebuilding the state object on load.
