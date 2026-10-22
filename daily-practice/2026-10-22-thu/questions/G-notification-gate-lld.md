> Code: `g/RateLimitedGate.java` (the interface `NotificationGate`, the records `Notification`, `GateConfig` and the enums are given). Test: `RateLimitedGateTest` (uses a movable `Clock`). Sketch classes first in `my-designs/G-classes-sketch.md`. **40 min.**

# G. LLD of F1: the in-process `NotificationGate`

Implement `decide(Notification)`; the result is `SEND`, `DUPLICATE`, `QUIET_HOURS` or `THROTTLED`. Check order: (1) duplicate, (2) quiet hours, (3) rate limit. ONLY a `SEND` changes state: a throttled, held or duplicate attempt must not consume quota nor refresh the dedupe timestamp.

Rules: sliding window of length `window` per `(userId, channel)`, at most `maxPerWindow` sends (a send exactly one window old no longer counts). Dedupe applies when `dedupeKey != null`, per user, for `dedupeTtl` after the SEND (exactly at the TTL it is allowed again). Quiet hours are `[quietStart, quietEnd)` in `cfg.zone()`, may wrap midnight; `urgent` skips them. Use the injected `Clock` only (never `Instant.now()`); the class must be thread-safe.

Say out loud: why a deque of timestamps is exact while a fixed window counter allows 2N at a boundary; memory per user; the per-key lock design (here one `synchronized`: contention?); how the same logic maps to Redis (sorted set + `ZREMRANGEBYSCORE`, or a Lua script for atomicity); how to evict idle users; DST: what happens at a 02:30 that does not exist.
