package practice.d20261012.g;

import java.util.List;

/**
 * G - Chain: every limiter must allow. Ask them in list order and return the FIRST denial immediately (the rest are
 * not asked). If all allow: Decision(true, smallest remaining among them, ZERO).
 */
public class ChainedLimiter implements RateLimiter {

    public ChainedLimiter(List<RateLimiter> limiters) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Decision tryAcquire(String key) {
        throw new UnsupportedOperationException("TODO");
    }
}
