package practice.d20261031.g;

import java.util.Optional;

/** Read-through cache. get() returns the cached value, or loads it (once, even under concurrency). Empty means "known to be absent". */
public interface LoadingCache<K, V> {
    Optional<V> get(K key);

    /** Drops the entry AND detaches any load in flight: that load's result must not be stored. */
    void invalidate(K key);

    /** Removes every expired entry; returns how many. */
    int purgeExpired();

    /** Entries currently held, expired-but-untouched ones included. */
    int size();

    CacheStats stats();
}
