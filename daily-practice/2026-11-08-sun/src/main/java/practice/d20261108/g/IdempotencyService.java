package practice.d20261108.g;

import java.time.Clock;
import java.time.Duration;

/**
 * In-memory core of an idempotency-key store (the LLD of F1). Thread safe: every transition of one key is atomic.
 *
 * Rules (now = clock.instant()):
 *  - begin(key, fp) with no record, or a record whose expiry <= now (expired lease of an IN_PROGRESS record, or an expired
 *    COMPLETED record) -> new IN_PROGRESS record with a fresh increasing token, lease until now + lease, return Started(token).
 *  - live record with another fingerprint -> Mismatch (never touches the record).
 *  - live IN_PROGRESS, same fingerprint -> InProgress. live COMPLETED, same fingerprint -> Replay(saved response).
 *  - complete(key, token, response): only if the record is IN_PROGRESS with exactly this token (a worker whose lease was
 *    taken over has a stale token and gets false). The record becomes COMPLETED and is kept until now + retention.
 *  - abort(key, token): same token check; removes the record so a retry can start immediately. Returns whether it removed.
 *  - purgeExpired(): removes expired records, returns how many. size(): number of stored records (expired ones included).
 *  - null key/fingerprint -> NullPointerException; blank key -> IllegalArgumentException.
 */
public class IdempotencyService {
    public IdempotencyService(Clock clock, Duration lease, Duration retention) { throw new UnsupportedOperationException("TODO"); }

    public Outcome begin(String key, String fingerprint) { throw new UnsupportedOperationException("TODO"); }
    public boolean complete(String key, long token, StoredResponse response) { throw new UnsupportedOperationException("TODO"); }
    public boolean abort(String key, long token) { throw new UnsupportedOperationException("TODO"); }
    public int purgeExpired() { throw new UnsupportedOperationException("TODO"); }
    public int size() { throw new UnsupportedOperationException("TODO"); }
}
