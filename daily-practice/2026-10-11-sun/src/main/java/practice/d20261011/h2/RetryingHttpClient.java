package practice.d20261011.h2;

/** Decorator (composition, not inheritance): retries the delegate. */
public class RetryingHttpClient implements HttpClient {

    /** maxAttempts < 1 throws IllegalArgumentException. */
    public RetryingHttpClient(HttpClient delegate, int maxAttempts) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Call the delegate up to maxAttempts times while it throws a RuntimeException; rethrow the last one when all attempts fail. */
    @Override public String get(String url) {
        throw new UnsupportedOperationException("TODO");
    }
}
