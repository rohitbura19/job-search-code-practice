package practice.d20261102.g;

/** Given: the contract. */
public interface RateLimiter {
    /**
     * Try to consume one permit for key. Thread-safe; two threads checking the same key never over-admit.
     * Allowed: remaining = permits left after this call, retryAfter = Duration.ZERO.
     * Denied: remaining = 0, retryAfter = how long until a permit could be available (> 0, millisecond precision).
     */
    Decision check(String key);

    /** Number of keys currently holding state. */
    int trackedKeys();

    /** Drop every key whose state is identical to a brand-new key's (full bucket / empty window). Must not lose a concurrent check. */
    void evictIdle();
}
