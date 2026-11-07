package practice.d20261107.g;

import java.util.List;

/** Immutable snapshot. lines sorted by sku. totalCents = subtotalCents - discountCents. */
public record CartView(String owner, List<CartLine> lines, long subtotalCents, long discountCents, long totalCents) {
    public int itemCount() { return lines.stream().mapToInt(CartLine::qty).sum(); }
}
