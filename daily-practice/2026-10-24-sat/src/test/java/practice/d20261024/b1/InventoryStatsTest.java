package practice.d20261024.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;

class InventoryStatsTest {
    static LocalDate d(int day) { return LocalDate.of(2026, 11, day); }

    static final List<Stock> STOCK = List.of(
            new Stock("bolt", "W2", 40, d(10)),
            new Stock("nut", "W1", 25, d(5)),
            new Stock("bolt", "W1", 10, d(10)),
            new Stock("gear", "W1", 25, d(20)),
            new Stock("nut", "W2", 5, d(3)),
            new Stock("bolt", "W1", 7, d(15)));
    static final LocalDate TODAY = d(4);

    @Test void totalsMergeDuplicateKeysAndAreSortedBySku() {
        Map<String, Integer> m = InventoryStats.totalBySku(STOCK);
        assertEquals(List.of("bolt", "gear", "nut"), new ArrayList<>(m.keySet()));
        assertEquals(Map.of("bolt", 57, "gear", 25, "nut", 30), m);
        assertEquals(Map.of(), InventoryStats.totalBySku(List.of()));
    }

    @Test void warehousesAreDistinctSortedAndImmutable() {
        Map<String, List<String>> m = InventoryStats.warehousesBySku(STOCK);
        assertEquals(List.of("W1", "W2"), m.get("bolt"));
        assertEquals(List.of("W1"), m.get("gear"));
        assertEquals(List.of("bolt", "gear", "nut"), new ArrayList<>(m.keySet()));
        assertThrows(UnsupportedOperationException.class, () -> m.get("bolt").add("W9"));
    }

    @Test void earliestExpiringSkipsExpiredAndBreaksTiesBySku() {
        assertEquals(new Stock("nut", "W1", 25, d(5)), InventoryStats.earliestExpiring(STOCK, TODAY).orElseThrow());
        assertEquals(new Stock("nut", "W2", 5, d(3)), InventoryStats.earliestExpiring(STOCK, d(3)).orElseThrow(), "expiring today counts");
        assertEquals(new Stock("bolt", "W1", 10, d(10)), InventoryStats.earliestExpiring(STOCK, d(6)).orElseThrow(), "tie bolt/W1 vs bolt/W2");
        assertTrue(InventoryStats.earliestExpiring(STOCK, d(21)).isEmpty());
        assertTrue(InventoryStats.earliestExpiring(List.of(), TODAY).isEmpty());
    }

    @Test void qtyByExpiryAndRange() {
        NavigableMap<LocalDate, Integer> m = InventoryStats.qtyByExpiry(STOCK);
        assertEquals(List.of(d(3), d(5), d(10), d(15), d(20)), new ArrayList<>(m.keySet()));
        assertEquals(50, m.get(d(10)));
        assertEquals(75, InventoryStats.qtyExpiringBetween(m, d(5), d(10)), "both ends inclusive");
        assertEquals(50, InventoryStats.qtyExpiringBetween(m, d(6), d(14)));
        assertEquals(0, InventoryStats.qtyExpiringBetween(m, d(11), d(14)));
        assertEquals(0, InventoryStats.qtyExpiringBetween(m, d(10), d(5)), "from after to");
        assertEquals(112, InventoryStats.qtyExpiringBetween(m, d(1), d(30)));
    }

    @Test void largestPerWarehouseBreaksTiesBySmallerSku() {
        Map<String, Stock> m = InventoryStats.largestPerWarehouse(STOCK);
        assertEquals(new Stock("gear", "W1", 25, d(20)), m.get("W1"), "gear and nut both 25: smaller sku wins");
        assertEquals(new Stock("bolt", "W2", 40, d(10)), m.get("W2"));
        assertEquals(2, m.size());
        assertEquals(Map.of(), InventoryStats.largestPerWarehouse(List.of()));
    }
}
