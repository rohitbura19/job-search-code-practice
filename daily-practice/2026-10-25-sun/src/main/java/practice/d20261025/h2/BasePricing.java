package practice.d20261025.h2;

/** The undecorated price: the subtotal. */
public final class BasePricing implements Pricing {
    @Override public long priceOf(Order order) { return order.subtotalPaise(); }
}
