package practice.d20261013.g;

/**
 * Abort when failed / (failed + succeeded) is STRICTLY greater than maxRate, but only once failed + succeeded >= minSample
 * (so that 1 failure out of 3 does not abort at 33%). Exactly at the threshold does not abort.
 */
public class FailureRateAbort implements AbortPolicy {
    public FailureRateAbort(double maxRate, int minSample) {
        // TODO
    }

    @Override public boolean shouldAbort(Stats s) {
        throw new UnsupportedOperationException("TODO");
    }
}
