package practice.d20261025.g;

import java.util.Optional;

public class InMemoryUrlStore implements UrlStore {
    @Override public boolean putIfAbsent(ShortUrl u) { throw new UnsupportedOperationException("TODO"); }
    @Override public Optional<ShortUrl> find(String code) { throw new UnsupportedOperationException("TODO"); }
    @Override public long incrementClicks(String code) { throw new UnsupportedOperationException("TODO"); }
    @Override public long clicks(String code) { throw new UnsupportedOperationException("TODO"); }
}
