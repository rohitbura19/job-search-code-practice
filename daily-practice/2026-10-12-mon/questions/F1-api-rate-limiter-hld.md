> Write your answer in `my-designs/F1-api-rate-limiter.md` (15 min, then say it out loud).

## F) HLD — two small features of a big app (30 min, about 15 each)

Use this frame: requirements (functional, non-functional, scale numbers) -> API -> data model -> high-level diagram -> deep dive on the one hard part -> failure modes -> metrics.

### F1. API rate limiter for a public platform (per-API-key and per-IP limits)
A gateway fronts 200 microservices. Plans: Free 100 req/min, Pro 1,000 req/min, burst allowed. 1M req/sec at peak across 20 gateway nodes. Limiter adds < 2 ms p99. Must return `429` with `Retry-After`. Limits can be changed live per customer. Fail open or closed when the limiter store is down?

Think about: algorithm (fixed window, sliding window log/counter, token bucket, leaky bucket), where the counters live (local vs Redis vs DynamoDB), atomicity across gateway nodes, hot keys, clock skew, config distribution, multi-region.
