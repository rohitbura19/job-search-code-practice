package practice.d20261109.g;

import java.time.Instant;

/** expiresAt is null for links that never expire; it is exclusive (expired when now >= expiresAt). */
public record ShortLink(String code, String longUrl, Instant createdAt, Instant expiresAt) {
    public boolean isExpiredAt(Instant now) { return expiresAt != null && !now.isBefore(expiresAt); }
}
