> Code: `g/TokenBucket.java`, `g/InMemoryTokenBucketLimiter.java`, `g/FailOpenLimiter.java`, `g/ChainedLimiter.java` (`Decision`, `Limit`, `RateLimiter`, `LimitResolver` are given). Test: `RateLimiterTest` (fake clock, no sleeps; includes a 50-thread race). The servlet `RateLimitFilter`, the Redis implementation, eviction and the discussion are paper: sketch first in `my-designs/G-classes-sketch.md`.

## G) LLD of the same question — Rate limiter library (25 min)

Design the in-process object model for the **Rate Limiter** (F1) used by a gateway filter. Requirements: pluggable algorithms (token bucket now, sliding window later), per-key + per-plan limits, in-memory and Redis-backed storage, thread-safe, testable time, no memory leak for millions of one-off keys.

Write the interfaces and classes, the `allow()` flow, and the filter integration. Then discuss concurrency, patterns and testing.
