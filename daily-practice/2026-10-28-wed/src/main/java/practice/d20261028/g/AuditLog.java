package practice.d20261028.g;

import java.util.List;

/** Append-only, tamper-evident audit log. Thread-safe. */
public interface AuditLog {
    /**
     * Appends an event: seq = previous seq + 1 (first is 1), prevHash = previous hash (AuditChain.GENESIS for the first),
     * at = the clock's time but never earlier than the previous event's `at`.
     * actor, action and resource must be non-null and non-blank (IllegalArgumentException otherwise).
     */
    AuditEvent append(String actor, String action, String resource);

    /**
     * Matching events by ascending seq, at most q.limit(). Must not scan the whole log when the actor or resource is rare, and
     * must jump straight to afterSeq. limit < 1 -> IllegalArgumentException.
     */
    List<AuditEvent> query(AuditQuery q);

    int size();
}
