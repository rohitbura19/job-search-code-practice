package practice.d20261025.h2;

/** Delegation: hold the inner Pricing, change one aspect. discount = floor(price * percent / 100) via Math.multiplyExact; O(1). */
public final class DiscountPricing implements Pricing {
    private final Pricing inner;
    private final int percent;

    public DiscountPricing(Pricing inner, int percent) {
        if (inner == null) throw new NullPointerException("inner");
        if (percent < 0 || percent > 100) throw new IllegalArgumentException("percent");
        this.inner = inner;
        this.percent = percent;
    }

    @Override public long priceOf(Order order) {
        long price = inner.priceOf(order);
        return price - Math.multiplyExact(price, (long) percent) / 100;
    }
}
