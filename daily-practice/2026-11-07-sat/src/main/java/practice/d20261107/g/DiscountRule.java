package practice.d20261107.g;

import java.util.List;

public interface DiscountRule {
    /** Discount in cents (>= 0) for these priced lines. */
    long discountCents(List<CartLine> lines);
}
