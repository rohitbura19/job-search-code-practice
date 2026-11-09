package practice.d20261109.g;

import java.time.Clock;
import java.time.Duration;
import java.util.Optional;

/**
 * URL shortener core. Thread-safe. Rules:
 *  - longUrl must be an absolute http/https URL with a host, else IllegalArgumentException. Same URL twice gives two codes.
 *  - customAlias (nullable) must match [A-Za-z0-9_-]{3,30}, else IllegalArgumentException. If a LIVE link already owns it: AliasTakenException.
 *    An expired link's alias can be re-used (the old link is replaced).
 *  - Without an alias the code is Base62.encode(ids.nextId()); if that code is already used (e.g. by a custom alias), take the next id.
 *  - ttl == null: never expires; otherwise it must be positive (IllegalArgumentException) and expiresAt = now + ttl (clock-based).
 *  - resolve(code): the long URL, or empty if the code is unknown or expired. Each successful resolve counts one click.
 *  - clicks(code): clicks so far (0 for unknown codes). Must be exact under concurrent resolves.
 *  - purgeExpired(): removes expired links, returns how many.
 */
public class UrlShortenerService {
    public UrlShortenerService(IdGenerator ids, Clock clock) { throw new UnsupportedOperationException("TODO"); }

    public ShortLink shorten(String longUrl, String customAlias, Duration ttl) { throw new UnsupportedOperationException("TODO"); }

    public Optional<String> resolve(String code) { throw new UnsupportedOperationException("TODO"); }

    public long clicks(String code) { throw new UnsupportedOperationException("TODO"); }

    public int purgeExpired() { throw new UnsupportedOperationException("TODO"); }
}
