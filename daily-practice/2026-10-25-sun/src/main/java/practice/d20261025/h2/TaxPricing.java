package practice.d20261025.h2;

/** Adds tax of `basisPoints` (1 bp = 0.01%; must be >= 0) on the inner price, tax rounded HALF UP to a whole paisa. Overflow -> ArithmeticException. */
public final class TaxPricing implements Pricing {
    public TaxPricing(Pricing inner, int basisPoints) {}

    @Override public long priceOf(Order order) { throw new UnsupportedOperationException("TODO"); }
}
