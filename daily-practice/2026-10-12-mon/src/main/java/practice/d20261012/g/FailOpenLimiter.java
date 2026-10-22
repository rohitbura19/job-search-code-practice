package practice.d20261012.g;

/** G - Decorator: if the wrapped limiter throws a RuntimeException (store down), ALLOW: Decision(true, -1, ZERO). */
public class FailOpenLimiter implements RateLimiter {

    public FailOpenLimiter(RateLimiter delegate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Decision tryAcquire(String key) {
        throw new UnsupportedOperationException("TODO");
    }
}
