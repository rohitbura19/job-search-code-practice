package practice.d20261031.g;

import java.time.Clock;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

/**
 * Rules:
 * - an entry is valid while clock.millis() < loadedAt + ttl (present value) or + negativeTtl (loader returned empty); expiry is lazy, on access.
 * - capacity: when size exceeds maxEntries the least recently USED entry is evicted (get on a valid entry counts as use).
 * - single flight: concurrent get() of one missing key calls the loader once; the others wait for that result. The loader runs OUTSIDE any lock.
 * - loader exception: nothing is cached, every waiter of that flight gets CacheLoadException (cause = the original), the next get() retries.
 * - invalidate(key) during a load: that load's result is returned to its own waiters but NOT stored.
 */
public final class TtlLruCache<K, V> implements LoadingCache<K, V> {
    public TtlLruCache(int maxEntries, Duration ttl, Duration negativeTtl, Function<K, Optional<V>> loader, Clock clock) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override public Optional<V> get(K key) { throw new UnsupportedOperationException("TODO"); }
    @Override public void invalidate(K key) { throw new UnsupportedOperationException("TODO"); }
    @Override public int purgeExpired() { throw new UnsupportedOperationException("TODO"); }
    @Override public int size() { throw new UnsupportedOperationException("TODO"); }
    @Override public CacheStats stats() { throw new UnsupportedOperationException("TODO"); }
}
