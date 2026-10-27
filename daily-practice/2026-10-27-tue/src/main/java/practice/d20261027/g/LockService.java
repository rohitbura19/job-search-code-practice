package practice.d20261027.g;

import java.time.Duration;
import java.util.Optional;

/**
 * Lease-based mutual exclusion with fencing tokens.
 * A lease is live while clock.instant() is strictly before expiresAt. Null arguments -> NullPointerException;
 * a zero or negative ttl -> IllegalArgumentException.
 */
public interface LockService {
    /** Grants a new lease if the resource is free or its lease has expired (token = previous token + 1, first token is 1). Empty if a live lease exists, even the caller's own. */
    Optional<Lease> tryAcquire(String resource, String owner, Duration ttl);

    /** Extends a live lease to now + ttl, keeping the token. Empty if the lease expired, was released, or was superseded by another grant. */
    Optional<Lease> renew(Lease lease, Duration ttl);

    /** Releases the lease. False if it is not the current live lease (expired, superseded, already released). The next grant still gets a higher token. */
    boolean release(Lease lease);

    /** The live lease on the resource, if any. */
    Optional<Lease> current(String resource);
}
