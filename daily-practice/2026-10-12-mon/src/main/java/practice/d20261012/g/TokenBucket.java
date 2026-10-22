package practice.d20261012.g;

/**
 * G - one bucket per key. Test: RateLimiterTest.
 * Fields you will want: capacity, refillPerNano (= refillPerSecond / 1e9), tokens (double), lastNanos.
 */
public class TokenBucket {

    /** Starts full. */
    TokenBucket(Limit limit, long nowNanos) {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Refill by elapsed time (never above capacity), then take one token if there is one.
     * Allowed: Decision(true, whole tokens left, ZERO). Denied: Decision(false, 0, time until one token exists).
     * Must be thread-safe: refill + decrement is one atomic step.
     */
    Decision take(long nowNanos) {
        throw new UnsupportedOperationException("TODO");
    }
}
