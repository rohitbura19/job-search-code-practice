# Reference solutions (open only after trying)

Only files you must write are here (given types stay in `src/main/java`). The Java files mirror `src/main/java`; to check them, copy `answers/java/*` over `src/main/java/` in a scratch copy and run `mvn test`.
The Spring solution is under `e-spring-catalog/src/` (copy over the project's `src/`). Each file's header comment has the idea and the complexity.

| Item | File | Time | Space |
|---|---|---|---|
| A1 | java/.../a1/Palindromes.java | O(n) | O(1) |
| A2 | java/.../a2/ListCycle.java | O(n) | O(1) |
| B1 | java/.../b1/InventoryStats.java | O(n) per method, sorted maps O(n log n) | O(n) |
| C1 | java/.../c1/Rotated.java | O(log n); findMin O(n) worst case | O(1) |
| C2 | java/.../c2/Grids.java | O(rows x cols) | O(rows x cols) |
| D1 | java/.../d1/MinWindow.java | O(\|s\| + \|t\|) | O(1) |
| E | e-spring-catalog/src/... | - | - |
| G | java/.../g/DefaultFlagEvaluator.java | O(len of key + user id) per call | O(1) |
| H1 | java/.../h1/H1Predictions.java | - | the reasoning is in the header |
| H2 | java/.../h2/Exprs.java | O(nodes) | O(depth) |

F1 / F2 (HLD) anchors.
**F1:** control plane (admin API, audit log, versioned ruleset in a relational store) publishes one compact ruleset per environment to a CDN/object store and a push channel (SSE) with a version number; SDKs hold the full ruleset in memory, evaluate locally (unknown flag -> bundled default), poll with `ETag` as the fallback, so a kill switch reaches clients in seconds and the hot path has no network hop; bucketing is `hash(flagKey + userId) % 100` so a ramp only adds users; ruleset size (a few MB at most) is the scaling limit, not QPS; per-service and per-country targeting are rules evaluated in order, first match wins; guard writes with approval, schedules and expiry dates; the control plane being down only freezes changes.
**F2:** the producer writes the event and an outbox row in one transaction; a relay publishes to Kafka keyed by endpoint; dispatchers POST with a short timeout, an HMAC signature header over `timestamp.body` and the event id; 2xx = done, otherwise retry with exponential backoff and full jitter for about 3 days via delay topics or a scheduler table, then dead-letter and a replay API; each endpoint has a concurrency cap and a circuit breaker so slow endpoints back up only their own queue; block private IP ranges after DNS resolution and disallow redirects (SSRF); attempts are stored with a TTL; alert and auto-disable on sustained failure.
