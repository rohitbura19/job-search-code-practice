package practice.d20261103.g;

import java.time.Duration;

/** backoff(n) = base * 2^(n-1), capped at one hour (and the shift is capped so it cannot overflow). */
public record RetryPolicy(int maxAttempts, Duration base) {
    public static final Duration MAX_BACKOFF = Duration.ofHours(1);
    public static final RetryPolicy NONE = new RetryPolicy(1, Duration.ZERO);

    public RetryPolicy {
        if (maxAttempts < 1) throw new IllegalArgumentException("maxAttempts");
        if (base.isNegative()) throw new IllegalArgumentException("base");
    }

    /** Delay before the next attempt after `failedAttempts` failures (1-based). */
    public Duration backoff(int failedAttempts) {
        if (base.compareTo(MAX_BACKOFF) >= 0) return MAX_BACKOFF;
        Duration d = base.multipliedBy(1L << Math.min(failedAttempts - 1, 30));
        return d.compareTo(MAX_BACKOFF) > 0 ? MAX_BACKOFF : d;
    }
}
