# Answers: F1 and F2 (HLD)

(Spoilers. Open only after your attempt.)

**Reference — F1 (SQS-based, matches your Enphase experience)**
```
Charger -> AWS IoT Core (MQTT) -> IoT Rule -> SQS(events)
   -> Event Router (Spring Boot) : enrich + load preferences + template
        -> SQS(urgent)  -> Push/SMS workers   (small batch, tight visibility timeout)
        -> SQS(bulk)    -> Email workers      (large batch, rate limited)
   -> workers -> provider (FCM/APNs/SMS vendor/SES) -> status table + CloudWatch metrics
   -> failures -> retry with backoff -> DLQ -> alarm / replay tool
```
- Dedupe: `dedupeKey = userId + eventType + eventId`; conditional put in DynamoDB with TTL, or SQS FIFO with MessageDeduplicationId (note FIFO throughput limits).
- Preferences: DynamoDB (userId PK), cached in-process with short TTL.
- SQS is at-least-once, so workers must be idempotent. Visibility timeout > provider timeout. DLQ after maxReceiveCount=5.
- Urgent vs bulk on separate queues so a marketing blast can never delay a fault alert.
- Provider failover: circuit breaker per vendor, switch to secondary on error rate.
- Data: `notification(id, userId, type, channel, status, attempts, createdAt)` partitioned by day; keep 30 days hot.

**Reference — F2**
- Source of truth: SQL row per SKU with `UPDATE stock SET available = available - 1 WHERE sku=? AND available > 0` (check rows affected = 1). Correct, but one hot row serializes. Mitigation: shard stock into N buckets per SKU, or pre-load stock into Redis and decrement with a Lua script, with DB as the durable record.
- `reservation(id, sku, userId, status, expiresAt, idempotencyKey)`; unique(`userId`, `sku`) prevents double hold.
- Expiry: delayed message (SQS delay up to 15 min fits a 10 min hold) or a sweeper job; on expiry do a conditional release only if still HELD.
- Payment callback after expiry: re-check reservation state; if expired and stock gone -> refund path.
- Under load: waiting room / token bucket in front, return 429 + retry hint, never hit the DB with 100k writers.

### Principal-level discussion: make F1 more efficient
1. Per-event DB lookup of preferences at 20k/s is wasteful. How would you cache them, and how do you invalidate when a user changes a setting? (TTL + event-driven invalidation.)
2. Fan-out events ("site outage" hitting 50k users) — do you expand in the router or in a separate fan-out stage? Why batching matters.
3. Cost: SQS long polling, batch send/receive (10 msgs), Lambda concurrency limits vs a worker fleet on autoscaling by queue depth.
4. Collapsing: if a charger flaps FAULTED/OK 10 times in a minute, how do you coalesce into one alert? (windowed aggregation, per-charger debounce.)
5. What breaks first at 10x? Where do you put the backpressure?
