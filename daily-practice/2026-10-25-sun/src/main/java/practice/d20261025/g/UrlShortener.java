package practice.d20261025.g;

import java.time.Duration;
import java.util.Optional;

public interface UrlShortener {
    /**
     * @param customAlias null for a generated code; otherwise 3..20 chars of [A-Za-z0-9_-], else IllegalArgumentException
     * @param ttl         null = never expires; zero or negative -> IllegalArgumentException
     * @return the short code. longUrl must be an absolute http or https url with a host, at most 2048 chars, else IllegalArgumentException
     * @throws AliasTakenException if customAlias already exists (a generated code never collides with a stored alias)
     */
    String shorten(String longUrl, String customAlias, Duration ttl);

    /** The long url, or empty if the code is unknown or expired (expired exactly at expiresAt). Counts one click only when it returns a url. */
    Optional<String> resolve(String code);

    /** Unknown code -> java.util.NoSuchElementException. Expired codes keep their count. */
    long clicks(String code);
}
