# Thu 22 Oct 2026 - practice

Interviews: **Light & Wonder Fri 23 Oct 5 PM (Teams)**, **IDFC drive Sat 24 Oct 9:30 AM**. Today is the last full day before them: do the **core set first** (about 2 h), the rest in the evening or Fri morning.

Warm-up (10 min, blank page): the items due from 19/20/21 Oct: Word Break, Next Permutation, Word Ladder, Spiral Matrix, Clone Graph, Split Array Largest Sum, Quickselect, build tree from traversals, weighted job scheduling. State the recurrence or invariant in one sentence each; mark "needed hints" in the score card.

## Order and time boxes (about 3 h)

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up (above) | 10 | paper | - |
| 1 | A1 two pointers (`questions/A1-two-pointers.md`) | 10 | a1/TwoPointers.java | TwoPointersTest |
| 2 | A2 parentheses | 10 | a2/Parentheses.java | ParenthesesTest |
| 3 | C1 course schedule (20 min timer) | 20 | c1/CourseSchedule.java | CourseScheduleTest |
| 4 | C2 coin change | 25 | c2/CoinChange.java | CoinChangeTest |
| 5 | D1 sliding window maximum | 25 | d1/SlidingWindowMax.java | SlidingWindowMaxTest |
| 6 | E Spring Boot + JPA (`e-spring-meter-readings/`) | 30 | that project | MeterReadingRepositoryTest, ReadingControllerTest |
| 7 | B1 streams / collectors | 15 | b1/OrderStats.java | OrderStatsTest |
| 8 | H1 predict the output | 15 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 `Money` (OOP) | 30 | h2/Money.java | MoneyTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review) | 30 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1 (sketch 10, code 30) | 40 | my-designs/G-classes-sketch.md, then g/RateLimitedGate.java | RateLimitedGateTest |

Core set if short on time: 3, 4, 6, 8, 11 (and F1 out loud). Code paths are under `src/main/java/practice/d20261022/`. Run one test class: `mvn test -Dtest=CourseScheduleTest`. Reference solutions: `answers/` (the Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities).

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

- **A1 / A2:** the follow-up is always the invariant ("everything before `w` is final"). Efficiency: (A1a) fewer writes by swapping only when `w != i`; (A2) fail fast on odd length and on a closer when the remaining characters cannot balance the stack; a single pass with an `int[]` stack avoids boxing `Character`.
- **B1:** six passes over the list could be ONE pass with a custom collector or a loop that updates several maps; use streams for clarity, loops when profiling says so. Totals as `long` cents, never `double`. Parallel streams only for big, splittable, CPU-bound work.
- **C1:** Kahn is O(V+E); say that DFS needs recursion depth V. Efficiency: array-based adjacency (done here) beats `List<Integer>[]` for memory; for repeated edge inserts use an incremental topological order (Pearce-Kelly); report the cycle nodes for diagnostics (nodes with in-degree > 0 after Kahn).
- **C2:** O(amount x coins). Sort coins and break early in (a); use the rolling array (done); with huge amount and few coins use periodicity/shortest-path-on-residues (Dijkstra over `amount mod smallestCoin`: O(coin x smallest)). Count with `long` and say what you do when it overflows (`BigInteger` or modulo as the interviewer asks).
- **D1:** O(n) with the deque; the block prefix/suffix max trick is O(n) with no deque and is easy to parallelise. A ring buffer of size k cuts memory from O(n) to O(k).
- **E:** the repository is called straight from the controller: add a service with `@Transactional(readOnly = true)` once there is a rule. For ingestion volume switch the id to a `SEQUENCE` with allocation size 50 and enable JDBC batching; add a unique `(meterId, readAt)` for idempotent retries; replace `OFFSET`-style paging by keyset (`readAt < :cursor`); put a Kafka topic keyed by `meterId` in front of the writer so per-meter order is kept and the DB sees steady batches.
- **F1 / G:** the in-process sliding-log gate is exact but costs O(N) timestamps per active key and one global lock. Production: a sliding-window COUNTER (two fixed windows weighted, O(1) memory, about 1 % error) in Redis via one Lua script per decision, sharded by `userId`; evict by TTL; fail OPEN for marketing and fail CLOSED-with-fallback (local limiter) for OTP; keep decisions as events for analytics instead of querying the hot store; held quiet-hours messages go to a delay queue keyed by the user's next window start. Sharding the in-process lock by `userId.hashCode()` removes contention.
- **F2:** a single-database transfer with ordered row locks and a unique idempotency key is simple and correct up to a few thousand TPS; shard by account only when needed and then use a saga with a PENDING state and an outbox, not 2PC. For hot merchant accounts split the balance into sub-accounts (striping) or batch credits asynchronously and reconcile. Derive statements from the ledger on a read replica or a CQRS read model.
- **H1:** the follow-up is the JLS rule behind each result; know the Integer cache, constant folding, init order and `modCount` by heart.
- **H2:** the follow-up is the contract between `equals`, `hashCode` and `compareTo`; make the type a `record` with a compact constructor if allocation logic can live in a separate method.
