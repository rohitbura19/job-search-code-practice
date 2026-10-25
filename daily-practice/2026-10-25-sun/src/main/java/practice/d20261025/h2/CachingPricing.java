package practice.d20261025.h2;

/**
 * Remembers the inner result per Order (equal orders share an entry) so the inner pricing is called once per distinct order.
 * Thread-safe; must not call the inner pricing while holding a lock that blocks other orders (use computeIfAbsent semantics carefully).
 * An exception from the inner pricing is not cached.
 */
public final class CachingPricing implements Pricing {
    public CachingPricing(Pricing inner) {}

    @Override public long priceOf(Order order) { throw new UnsupportedOperationException("TODO"); }
}
