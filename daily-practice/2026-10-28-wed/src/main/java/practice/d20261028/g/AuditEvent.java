package practice.d20261028.g;

import java.time.Instant;

/** One immutable entry of the log. hash covers the fields before it plus prevHash; see AuditChain.hash. */
public record AuditEvent(long seq, String actor, String action, String resource, Instant at, String prevHash, String hash) {}
