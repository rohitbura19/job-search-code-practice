# Reference solutions (open only after trying)

The Java files here mirror `src/main/java`; to check them, copy `answers/java/*` over `src/main/java/` in a scratch copy and run `mvn test`.
The Spring solution is under `e-spring-slot-booking/src/` (copy over the project's `src/`). Each file's header comment has the idea and the complexity.

| Item | File | Time | Space |
|---|---|---|---|
| A1 | java/.../a1/Majority.java | O(n) | O(1) |
| A2 | java/.../a2/HouseRobber.java | O(n) | O(1) |
| B1 | java/.../b1/TripStats.java | O(n), sorted outputs O(n log n) | O(n) |
| C1 | java/.../c1/TopKWords.java | O(n + d log k) | O(d) |
| C2 | java/.../c2/Increasing.java | O(n log n) | O(n) |
| D1 | java/.../d1/Histogram.java | O(n) | O(n) |
| E | e-spring-slot-booking/src/... | - | - |
| G | java/.../g/InMemoryScheduler.java | O(log n) per schedule/cancel/pop | O(n) |
| H1 | java/.../h1/H1Predictions.java | - | the reasoning is in the header |
| H2 | java/.../h2/DirEntry.java | size O(nodes); add O(log c + subtree) for the cycle check | O(nodes) |

F1 / F2 (HLD) anchors.
**F1:** store jobs in a DB/KV partitioned by `(shard, time bucket of 1 s or 1 min)`; a scheduler node owns a shard via a lease (with a fencing token), loads the next minute into an in-memory timing wheel / delay queue, fires, and marks the row `DONE` (or reschedules with backoff) in the same step; delivery is at-least-once so consumers dedupe on `jobId + attempt`; cancel is a conditional update (`WHERE state='PENDING'`) so it loses cleanly to a firing; spread the 09:00:00 spike with a jitter window or pre-split buckets; alert on fire lag.
**F2:** Redis sorted set per board (`ZINCRBY`/`ZADD`, `ZREVRANGE 0 99`, `ZREVRANK`), 20M members is about 2 GB per board; score events come from Kafka keyed by player and are applied idempotently (event id / monotonic version); cache the top-100 for 1 s; period boards are separate keys named by period, so rollover is "start writing to the new key" and old keys expire by TTL; shard by score range only when a single instance is too big, and answer rank for the far tail approximately; rebuild from the event log after a loss.
