# F1. HLD: distributed rate limiter for an API gateway (12 min out loud + 5 min self-review)

> No code. Write your answer in `my-designs/F1-rate-limiter.md`. No tests.

Design a rate-limiting service used by 200 gateway nodes. Limits are per API key and per endpoint class (for example 100 requests/second sustained, burst 200), configurable per customer plan. Peak 1M checks/s overall; the check adds < 2 ms p99 to a request.

Cover: algorithms and trade-offs (fixed window, sliding log, sliding counter, token bucket), where state lives (local counters vs Redis, Lua for atomicity), sharding of hot keys, what happens when the store is down (fail open or closed, per plan), response headers (`429`, `Retry-After`, `X-RateLimit-*`), clock skew, config propagation, abuse by many keys from one IP, and how you test and observe it.
