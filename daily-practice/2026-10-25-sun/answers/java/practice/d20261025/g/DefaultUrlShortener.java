package practice.d20261025.g;

import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * shorten: validate, then claim a code with store.putIfAbsent. A custom alias fails fast when taken; a generated code (Base62 of the next id)
 * loops to the next id when an alias already owns it, so the counter never hands out a used code. resolve: find, check expiry against the
 * injected clock, count the click only on success. O(1) per call, plus the store.
 */
public class DefaultUrlShortener implements UrlShortener {
    private static final Pattern ALIAS = Pattern.compile("[A-Za-z0-9_-]{3,20}");
    private final UrlStore store;
    private final IdGenerator ids;
    private final Clock clock;

    public DefaultUrlShortener(UrlStore store, IdGenerator ids, Clock clock) {
        this.store = store;
        this.ids = ids;
        this.clock = clock;
    }

    @Override public String shorten(String longUrl, String customAlias, Duration ttl) {
        validateUrl(longUrl);
        if (customAlias != null && !ALIAS.matcher(customAlias).matches()) throw new IllegalArgumentException("bad alias");
        if (ttl != null && (ttl.isZero() || ttl.isNegative())) throw new IllegalArgumentException("ttl must be positive");
        Instant now = clock.instant();
        Instant expires = ttl == null ? null : now.plus(ttl);
        if (customAlias != null) {
            if (!store.putIfAbsent(new ShortUrl(customAlias, longUrl, now, expires))) throw new AliasTakenException(customAlias);
            return customAlias;
        }
        while (true) {
            String code = Base62.encode(ids.next());
            if (store.putIfAbsent(new ShortUrl(code, longUrl, now, expires))) return code;
        }
    }

    @Override public Optional<String> resolve(String code) {
        if (code == null) return Optional.empty();
        Optional<ShortUrl> found = store.find(code);
        if (found.isEmpty() || found.get().isExpiredAt(clock.instant())) return Optional.empty();
        store.incrementClicks(code);
        return Optional.of(found.get().longUrl());
    }

    @Override public long clicks(String code) { return store.clicks(code); }

    private static void validateUrl(String url) {
        if (url == null || url.isBlank() || url.length() > 2048) throw new IllegalArgumentException("bad url");
        try {
            URI u = new URI(url);
            String scheme = u.getScheme();
            if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https")) || u.getHost() == null)
                throw new IllegalArgumentException("only absolute http(s) urls with a host");
        } catch (java.net.URISyntaxException e) {
            throw new IllegalArgumentException("malformed url", e);
        }
    }
}
