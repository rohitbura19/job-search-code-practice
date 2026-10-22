package practice.d20261012.g;

import java.util.function.LongSupplier;

/** G - one TokenBucket per key, created lazily with the key's Limit. Time comes from the injected clock (nanoseconds). */
public class InMemoryTokenBucketLimiter implements RateLimiter {

    public InMemoryTokenBucketLimiter(LimitResolver resolver, LongSupplier nanoClock) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Decision tryAcquire(String key) {
        throw new UnsupportedOperationException("TODO");
    }
}
