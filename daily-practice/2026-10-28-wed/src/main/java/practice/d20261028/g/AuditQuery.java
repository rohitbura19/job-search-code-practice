package practice.d20261028.g;

import java.time.Instant;

/**
 * actor / resource: null = any. from inclusive, to exclusive, null = open. Only events with seq > afterSeq (keyset paging:
 * pass the last seq of the previous page). limit must be >= 1.
 */
public record AuditQuery(String actor, String resource, Instant from, Instant to, long afterSeq, int limit) {
    public static AuditQuery first(int limit) { return new AuditQuery(null, null, null, null, 0, limit); }
    public AuditQuery byActor(String a) { return new AuditQuery(a, resource, from, to, afterSeq, limit); }
    public AuditQuery onResource(String r) { return new AuditQuery(actor, r, from, to, afterSeq, limit); }
    public AuditQuery between(Instant f, Instant t) { return new AuditQuery(actor, resource, f, t, afterSeq, limit); }
    public AuditQuery after(long seq) { return new AuditQuery(actor, resource, from, to, seq, limit); }
}
