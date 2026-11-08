package practice.d20261108.g;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * One immutable Entry per key swapped inside ConcurrentHashMap.compute, so each transition is atomic per key and keys never
 * block each other (compute locks only the bin). A single expiry field serves both meanings: lease end for IN_PROGRESS and
 * retention end for COMPLETED. Tokens fence stale workers. The clock is injected, so time is testable.
 * begin/complete/abort O(1); purgeExpired O(n).
 */
public class IdempotencyService {
    private enum State { IN_PROGRESS, COMPLETED }
    private record Entry(String fingerprint, State state, long token, Instant expiresAt, StoredResponse response) {}

    private final Clock clock;
    private final Duration lease, retention;
    private final Map<String, Entry> map = new ConcurrentHashMap<>();
    private final AtomicLong tokens = new AtomicLong();

    public IdempotencyService(Clock clock, Duration lease, Duration retention) {
        this.clock = Objects.requireNonNull(clock);
        this.lease = Objects.requireNonNull(lease);
        this.retention = Objects.requireNonNull(retention);
    }

    public Outcome begin(String key, String fingerprint) {
        check(key);
        Objects.requireNonNull(fingerprint);
        Instant now = clock.instant();
        Outcome[] result = new Outcome[1];
        map.compute(key, (k, e) -> {
            if (e == null || !now.isBefore(e.expiresAt())) {
                long token = tokens.incrementAndGet();
                result[0] = new Outcome.Started(token);
                return new Entry(fingerprint, State.IN_PROGRESS, token, now.plus(lease), null);
            }
            if (!e.fingerprint().equals(fingerprint)) result[0] = new Outcome.Mismatch();
            else result[0] = e.state() == State.COMPLETED ? new Outcome.Replay(e.response()) : new Outcome.InProgress();
            return e;
        });
        return result[0];
    }

    public boolean complete(String key, long token, StoredResponse response) {
        check(key);
        Objects.requireNonNull(response);
        Instant now = clock.instant();
        boolean[] ok = new boolean[1];
        map.computeIfPresent(key, (k, e) -> {
            if (e.state() != State.IN_PROGRESS || e.token() != token) return e;
            ok[0] = true;
            return new Entry(e.fingerprint(), State.COMPLETED, token, now.plus(retention), response);
        });
        return ok[0];
    }

    public boolean abort(String key, long token) {
        check(key);
        boolean[] ok = new boolean[1];
        map.computeIfPresent(key, (k, e) -> {
            if (e.state() == State.IN_PROGRESS && e.token() == token) { ok[0] = true; return null; }
            return e;
        });
        return ok[0];
    }

    public int purgeExpired() {
        Instant now = clock.instant();
        int before = map.size();
        map.entrySet().removeIf(en -> !now.isBefore(en.getValue().expiresAt()));
        return before - map.size();
    }

    public int size() { return map.size(); }

    private static void check(String key) {
        Objects.requireNonNull(key);
        if (key.isBlank()) throw new IllegalArgumentException("blank key");
    }
}
