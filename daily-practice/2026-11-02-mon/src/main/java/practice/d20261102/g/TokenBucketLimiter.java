package practice.d20261102.g;

import java.time.Clock;
import java.time.Duration;

/**
 * Bucket of at most capacity tokens, new keys start full, one token is added every perToken (>= 1 ms) up to capacity.
 * Constructor: capacity < 1 or perToken < 1 ms -> IllegalArgumentException. Use only clock.millis() for time.
 * A clock that moves backwards must not add or remove tokens.
 */
public final class TokenBucketLimiter implements RateLimiter {
    public TokenBucketLimiter(Clock clock, int capacity, Duration perToken) { throw new UnsupportedOperationException("TODO"); }

    @Override public Decision check(String key) { throw new UnsupportedOperationException("TODO"); }
    @Override public int trackedKeys() { throw new UnsupportedOperationException("TODO"); }
    @Override public void evictIdle() { throw new UnsupportedOperationException("TODO"); }
}
