package practice.d20261025.h2;

/** tax = (price * bp + 5000) / 10000 is round-half-up for non-negative values; exact arithmetic so overflow throws instead of wrapping. */
public final class TaxPricing implements Pricing {
    private final Pricing inner;
    private final int basisPoints;

    public TaxPricing(Pricing inner, int basisPoints) {
        if (inner == null) throw new NullPointerException("inner");
        if (basisPoints < 0) throw new IllegalArgumentException("basisPoints");
        this.inner = inner;
        this.basisPoints = basisPoints;
    }

    @Override public long priceOf(Order order) {
        long price = inner.priceOf(order);
        long tax = Math.addExact(Math.multiplyExact(price, (long) basisPoints), 5000L) / 10_000L;
        return Math.addExact(price, tax);
    }
}
