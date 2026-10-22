package practice.d20261011.g;

/** Sends with retry. Tests: DispatchingTest. */
public class Dispatcher {

    public Dispatcher(RetryPolicy policy, Sleeper sleeper) {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Send; on a RuntimeException ask policy.shouldRetry(attemptsSoFar, e) (attemptsSoFar = 1 after the first failure); if yes,
     * sleep policy.delay(attemptsSoFar) and try again; if no, rethrow that exception. Returns the successful SendResult.
     */
    public SendResult dispatchWithRetry(ChannelSender sender, Recipient r, RenderedMessage m) {
        throw new UnsupportedOperationException("TODO");
    }
}
