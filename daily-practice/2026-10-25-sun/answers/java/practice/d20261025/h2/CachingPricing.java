package practice.d20261025.h2;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap.computeIfAbsent runs the loader once per key and does not cache an exception. Caveat: the loader runs inside the map's
 * bin lock, so a slow inner pricing blocks other keys that hash to the same bin; for slow loaders cache a Future/CompletableFuture instead.
 * Unbounded growth is the other caveat: use a bounded cache (Caffeine) in production.
 */
public final class CachingPricing implements Pricing {
    private final Pricing inner;
    private final ConcurrentHashMap<Order, Long> cache = new ConcurrentHashMap<>();

    public CachingPricing(Pricing inner) {
        if (inner == null) throw new NullPointerException("inner");
        this.inner = inner;
    }

    @Override public long priceOf(Order order) { return cache.computeIfAbsent(order, inner::priceOf); }
}
