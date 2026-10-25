package practice.d20261025.h2;

/** Takes `percent` (0..100, else IllegalArgumentException) off the inner price; round the discount DOWN, so the customer never gets more than promised. */
public final class DiscountPricing implements Pricing {
    public DiscountPricing(Pricing inner, int percent) {}

    @Override public long priceOf(Order order) { throw new UnsupportedOperationException("TODO"); }
}
