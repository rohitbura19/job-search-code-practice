package practice.d20261107.g;

public record CartLine(String sku, int qty, long unitPriceCents) {
    public long totalCents() { return qty * unitPriceCents; }
}
