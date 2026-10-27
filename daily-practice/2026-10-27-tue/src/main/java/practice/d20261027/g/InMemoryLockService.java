package practice.d20261027.g;

import java.time.Clock;
import java.time.Duration;
import java.util.Optional;

public class InMemoryLockService implements LockService {
    private final Clock clock;

    public InMemoryLockService(Clock clock) { this.clock = clock; }

    @Override public Optional<Lease> tryAcquire(String resource, String owner, Duration ttl) { throw new UnsupportedOperationException("TODO"); }

    @Override public Optional<Lease> renew(Lease lease, Duration ttl) { throw new UnsupportedOperationException("TODO"); }

    @Override public boolean release(Lease lease) { throw new UnsupportedOperationException("TODO"); }

    @Override public Optional<Lease> current(String resource) { throw new UnsupportedOperationException("TODO"); }
}
