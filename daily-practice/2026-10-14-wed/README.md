# Wed 14 Oct 2026 - practice

Topics: in-place merge, bit manipulation, top-K, union-find, edit distance (2D DP), telemetry streams, transactional outbox + retry/DLT + JWT, smart-charging schedules (time zones), prepaid wallet ledger, boxing traps, initialization order.

## Order for today
1. **Minimum set (about 65 min):** `questions/00-warmup.md` (10 min) -> **C1** with a 15 min timer -> **E** (25 min, Spring project: AlertService and OutboxRelay tests) -> **H1** predictions (10 min) -> closing question 1 out loud (5 min).
2. Extended set: A1, A2, B1, C2, D1, F1, F2, G, H2 and the rest of the closing round.

| Item | Question | Where you write code / answer | Test |
|---|---|---|---|
| Warm-up | questions/00-warmup.md | paper | - |
| A1 | questions/A1-merge-sorted-array.md | a1/MergeSortedArray.java | MergeSortedArrayTest |
| A2 | questions/A2-single-number.md | a2/SingleNumber.java | SingleNumberTest |
| B1 | questions/B1-telemetry-streams.md | b1/TelemetryStats.java | TelemetryStatsTest |
| C1 | questions/C1-top-k-frequent.md | c1/TopKFrequent.java | TopKFrequentTest |
| C2 | questions/C2-redundant-connection.md | c2/Dsu.java, c2/RedundantConnection.java | DsuTest, RedundantConnectionTest |
| D1 | questions/D1-edit-distance.md | d1/EditDistance.java | EditDistanceTest |
| E | questions/E-outbox-kafka-jwt.md | e-spring-outbox/ (AlertService, OutboxRelay, AlertController); JPA, consumer, security, YAML on paper | AlertServiceTest, OutboxRelayTest, AlertControllerTest |
| F1 | questions/F1-smart-charging-schedules-hld.md | my-designs/F1-smart-charging-schedules.md | say it out loud |
| F2 | questions/F2-prepaid-wallet-hld.md | my-designs/F2-prepaid-wallet.md | say it out loud |
| G | questions/G-schedule-engine-lld.md | my-designs/G-classes-sketch.md, then g/Schedule.java, AllOf.java, ScheduleHolder.java | ScheduleTest |
| H1 | questions/H1-boxing-traps.md | h1/H1Predictions.java (read H1Snippets.java) | H1PredictionsTest |
| H2 | questions/H2-init-order-decorator.md | h2/H2Predictions.java (read H2Snippets.java), h2/LoggingNotifier.java, RetryingNotifier.java, MetricNotifier.java | H2PredictionsTest, NotifierDecoratorTest |
| Close | questions/closing-round.md | out loud | - |

Code paths are under `src/main/java/practice/d20261014/`. Run the main kit with `mvn test`; run the Spring project with `mvn test` inside `e-spring-outbox/` (the relay timeout test waits about 5 seconds).

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

Minutes actually spent: ____
