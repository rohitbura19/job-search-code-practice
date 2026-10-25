package practice.d20261025.g;

import java.time.Clock;
import java.time.Duration;
import java.util.Optional;

public class DefaultUrlShortener implements UrlShortener {
    public DefaultUrlShortener(UrlStore store, IdGenerator ids, Clock clock) {}

    @Override public String shorten(String longUrl, String customAlias, Duration ttl) { throw new UnsupportedOperationException("TODO"); }
    @Override public Optional<String> resolve(String code) { throw new UnsupportedOperationException("TODO"); }
    @Override public long clicks(String code) { throw new UnsupportedOperationException("TODO"); }
}
