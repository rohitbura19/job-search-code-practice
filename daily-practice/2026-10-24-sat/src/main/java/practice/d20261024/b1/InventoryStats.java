package practice.d20261024.b1;

import java.time.LocalDate;
import java.util.*;

public final class InventoryStats {
    private InventoryStats() {}

    /** Total qty per sku, keys sorted ascending. A sku can appear many times: merge, do not throw. */
    public static Map<String, Integer> totalBySku(List<Stock> stock) { throw new UnsupportedOperationException("TODO"); }

    /** Per sku: the DISTINCT warehouses, sorted ascending; keys sorted ascending; the lists are unmodifiable. */
    public static Map<String, List<String>> warehousesBySku(List<Stock> stock) { throw new UnsupportedOperationException("TODO"); }

    /** The stock expiring soonest on or after today (expired ones are skipped); ties: smaller sku, then smaller warehouse. */
    public static Optional<Stock> earliestExpiring(List<Stock> stock, LocalDate today) { throw new UnsupportedOperationException("TODO"); }

    /** Total qty per expiry date, keys ascending. */
    public static NavigableMap<LocalDate, Integer> qtyByExpiry(List<Stock> stock) { throw new UnsupportedOperationException("TODO"); }

    /** Sum of qty with from <= expiry <= to (inclusive) using the sorted map's range view; 0 if from is after to. */
    public static int qtyExpiringBetween(NavigableMap<LocalDate, Integer> byExpiry, LocalDate from, LocalDate to) { throw new UnsupportedOperationException("TODO"); }

    /** Per warehouse, the single stock line with the largest qty; ties: the lexicographically smaller sku. Keys sorted. */
    public static Map<String, Stock> largestPerWarehouse(List<Stock> stock) { throw new UnsupportedOperationException("TODO"); }
}
