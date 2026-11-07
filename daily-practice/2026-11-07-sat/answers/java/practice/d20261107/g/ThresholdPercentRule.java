package practice.d20261107.g;

import java.util.List;

public final class ThresholdPercentRule implements DiscountRule {
    private final long threshold;
    private final int percent;

    public ThresholdPercentRule(long thresholdCents, int percent) {
        if (percent < 0 || percent > 100) throw new IllegalArgumentException("percent");
        this.threshold = thresholdCents;
        this.percent = percent;
    }

    @Override public long discountCents(List<CartLine> lines) {
        long sub = lines.stream().mapToLong(CartLine::totalCents).sum();
        return sub >= threshold ? sub * percent / 100 : 0;
    }
}
