package practice.d20261105.g;

import java.util.List;

/** Given. Money is always in cents (long). */
public record Cart(List<Line> lines) {
    public record Line(String sku, long unitPriceCents, int qty) {}

    public long subtotalCents() {
        long sum = 0;
        for (Line l : lines) sum += l.unitPriceCents() * l.qty();
        return sum;
    }
}
