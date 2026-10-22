# Answers: F1 and F2 (HLD)

(Spoilers. Open only after your attempt.)

**Reference — F1**
- Algorithm: token bucket per key (capacity = burst, refill = rate). Memory is two numbers per key, allows bursts, O(1). Sliding-window-counter is the alternative when strict windows matter.
- Shared counters: Redis with a Lua script (read tokens, refill by elapsed time, decrement, return allow/deny + retry-after) so the check-and-update is atomic in one round trip. Key = `rl:{apiKey}:{route}`, TTL = time to refill fully.
```
Client -> Gateway node -> [local cache of config + small local token slice]
                            -> Redis Cluster (Lua token bucket)  <- config service (limits per plan)
                            -> upstream service
Deny: 429 + Retry-After + X-RateLimit-Remaining
```
- Latency: Redis round trip per request is expensive at 1M rps. Mitigation: each gateway pre-fetches a batch of tokens (e.g. 10% of the limit) from Redis and serves locally, syncing asynchronously. This trades a small overshoot for a big drop in Redis calls.
- Hot key (one big customer): shard the bucket into N sub-buckets, each holding limit/N, route by node.
- Failure: Redis down -> fail open with a conservative local limit for normal APIs, fail closed for expensive or abuse-prone ones (login, OTP).
- Config: limits in DynamoDB/SQL, pushed to nodes by pub/sub or polled every 30 s with ETag.
- Metrics: allowed/denied by plan, limiter latency, Redis CPU and hot-shard skew, override rate.

**Reference — F2**
```
Charger --MQTT--> AWS IoT Core --rule--> Kinesis(by chargerId) --> Session Processor (Spring Boot)
     --> DynamoDB latest_state(chargerId, sessionId, kwh, power, ts)
     --> publish "session-updated" to Push Gateway (pub/sub topic per sessionId)
App <--WebSocket/SSE-- Push Gateway nodes (stateless apart from connection table)
App on connect: GET /sessions/{id}/state (snapshot) then subscribe from last version
```
- Why SSE/WebSocket: server-initiated, avoids 1M clients polling every 3 s (about 330k rps of mostly useless reads). SSE is simpler (one way, auto-reconnect over HTTP), WebSocket if the app also sends commands.
- Fan-out: Push Gateway keeps `sessionId -> connections` in memory; a pub/sub layer (Redis pub/sub or SNS -> per-node SQS) delivers an update to the nodes that hold that session's connections. 1M connections at about 50k per node means about 20 nodes.
- Missed updates: every update carries a monotonically increasing `version` (device sequence); the client sends `lastVersion` on reconnect and gets a snapshot, which is idempotent.
- Back-pressure: coalesce updates per session (send latest only), cap at 1 push per second per user.
- Offline charger: if `now - ts > 30 s`, the app shows "Last updated 1 min ago" rather than stale data as live.
- Mobile fallback: background app -> use silent push (FCM/APNs) not a held socket.

### Principal-level discussion: make F1 more efficient
1. Redis is a single point of latency: how do you cut calls by 10x (local token slices, async sync) and what accuracy do you lose?
2. Rate limit by what key: IP behind NAT/CGNAT unfairly blocks many users. Combine API key + IP + route, and tier the limits.
3. Multi-region: one global Redis adds 100 ms. Per-region limiters with a split quota (each region gets limit/regions, rebalanced periodically). What is the worst case overshoot?
4. Where to enforce: CDN/WAF edge for volumetric abuse, gateway for per-customer quotas, service-level for expensive endpoints (bulkheads). Why all three?
5. What breaks first at 10x traffic? Where is the backpressure and how do you shed load (priority classes, drop free tier first)?
