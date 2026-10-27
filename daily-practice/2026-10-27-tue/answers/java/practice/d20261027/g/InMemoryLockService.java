package practice.d20261027.g;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * One slot per resource (counter that never goes down + the current lease). ConcurrentHashMap.compute runs the whole
 * check-and-set atomically per key, so no global lock is needed. Expiry is evaluated lazily on access (no sweeper thread).
 * The fencing token is what protects the downstream resource from a paused client whose lease expired: the resource rejects
 * any write carrying a token lower than the highest it has seen. All operations O(1); memory O(resources).
 */
public class InMemoryLockService implements LockService {
    private static final class Slot {
        long counter;
        volatile Lease lease;
    }

    private final Clock clock;
    private final Map<String, Slot> slots = new ConcurrentHashMap<>();

    public InMemoryLockService(Clock clock) { this.clock = Objects.requireNonNull(clock); }

    private static void check(Duration ttl) {
        Objects.requireNonNull(ttl);
        if (ttl.isZero() || ttl.isNegative()) throw new IllegalArgumentException("ttl must be positive");
    }

    private boolean live(Lease l, Instant now) { return l != null && now.isBefore(l.expiresAt()); }

    @Override public Optional<Lease> tryAcquire(String resource, String owner, Duration ttl) {
        Objects.requireNonNull(resource);
        Objects.requireNonNull(owner);
        check(ttl);
        AtomicReference<Lease> granted = new AtomicReference<>();
        slots.compute(resource, (k, s) -> {
            if (s == null) s = new Slot();
            Instant now = clock.instant();
            if (!live(s.lease, now)) {
                s.lease = new Lease(resource, owner, ++s.counter, now.plus(ttl));
                granted.set(s.lease);
            }
            return s;
        });
        return Optional.ofNullable(granted.get());
    }

    @Override public Optional<Lease> renew(Lease lease, Duration ttl) {
        Objects.requireNonNull(lease);
        check(ttl);
        AtomicReference<Lease> renewed = new AtomicReference<>();
        slots.computeIfPresent(lease.resource(), (k, s) -> {
            Instant now = clock.instant();
            if (live(s.lease, now) && s.lease.fencingToken() == lease.fencingToken() && s.lease.owner().equals(lease.owner())) {
                s.lease = new Lease(k, lease.owner(), lease.fencingToken(), now.plus(ttl));
                renewed.set(s.lease);
            }
            return s;
        });
        return Optional.ofNullable(renewed.get());
    }

    @Override public boolean release(Lease lease) {
        Objects.requireNonNull(lease);
        AtomicBoolean released = new AtomicBoolean();
        slots.computeIfPresent(lease.resource(), (k, s) -> {
            if (live(s.lease, clock.instant()) && s.lease.fencingToken() == lease.fencingToken() && s.lease.owner().equals(lease.owner())) {
                s.lease = null;
                released.set(true);
            }
            return s;
        });
        return released.get();
    }

    @Override public Optional<Lease> current(String resource) {
        Objects.requireNonNull(resource);
        Slot s = slots.get(resource);
        if (s == null) return Optional.empty();
        Lease l = s.lease;
        return live(l, clock.instant()) ? Optional.of(l) : Optional.empty();
    }
}
