package practice.d20261102.g;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding window log: exact, but O(limit) memory per key (the token bucket is O(1)).
 * Per key a deque of admitted timestamps; each check drops the expired head (amortised O(1)).
 * Atomic per key through ConcurrentHashMap.compute; evictIdle removes keys whose log is empty after pruning.
 */
public final class SlidingWindowLimiter implements RateLimiter {
    private final Clock clock;
    private final int limit;
    private final long windowMs;
    private final ConcurrentHashMap<String, ArrayDeque<Long>> logs = new ConcurrentHashMap<>();

    public SlidingWindowLimiter(Clock clock, int limit, Duration window) {
        if (limit < 1 || window.toMillis() < 1) throw new IllegalArgumentException();
        this.clock = clock;
        this.limit = limit;
        this.windowMs = window.toMillis();
    }

    private void prune(ArrayDeque<Long> log, long now) {
        while (!log.isEmpty() && log.peekFirst() <= now - windowMs) log.pollFirst();
    }

    @Override public Decision check(String key) {
        long now = clock.millis();
        Decision[] out = new Decision[1];
        logs.compute(key, (k, log) -> {
            if (log == null) log = new ArrayDeque<>();
            prune(log, now);
            if (log.size() < limit) {
                log.addLast(now);
                out[0] = new Decision(true, limit - log.size(), Duration.ZERO);
            } else {
                out[0] = new Decision(false, 0, Duration.ofMillis(log.peekFirst() + windowMs - now));
            }
            return log;
        });
        return out[0];
    }

    @Override public int trackedKeys() { return logs.size(); }

    @Override public void evictIdle() {
        long now = clock.millis();
        for (String key : new ArrayList<>(logs.keySet())) {
            logs.computeIfPresent(key, (k, log) -> {
                prune(log, now);
                return log.isEmpty() ? null : log;
            });
        }
    }
}
