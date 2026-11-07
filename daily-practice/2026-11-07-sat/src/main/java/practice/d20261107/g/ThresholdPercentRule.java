package practice.d20261107.g;

import java.util.List;

/** percent off the subtotal when the subtotal is at least thresholdCents (integer math, round down). */
public final class ThresholdPercentRule implements DiscountRule {
    public ThresholdPercentRule(long thresholdCents, int percent) { throw new UnsupportedOperationException("TODO"); }

    @Override public long discountCents(List<CartLine> lines) { throw new UnsupportedOperationException("TODO"); }
}
