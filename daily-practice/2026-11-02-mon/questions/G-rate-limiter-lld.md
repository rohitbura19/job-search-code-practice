# G. LLD of F1: in-memory rate limiters (sketch 8 min, code 22 min)

> Code: `src/main/java/practice/d20261102/g/` (`RateLimiter` and `Decision` are given; implement `TokenBucketLimiter` and `SlidingWindowLimiter`). Tests: `TokenBucketLimiterTest`, `SlidingWindowLimiterTest` (movable clock, no sleeps; 32-thread race, eviction racing with checks, 10^6 checks within 3 s). Sketch the state per key first in `my-designs/G-classes-sketch.md`. **30 min.**

Read the Javadoc of `RateLimiter`: it is the contract. Hints: keep one small state object per key in a `ConcurrentHashMap`; make each check atomic per key (`compute`), not globally locked; refill lazily from elapsed time (no timer thread); advance `lastRefill` only by whole tokens so fractions are not lost; `evictIdle` must not lose a concurrent check (use `computeIfPresent`).
