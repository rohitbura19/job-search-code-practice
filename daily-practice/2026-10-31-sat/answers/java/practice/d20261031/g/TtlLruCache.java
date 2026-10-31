package practice.d20261031.g;

import java.time.Clock;
import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

/**
 * One monitor guards the access-ordered LinkedHashMap (LRU for free), the in-flight map and the counters; critical sections are tiny
 * and the loader never runs inside them. Single flight: the first misser installs a CompletableFuture in `inflight` and becomes the
 * owner; later missers join that future. On completion the owner stores the value only if `inflight.get(key) == its future`, which is
 * exactly what invalidate() breaks by removing the entry (so a stale in-flight load never resurrects an invalidated key).
 * get O(1), purgeExpired O(n). For high contention: stripe the monitor by key hash (N independent caches), as Guava/Caffeine do.
 */
public final class TtlLruCache<K, V> implements LoadingCache<K, V> {
    private record Entry<V>(Optional<V> value, long expiresAt) {}

    private final long ttlMs, negTtlMs;
    private final Function<K, Optional<V>> loader;
    private final Clock clock;
    private final Object lock = new Object();
    private final Map<K, Entry<V>> map;
    private final Map<K, CompletableFuture<Optional<V>>> inflight = new HashMap<>();
    private long hits, misses, loads, failures, evictions, expirations;

    public TtlLruCache(int maxEntries, Duration ttl, Duration negativeTtl, Function<K, Optional<V>> loader, Clock clock) {
        if (maxEntries < 1) throw new IllegalArgumentException("maxEntries");
        this.ttlMs = ttl.toMillis();
        this.negTtlMs = negativeTtl.toMillis();
        this.loader = loader;
        this.clock = clock;
        this.map = new LinkedHashMap<>(16, 0.75f, true) {
            @Override protected boolean removeEldestEntry(Map.Entry<K, Entry<V>> eldest) {
                boolean evict = size() > maxEntries;
                if (evict) evictions++;
                return evict;
            }
        };
    }

    @Override public Optional<V> get(K key) {
        CompletableFuture<Optional<V>> flight;
        boolean owner = false;
        synchronized (lock) {
            Entry<V> e = map.get(key);                       // access-ordered: marks it most recently used
            if (e != null) {
                if (clock.millis() < e.expiresAt()) { hits++; return e.value(); }
                map.remove(key);
                expirations++;
            }
            misses++;
            flight = inflight.get(key);
            if (flight == null) { flight = new CompletableFuture<>(); inflight.put(key, flight); owner = true; }
        }
        if (!owner) {
            try { return flight.join(); }
            catch (CompletionException ex) { throw new CacheLoadException(key, ex.getCause()); }
        }
        try {
            Optional<V> v = loader.apply(key);
            synchronized (lock) {
                loads++;
                if (inflight.get(key) == flight) {
                    inflight.remove(key);
                    map.put(key, new Entry<>(v, clock.millis() + (v.isPresent() ? ttlMs : negTtlMs)));
                }
            }
            flight.complete(v);
            return v;
        } catch (RuntimeException ex) {
            synchronized (lock) { failures++; inflight.remove(key, flight); }
            flight.completeExceptionally(ex);
            throw new CacheLoadException(key, ex);
        }
    }

    @Override public void invalidate(K key) {
        synchronized (lock) { map.remove(key); inflight.remove(key); }
    }

    @Override public int purgeExpired() {
        synchronized (lock) {
            long now = clock.millis();
            int n = 0;
            for (Iterator<Entry<V>> it = map.values().iterator(); it.hasNext(); ) {
                if (now >= it.next().expiresAt()) { it.remove(); n++; }
            }
            expirations += n;
            return n;
        }
    }

    @Override public int size() { synchronized (lock) { return map.size(); } }

    @Override public CacheStats stats() {
        synchronized (lock) { return new CacheStats(hits, misses, loads, failures, evictions, expirations); }
    }
}
