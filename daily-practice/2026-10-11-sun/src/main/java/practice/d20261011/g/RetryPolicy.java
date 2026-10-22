package practice.d20261011.g;

import java.time.Duration;

/** Exponential backoff with jitter. See questions/G-notification-lld.md for the contract. Tests: DispatchingTest. */
public class RetryPolicy {

    /** 200 ms * 2^attempt * (0.5 + random), random in [0, 1). */
    public Duration delay(int attempt) {
        throw new UnsupportedOperationException("TODO");
    }

    /** attempt = number of attempts already made. true while attempt < 5 and t is not a PermanentFailure. */
    public boolean shouldRetry(int attempt, Throwable t) {
        throw new UnsupportedOperationException("TODO");
    }
}
