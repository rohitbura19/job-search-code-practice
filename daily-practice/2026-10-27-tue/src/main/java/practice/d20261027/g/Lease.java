package practice.d20261027.g;

import java.time.Instant;

/** A granted lock. The fencing token grows with every NEW grant on the same resource (renewals keep it). */
public record Lease(String resource, String owner, long fencingToken, Instant expiresAt) {}
