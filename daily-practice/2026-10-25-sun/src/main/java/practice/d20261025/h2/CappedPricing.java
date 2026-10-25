package practice.d20261025.h2;

/** The inner price, but never more than `capPaise` (must be >= 0, else IllegalArgumentException). */
public final class CappedPricing implements Pricing {
    public CappedPricing(Pricing inner, long capPaise) {}

    @Override public long priceOf(Order order) { throw new UnsupportedOperationException("TODO"); }
}
