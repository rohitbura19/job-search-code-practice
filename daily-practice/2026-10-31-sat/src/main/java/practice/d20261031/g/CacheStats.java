package practice.d20261031.g;

/** hits: served from cache. misses: not served from cache (includes callers that waited for another caller's load). loads: loader calls that returned. */
public record CacheStats(long hits, long misses, long loads, long loadFailures, long evictions, long expirations) {}
