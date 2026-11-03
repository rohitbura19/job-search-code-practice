# Tue 3 Nov 2026 - practice

One focused block of about 3 hours. Do the **core set first** (about 1.5 h), then the rest. No IDE help for the warm-up and the HLD parts.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the invariant or recurrence in one sentence each; mark "needed hints" in the score card.

## Order and time boxes

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 majority element, Pascal row, brackets | 10 | a1/Basics.java | BasicsTest |
| 2 | A2 tree recursion: diameter, balanced, path sum | 10 | a2/TreeRecursion.java | TreeRecursionTest |
| 3 | C1 topological sort: can finish, smallest order, semesters | 20 | c1/CourseOrder.java | CourseOrderTest |
| 4 | C2 subsequence DP: LIS n log n, LCS, circular robber | 20 | c2/Subsequences.java | SubsequencesTest |
| 5 | D1 Trapping Rain Water II (min-heap flood) | 20 | d1/TrappingRain2.java | TrappingRain2Test |
| 6 | E Spring Boot: overlap `@Query` + `@AssertTrue` + paging + `@Modifying` + `@ConfigurationProperties` + ProblemDetail (`e-spring-bookings/`) | 30 | that project | BookingRepositoryTest, BookingControllerTest, BookingServiceTest, BookingPropsTest |
| 7 | B1 collections: `merge`, `computeIfAbsent`, `removeIf`, entry comparators | 15 | b1/MapApi.java | MapApiTest |
| 8 | H1 predict the output | 15 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 Composite + Visitor: file tree | 20 | h2/FileNode.java, DirNode.java, Visitors.java | FileSystemTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review each) | 34 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1: delayed job scheduler with retries (sketch 8, code 22) | 30 | my-designs/G-classes-sketch.md, then g/JobScheduler.java, g/RetryPolicy.java | JobSchedulerTest |

Core set if short on time: 3, 5, 6, 11 and F1 out loud (about 1.5 h). Code is under `src/main/java/practice/d20261103/`. Run one test class: `mvn test -Dtest=CourseOrderTest`. Spring project: `cd e-spring-bookings && mvn test`. Reference solutions: `answers/` (Java files mirror `src/main/java`; `answers/e-spring-bookings/` holds the completed gap files; the explanation is in each file's header; `answers/README.md` lists complexities).

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

- **A1 / A2:** Boyer-Moore only works when a majority is guaranteed; without the guarantee add a counting pass, and for "elements above n/k" keep k-1 candidates (Misra-Gries, also the basis of heavy-hitter sketches). Pascal rows overflow `int` at row 34: switch to `long`/`BigInteger` or a modulus, and use `C(r,k+1) = C(r,k)*(r-k)/(k+1)` for a single cell in O(k). For brackets, a counter is enough with one bracket type; with several you need the stack, and the follow-up is "minimum removals" or "longest valid substring". The tree methods are post-order passes returning a value plus a side result; recursion depth is the tree height, so for skewed trees of 10^6 nodes use an explicit stack or Morris traversal, or run in a thread with a bigger stack as a stop-gap.
- **B1:** `merge`/`compute`/`computeIfAbsent` do one hash lookup instead of two and are atomic on `ConcurrentHashMap` (the function must be short and must not touch the same map). A null from `merge`/`compute` removes the key, which is what makes `mergeAll` drop zero totals. `removeIf` on an `ArrayList` is one O(n) compaction pass; repeated `remove(i)` is O(n^2). `topK` with a full sort is O(m log m); a bounded min-heap of size k is O(m log k). `groupingBy` into a `TreeMap` costs O(log m) per insert; sort the keys once if you only need ordered output.
- **C1:** Kahn gives cycle detection for free (fewer than n nodes emitted). A heap makes the order smallest-first at O(log n) per node; if any valid order will do, a plain queue is O(n + e). `minSemesters` is the longest path in the DAG (level-by-level BFS). A DFS version needs three colours and recursion depth up to n, which is why the iterative form is the safe one. Follow-ups: alien dictionary (build edges from adjacent words), parallel scheduling with durations (critical path), incremental updates (maintain indegrees, detect a new cycle by a reachability check from the new edge's target).
- **C2:** LIS `tails` is a patience-sorting pile top array; to rebuild the sequence keep for each element the index of its predecessor and the index sitting at each length. LCS is O(nm) and the rolling row loses the path; Hirschberg recovers it in O(min) space. When one string is long and alphabets are small, bit-parallel LCS is 64x faster. Circular robber reduces a cycle to two lines by cutting one edge: say "either house 0 or house n-1 is unused".
- **D1:** The level of a cell is `max(own height, min over paths to the border of the highest wall)`; a min-heap flood from the border visits cells in increasing wall height, so the first time you touch a cell the boundary that reached it is the lowest possible. Each cell is pushed once: O(mn log mn). The 1D two-pointer works because the minimum of two sides is known; in 2D the "side" is a ring of unknown shape. A bucket queue (heights up to 20_000) turns the heap into O(mn + H). Check the overflow bound explicitly: 300 x 300 x 20_000 = 1.8e9 fits `int`, larger grids need `long`.
- **E:** The half-open interval and `start < :end and end > :start` is the whole overlap rule. The check-then-insert is a race: use a row lock on the room, `SERIALIZABLE` with retry, or a database exclusion constraint (PostgreSQL `EXCLUDE USING gist (room WITH =, tstzrange(start_at, end_at) WITH &&)`), which is the only version that is correct under any number of app instances. Keep `Page` out of JSON (the unstable `PageImpl` serialisation): return a list plus `X-Total-Count` or a small response record. A paged `count(*)` is a full scan on big tables; use keyset paging on `(startAt, id)` for deep pages. `@Modifying` queries bypass the persistence context: clear it (`clearAutomatically`) if you read the same entities afterwards. Store timestamps as `Instant`/UTC and convert at the edge. Use `ProblemDetail` consistently (and `spring.mvc.problemdetails.enabled=true` for the built-in exceptions).
- **F1 / G:** Storage choice decides the design. A relational table `(due_at, status)` with `SELECT ... FOR UPDATE SKIP LOCKED LIMIT n` is simple and durable up to a few thousand jobs/s. Beyond that, partition by time bucket (minute) so a poller only reads the current bucket, or use a Redis ZSET scored by due time with a Lua claim script, or a timing wheel in each worker fed from the durable store. Claims need a lease (visibility timeout) and a fencing token so a slow worker cannot overwrite the retry's result; handlers must be idempotent because delivery is at least once. The 09:00 spike: jitter non-critical jobs, shard the bucket by job-id hash, per-tenant concurrency caps. Long delays (months) stay in cold storage and are loaded into the hot set a few minutes early. In the LLD, lazy deletion makes cancel O(1), the sequence number makes ordering deterministic, running jobs outside the lock avoids deadlocks when a job calls `schedule`, and the `Clock` makes everything testable. A real worker sleeps until `nextDue()` and is woken by `schedule` when the new head is earlier (`Condition.awaitNanos` or a `DelayQueue`).
- **F2:** A Redis sorted set gives O(log n) update and rank for tens of millions of members on one node; the top-N is a range read of the head and can be cached for a second. Beyond one node, shard by score range (so global rank = members in higher shards + local rank) or approximate: exact ranks for the top 10k, percentile estimates (histogram buckets with counts) for everyone else. Ties: encode `score` and `(MAX - timestamp)` in one sortable value. Weekly boards are separate keys with TTL, "all-time" is a merge target updated by the same event. Score updates arrive as idempotent events (player, match id) through a queue; the board is a rebuildable projection of that log. Reads far outnumber writes, so replicas serve rank reads.
- **H1:** rules behind each result: JLS 12.4/12.5 (class init once, superclass first; instance initialisers run after `super()` returns, so a super constructor sees default field values), 14.20.2 (`finally` completes abruptly -> its return wins; the return value is copied before `finally`), 5.1.7 and 15.28 (Integer cache, constant folding, interned constants), 15.25 (`?:` typing: boxed Integer with a double operand promotes to double), 15.26.2 (compound assignment has an implicit cast), HashMap bucket by hash then `equals`, the `Collection` equality contracts, `Arrays.asList` write-through and fixed size, type erasure (JLS 4.6) and array covariance (ArrayStoreException), overload phases (JLS 15.12.2: widening before boxing), unary/binary operator rules and shift count masking (JLS 15.19), saturating double -> int casts.
- **H2:** Composite makes leaves and branches interchangeable; the cost is that child-management methods (`add`) live only on `DirNode` here, which is the safe variant (the alternative puts `add` on `Node` and throws on files). Visitor adds operations without touching the node classes, at the price of making new node TYPES expensive (every visitor changes). With sealed interfaces and pattern-matching `switch` you get the same extensibility for operations with less ceremony and exhaustiveness checks, so prefer that in new code and keep Visitor for libraries and for cases where traversal logic belongs in one place. `size()` is O(subtree) every call; cache it with invalidation along the parent links if reads dominate.
