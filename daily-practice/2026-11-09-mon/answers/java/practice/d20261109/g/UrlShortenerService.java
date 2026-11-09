package practice.d20261109.g;

import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.regex.Pattern;

/**
 * ConcurrentHashMap<code, Entry>. The atomic "is it free?" decision happens inside compute(), so two threads cannot
 * both take the same code. Clicks are a LongAdder per entry (no lock on the hot read path). O(1) per op, purge O(n).
 */
public class UrlShortenerService {
    private static final Pattern ALIAS = Pattern.compile("[A-Za-z0-9_-]{3,30}");

    private record Entry(ShortLink link, LongAdder clicks) {}

    private final IdGenerator ids;
    private final Clock clock;
    private final ConcurrentHashMap<String, Entry> links = new ConcurrentHashMap<>();

    public UrlShortenerService(IdGenerator ids, Clock clock) { this.ids = ids; this.clock = clock; }

    public ShortLink shorten(String longUrl, String customAlias, Duration ttl) {
        validateUrl(longUrl);
        if (ttl != null && (ttl.isNegative() || ttl.isZero())) throw new IllegalArgumentException("ttl must be positive");
        if (customAlias != null && !ALIAS.matcher(customAlias).matches()) throw new IllegalArgumentException("bad alias");
        Instant now = clock.instant();
        Instant expires = ttl == null ? null : now.plus(ttl);
        if (customAlias != null) {
            ShortLink link = new ShortLink(customAlias, longUrl, now, expires);
            if (!tryInsert(link, now)) throw new AliasTakenException(customAlias);
            return link;
        }
        while (true) {
            ShortLink link = new ShortLink(Base62.encode(ids.nextId()), longUrl, now, expires);
            if (tryInsert(link, now)) return link;
        }
    }

    private boolean tryInsert(ShortLink link, Instant now) {
        boolean[] inserted = {false};
        links.compute(link.code(), (k, old) -> {
            if (old != null && !old.link().isExpiredAt(now)) return old;
            inserted[0] = true;
            return new Entry(link, new LongAdder());
        });
        return inserted[0];
    }

    public Optional<String> resolve(String code) {
        Entry e = links.get(code);
        if (e == null || e.link().isExpiredAt(clock.instant())) return Optional.empty();
        e.clicks().increment();
        return Optional.of(e.link().longUrl());
    }

    public long clicks(String code) {
        Entry e = links.get(code);
        return e == null ? 0 : e.clicks().sum();
    }

    public int purgeExpired() {
        Instant now = clock.instant();
        int[] removed = {0};
        links.entrySet().removeIf(en -> en.getValue().link().isExpiredAt(now) && ++removed[0] > 0);
        return removed[0];
    }

    private static void validateUrl(String s) {
        try {
            URI u = URI.create(s);
            if (!u.isAbsolute() || u.getHost() == null || !("http".equals(u.getScheme()) || "https".equals(u.getScheme())))
                throw new IllegalArgumentException("not an http(s) url: " + s);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("invalid url: " + s);
        }
    }
}
