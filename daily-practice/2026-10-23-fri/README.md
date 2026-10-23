# Fri 23 Oct 2026 - practice

Interview day (Light & Wonder, 5 PM Teams, per the earlier plan; IDFC drive is tomorrow): do the **core set first** (about 1.5 h), then stop at least 2 h before the call. The rest is for the evening or the weekend.

Warm-up (10 min, blank page): `questions/00-warmup.md`. State the recurrence or invariant in one sentence each; mark "needed hints" in the score card.

## Order and time boxes (about 3 h)

| # | Item | Minutes | Write code / answer in | Test |
|---|---|---|---|---|
| 0 | Warm-up | 10 | paper | - |
| 1 | A1 majority element (`questions/A1-majority-element.md`) | 12 | a1/Majority.java | MajorityTest |
| 2 | A2 house robber | 12 | a2/HouseRobber.java | HouseRobberTest |
| 3 | C1 top K frequent words (20 min timer) | 20 | c1/TopKWords.java | TopKWordsTest |
| 4 | C2 LIS + envelopes | 25 | c2/Increasing.java | IncreasingTest |
| 5 | D1 largest rectangle in a histogram | 25 | d1/Histogram.java | HistogramTest |
| 6 | E Spring Boot + JPA + service (`e-spring-slot-booking/`) | 35 | that project | SlotRepositoryTest, SlotControllerTest, SlotServiceTest |
| 7 | B1 streams / collectors | 20 | b1/TripStats.java | TripStatsTest |
| 8 | H1 predict the output | 20 | h1/H1Predictions.java | H1PredictionsTest |
| 9 | H2 composite `DirEntry` (OOP) | 30 | h2/DirEntry.java | DirEntryTest |
| 10 | F1 + F2 HLD, out loud (12 min each, then 5 min self-review) | 30 | my-designs/F1..., F2... | - |
| 11 | G LLD of F1 (sketch 10, code 30) | 40 | my-designs/G-classes-sketch.md, then g/InMemoryScheduler.java | InMemorySchedulerTest |

Core set if short on time: 3, 5, 6, 8 and F1 out loud (about 1.5 h). Code paths are under `src/main/java/practice/d20261023/`. Run one test class: `mvn test -Dtest=HistogramTest`. Reference solutions: `answers/` (the Java files mirror `src/main/java`; the explanation is in each file's header; `answers/README.md` lists complexities).

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

- **A1 / A2:** the follow-up is "why is it correct": cancelling distinct pairs cannot remove a true majority, and the DP only needs the last two states. Efficiency: both are single passes with O(1) memory; for A1 on a stream the same counter works; for "more than n/k" keep k-1 candidates (Misra-Gries) and verify with a second pass or accept approximate counts.
- **B1:** seven passes over the list could be ONE pass with a custom collector or a loop updating several maps; keep streams for clarity and use loops when profiling says so. Totals as `long` cents, never `double`. Parallel streams only for big, splittable, CPU-bound work, never with a shared mutable accumulator.
- **C1:** O(n + d log k) with the bounded heap; a bucket sort by frequency is O(n + d) then a sort inside each bucket. For streams: exact counts sharded by hash of the word, a per-shard top-k, then merge; a count-min sketch when memory matters and approximate counts are acceptable.
- **C2:** O(n log n) because `tails` is sorted. Return the sequence with parent indices; count them with a BIT or segment tree over compressed values. Envelopes: the descending-height trick is the whole idea; mention the 3-D variant (CDQ divide and conquer or a 2-D BIT).
- **D1:** O(n) with an `int[]` stack (no boxing). The "maximal rectangle in a binary matrix" is this per row (O(rows x cols)). A divide-and-conquer with a segment tree for range-minimum is O(n log n) and handles online updates.
- **E:** the atomic `update ... where booked < capacity` serialises on one row and never oversells; it also makes the `@Version` redundant for this path, so say which writes still need it (edits of slot attributes). Under heavy contention on one hot slot, partition capacity into N sub-counters and sum on read, or queue reservations per slot (Kafka key = slotId) so one consumer applies them in order. Add an idempotency key per reservation (unique `(slotId, requestId)` table) so a client retry does not book twice; add keyset paging to `available`; index `(stationId, startsAt)` (the unique constraint already provides it).
- **F1 / G:** the in-memory scheduler is exact but single-node and runs jobs under its lock. Production: copy the due batch under the lock, run jobs on a worker pool outside it, re-lock only to record the outcome; use a hashed timing wheel (O(1) schedule/cancel) when there are millions of near-term timers; persist jobs and take shard leases with fencing tokens so a paused node cannot double-fire; jittered backoff (full jitter) to avoid retry storms; cap the dead-letter list and expose a replay API.
- **F2:** a sorted set gives O(log n) update and rank; the real limits are memory (about 100 bytes per member) and a single hot key. Shard by score range or by player-hash with a top-k merge; cache the top-100 and serve "my neighbours" with `ZREVRANGE rank-5..rank+5`; derive weekly/daily boards from the same event stream rather than copying members between keys; reconcile with a periodic rebuild from the log.
- **H1:** the follow-up is the JLS/Javadoc rule behind each result: close order and suppression (JLS 14.20.3), conditional-expression typing (JLS 15.25), the hashCode/equals contract, stream pipelines pulling element by element, and no virtual dispatch for fields or statics.
- **H2:** the follow-up is the immutability alternative: build the tree with a builder and freeze it, which removes the cycle problem and the need for locks. Replace the recursive `size()` by a cached size invalidated through parent pointers, or an iterative walk when the depth is unbounded; say what each costs.
