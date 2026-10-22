# Answers: G-rate-limiter-lld

(Spoilers. Open only after your attempt.)

**Reference**
```java
public record Decision(boolean allowed, long remaining, Duration retryAfter) {}
public record Limit(long capacity, double refillPerSecond) {}

public interface RateLimiter { Decision tryAcquire(String key); }          // Strategy

public interface LimitResolver { Limit resolve(String key); }              // plan lookup, hot-reloadable

public class TokenBucket {                                                // one bucket per key
    private final long capacity; private final double refillPerNano;
    private double tokens; private long lastNanos;
    TokenBucket(Limit l, long nowNanos) {
        capacity = l.capacity(); refillPerNano = l.refillPerSecond() / 1_000_000_000.0;
        tokens = capacity; lastNanos = nowNanos;
    }
    synchronized Decision take(long nowNanos) {
        tokens = Math.min(capacity, tokens + (nowNanos - lastNanos) * refillPerNano);
        lastNanos = nowNanos;
        if (tokens >= 1) { tokens -= 1; return new Decision(true, (long) tokens, Duration.ZERO); }
        long waitNanos = (long) ((1 - tokens) / refillPerNano);
        return new Decision(false, 0, Duration.ofNanos(waitNanos));
    }
}

public class InMemoryTokenBucketLimiter implements RateLimiter {
    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
    private final LimitResolver resolver; private final LongSupplier nanoClock;
    public InMemoryTokenBucketLimiter(LimitResolver r, LongSupplier clock) { resolver = r; nanoClock = clock; }

    public Decision tryAcquire(String key) {
        long now = nanoClock.getAsLong();
        return buckets.computeIfAbsent(key, k -> new TokenBucket(resolver.resolve(k), now)).take(now);
    }
}

public class RedisTokenBucketLimiter implements RateLimiter {            // same interface, Lua script inside
    public Decision tryAcquire(String key) { /* EVALSHA script, parse [allowed, remaining, retryAfterMs] */ return null; }
}

public class RateLimitFilter implements Filter {                          // integration point
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain ch) throws IOException, ServletException {
        String key = keyExtractor.extract((HttpServletRequest) rq);       // apiKey, else IP
        Decision d = limiter.tryAcquire(key);
        HttpServletResponse res = (HttpServletResponse) rs;
        res.setHeader("X-RateLimit-Remaining", String.valueOf(d.remaining()));
        if (!d.allowed()) {
            res.setStatus(429); res.setHeader("Retry-After", String.valueOf(d.retryAfter().toSeconds() + 1)); return;
        }
        ch.doFilter(rq, rs);
    }
}
```
Patterns: Strategy (`RateLimiter` algorithms), Factory (build limiter by config), Decorator (metrics/fail-open wrapper around any `RateLimiter`), Chain (multiple limiters: per-IP then per-key then per-route, all must pass).

Concurrency points: `computeIfAbsent` gives one bucket per key atomically; the bucket's `synchronized` keeps refill + decrement atomic; contention is per key only. Hot key alternative: `AtomicLong` + CAS loop, or a striped design. Use `System.nanoTime()` (monotonic), never wall clock, for refill math.

Memory: add eviction, e.g. Caffeine `expireAfterAccess` (a bucket that has been idle long enough to be fully refilled is equivalent to a new one, so dropping it is lossless), bounded by size.

Test plan: inject a fake `LongSupplier` clock, no sleeps; test burst then deny, refill after N seconds, retry-after value, per-key isolation; a concurrency test with 50 threads firing 10,000 calls against capacity 1,000 and asserting exactly 1,000 allowed (zero refill); contract test shared by in-memory and Redis implementations (Testcontainers).

Principal-level changes to discuss: decorator for fail-open with a circuit breaker; limits change live (resolver returns the latest, bucket compares capacity and adjusts); separate "check" and "commit" for multi-limiter chains to avoid consuming tokens from limiter 1 when limiter 2 rejects; expose the algorithm as a sealed interface plus config for safe rollout (shadow mode that logs but never blocks).

In the practice kit, `FailOpenLimiter` (Decorator: delegate throws -> `Decision(true, -1, ZERO)`) and `ChainedLimiter` (first denial wins, otherwise the smallest `remaining`) are small extras; the naive chain consumes a token from limiter 1 even when limiter 2 then rejects, which is exactly the "check and commit" point above.
