package practice.d20261029.g;

import java.time.Instant;
import java.util.Set;

/** A temporary claim on seats; valid while now is before expiresAt. */
public record Hold(String holdId, String eventId, Set<String> seats, String userId, Instant expiresAt) {}
