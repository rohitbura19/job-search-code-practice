# G. LLD of F1: in-memory read-through cache (sketch 8 min, code 22 min)

Sketch classes and responsibilities in `my-designs/G-classes-sketch.md` first, then implement `g/TtlLruCache` (interface `LoadingCache`, records `CacheStats`, `CacheLoadException` are given). Read the class Javadoc for the rules: TTL boundary (valid while `now < loadedAt + ttl`), separate negative TTL, LRU eviction counted in stats, **single flight** with the loader outside any lock, failures not cached (waiters of that flight all get `CacheLoadException`), `invalidate` detaches a load in flight so its result is not stored.
Tests use a fake `Clock` and latches: `TtlLruCacheTest`.
Think about: why `LinkedHashMap(accessOrder=true)` plus one monitor is enough here, what a `ConcurrentHashMap` version would need, and how you would stripe the lock.
