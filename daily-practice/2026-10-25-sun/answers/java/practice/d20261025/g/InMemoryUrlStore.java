package practice.d20261025.g;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** ConcurrentHashMap.putIfAbsent is the atomic claim; counters are AtomicLong (LongAdder is better under heavy contention). O(1) per call. */
public class InMemoryUrlStore implements UrlStore {
    private final ConcurrentHashMap<String, ShortUrl> urls = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> clicks = new ConcurrentHashMap<>();

    @Override public boolean putIfAbsent(ShortUrl u) {
        if (urls.putIfAbsent(u.code(), u) != null) return false;
        clicks.putIfAbsent(u.code(), new AtomicLong());
        return true;
    }

    @Override public Optional<ShortUrl> find(String code) { return Optional.ofNullable(urls.get(code)); }

    @Override public long incrementClicks(String code) { return counter(code).incrementAndGet(); }

    @Override public long clicks(String code) { return counter(code).get(); }

    private AtomicLong counter(String code) {
        AtomicLong c = clicks.get(code);
        if (c == null) throw new NoSuchElementException(code);
        return c;
    }
}
