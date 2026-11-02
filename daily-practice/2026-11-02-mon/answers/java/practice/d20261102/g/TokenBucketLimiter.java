package practice.d20261102.g;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Lazy refill: no timer thread, tokens are computed from elapsed time on each check (O(1)).
 * All state changes happen inside ConcurrentHashMap.compute, which is atomic per key, so concurrent checks never
 * over-admit and evictIdle (computeIfPresent returning null) cannot lose an update.
 * lastRefill advances only by whole tokens' worth of time, so fractions of a token are never lost.
 */
public final class TokenBucketLimiter implements RateLimiter {
    private static final class Bucket {
        long tokens, lastRefill;
        Bucket(long tokens, long lastRefill) { this.tokens = tokens; this.lastRefill = lastRefill; }
    }

    private final Clock clock;
    private final int capacity;
    private final long perMs;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public TokenBucketLimiter(Clock clock, int capacity, Duration perToken) {
        if (capacity < 1 || perToken.toMillis() < 1) throw new IllegalArgumentException();
        this.clock = clock;
        this.capacity = capacity;
        this.perMs = perToken.toMillis();
    }

    private void refill(Bucket b, long now) {
        long elapsed = now - b.lastRefill;
        if (elapsed >= perMs) {
            long add = elapsed / perMs;
            b.tokens = Math.min(capacity, b.tokens + add);
            b.lastRefill = b.tokens == capacity ? now : b.lastRefill + add * perMs;
        } else if (b.tokens == capacity) {
            b.lastRefill = Math.max(b.lastRefill, now);
        }
    }

    @Override public Decision check(String key) {
        long now = clock.millis();
        Decision[] out = new Decision[1];
        buckets.compute(key, (k, b) -> {
            if (b == null) b = new Bucket(capacity, now);
            refill(b, now);
            if (b.tokens > 0) {
                b.tokens--;
                out[0] = new Decision(true, (int) b.tokens, Duration.ZERO);
            } else {
                out[0] = new Decision(false, 0, Duration.ofMillis(perMs - (now - b.lastRefill)));
            }
            return b;
        });
        return out[0];
    }

    @Override public int trackedKeys() { return buckets.size(); }

    @Override public void evictIdle() {
        long now = clock.millis();
        for (String key : new ArrayList<>(buckets.keySet())) {
            buckets.computeIfPresent(key, (k, b) -> {
                refill(b, now);
                return b.tokens == capacity ? null : b;
            });
        }
    }
}
