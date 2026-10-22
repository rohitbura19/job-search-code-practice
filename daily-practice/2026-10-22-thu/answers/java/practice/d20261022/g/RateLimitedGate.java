package practice.d20261022.g;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 * Sliding-window LOG limiter: a deque of send instants per (user, channel); drop the ones older than the window, then compare
 * the size with the limit. Exact, O(1) amortised per call, memory O(maxPerWindow) per active key.
 * The clock is injected so tests can move time. One lock (synchronized) keeps check-then-record atomic; for real load shard by user.
 * Decisions that are not SEND record nothing, so a throttled or duplicate attempt does not extend its own penalty.
 * Known gap (see README review): entries of idle users are never evicted here; production needs a TTL cache or a Redis key with EXPIRE.
 */
public final class RateLimitedGate implements NotificationGate {
    private final Clock clock;
    private final GateConfig cfg;
    private final Map<String, Deque<Instant>> sends = new HashMap<>();
    private final Map<String, Instant> lastSent = new HashMap<>();

    public RateLimitedGate(Clock clock, GateConfig config) {
        this.clock = clock;
        this.cfg = config;
    }

    @Override
    public synchronized Decision decide(Notification n) {
        Instant now = clock.instant();
        String dedupe = n.userId() + "\u0000" + n.dedupeKey();
        if (n.dedupeKey() != null) {
            Instant prev = lastSent.get(dedupe);
            if (prev != null) {
                if (now.isBefore(prev.plus(cfg.dedupeTtl()))) return Decision.DUPLICATE;
                lastSent.remove(dedupe);
            }
        }
        if (!n.urgent() && inQuietHours(now)) return Decision.QUIET_HOURS;
        Deque<Instant> log = sends.computeIfAbsent(n.userId() + "\u0000" + n.channel(), k -> new ArrayDeque<>());
        Instant cutoff = now.minus(cfg.window());
        while (!log.isEmpty() && !log.peekFirst().isAfter(cutoff)) log.pollFirst();
        if (log.size() >= cfg.maxPerWindow()) return Decision.THROTTLED;
        log.addLast(now);
        if (n.dedupeKey() != null) lastSent.put(dedupe, now);
        return Decision.SEND;
    }

    private boolean inQuietHours(Instant now) {
        LocalTime t = now.atZone(cfg.zone()).toLocalTime();
        LocalTime s = cfg.quietStart(), e = cfg.quietEnd();
        if (s.equals(e)) return false;
        return s.isBefore(e) ? !t.isBefore(s) && t.isBefore(e) : !t.isBefore(s) || t.isBefore(e);
    }
}
