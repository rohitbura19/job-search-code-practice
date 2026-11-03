package practice.d20261014.h2;

/**
 * Decorator. Calls the inner notifier up to maxAttempts times while it throws a RuntimeException;
 * after the last failed attempt rethrow that last exception. maxAttempts below 1 -> IllegalArgumentException
 * (thrown by the constructor).
 */
public final class RetryingNotifier implements Notifier {
    public RetryingNotifier(Notifier inner, int maxAttempts) { throw new UnsupportedOperationException("TODO"); }

    @Override public void send(String message) { throw new UnsupportedOperationException("TODO"); }
}
