package practice.d20261112.b1;

import java.time.LocalDate;

/** type is "IN" (stock arrives) or "OUT" (stock leaves). */
public record Movement(String sku, String warehouse, String type, int qty, LocalDate date) {
    public int signedQty() { return type.equals("IN") ? qty : -qty; }
}
