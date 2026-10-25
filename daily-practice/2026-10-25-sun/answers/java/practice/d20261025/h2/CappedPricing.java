package practice.d20261025.h2;

public final class CappedPricing implements Pricing {
    private final Pricing inner;
    private final long capPaise;

    public CappedPricing(Pricing inner, long capPaise) {
        if (inner == null) throw new NullPointerException("inner");
        if (capPaise < 0) throw new IllegalArgumentException("cap");
        this.inner = inner;
        this.capPaise = capPaise;
    }

    @Override public long priceOf(Order order) { return Math.min(inner.priceOf(order), capPaise); }
}
