package practice.d20261102.g;

import java.time.Clock;
import java.time.Duration;

/**
 * At most limit admitted checks in any window: a check at time t counts the admitted checks with timestamp > t - window.
 * Denied checks are not recorded. Constructor: limit < 1 or window < 1 ms -> IllegalArgumentException. Use only clock.millis().
 */
public final class SlidingWindowLimiter implements RateLimiter {
    public SlidingWindowLimiter(Clock clock, int limit, Duration window) { throw new UnsupportedOperationException("TODO"); }

    @Override public Decision check(String key) { throw new UnsupportedOperationException("TODO"); }
    @Override public int trackedKeys() { throw new UnsupportedOperationException("TODO"); }
    @Override public void evictIdle() { throw new UnsupportedOperationException("TODO"); }
}
