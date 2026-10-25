package practice.d20261025.g;

import java.time.Instant;

/** expiresAt == null means "never expires". */
public record ShortUrl(String code, String longUrl, Instant createdAt, Instant expiresAt) {
    public boolean isExpiredAt(Instant now) { return expiresAt != null && !now.isBefore(expiresAt); }
}
