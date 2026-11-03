package practice.d20261103.g;

import java.time.Duration;

public record RetryPolicy(int maxAttempts, Duration base) {
    public static final Duration MAX_BACKOFF = Duration.ofHours(1);
    public static final RetryPolicy NONE = new RetryPolicy(1, Duration.ZERO);

    /** TODO: reject maxAttempts < 1 and a negative base with IllegalArgumentException (compact constructor). The stub keeps the body empty: add the checks. */
    public RetryPolicy {
    }

    /** Delay before the next attempt after `failedAttempts` failures (1-based): base * 2^(failedAttempts-1), never more than MAX_BACKOFF, no overflow for large inputs. */
    public Duration backoff(int failedAttempts) { throw new UnsupportedOperationException("TODO"); }
}
