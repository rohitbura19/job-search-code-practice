package practice.d20261013.b1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderAnalyticsTest {

    static OrderLine l(String order, String cust, String sku, int qty, double price, String city) {
        return new OrderLine(order, cust, sku, qty, price, city);
    }

    static List<OrderLine> sample() {
        return List.of(
            l("o1", "c1", "B2", 2, 10, "Pune"),     // c1: 20
            l("o2", "c2", "A1", 1, 50, "Bengaluru"), // c2: 50
            l("o3", "c3", "A1", 5, 10, "Pune"),     // c3: 50  (ties with c2 -> c2 first)
            l("o4", "c4", "C3", 1, 5, "Pune"),      // c4: 5
            l("o5", "c1", "A1", 3, 10, "Delhi"),    // c1 total 50 as well
            l("o6", "c5", "B2", 12, 1, "Delhi"));   // c5: 12, first line with qty > 10
    }

    @Test @DisplayName("top 3 customers by revenue, ties by customerId ascending")
    void top3() {
        assertEquals(List.of("c1", "c2", "c3"), OrderAnalytics.top3Customers(sample()));
    }

    @Test @DisplayName("top 3: fewer than 3 customers, and no lines")
    void top3Small() {
        assertEquals(List.of("c1"), OrderAnalytics.top3Customers(List.of(l("o", "c1", "S", 1, 1, "X"))));
        assertEquals(List.of(), OrderAnalytics.top3Customers(List.of()));
    }

    @Test @DisplayName("city (sorted) -> distinct SKUs (sorted), comma separated")
    void skusByCity() {
        Map<String, String> m = OrderAnalytics.skusByCity(sample());
        assertEquals(List.of("Bengaluru", "Delhi", "Pune"), new ArrayList<>(m.keySet()));
        assertEquals("A1", m.get("Bengaluru"));
        assertEquals("A1,B2", m.get("Delhi"));
        assertEquals("A1,B2,C3", m.get("Pune"));
    }

    @Test @DisplayName("count per SKU ordered by count desc then SKU, order preserved in the returned map")
    void countBySku() {
        Map<String, Long> m = OrderAnalytics.countBySku(sample());
        assertEquals(List.of("A1", "B2", "C3"), new ArrayList<>(m.keySet()));
        assertEquals(List.of(3L, 2L, 1L), new ArrayList<>(m.values()));
    }

    @Test @DisplayName("count per SKU: equal counts are ordered by SKU")
    void countTie() {
        Map<String, Long> m = OrderAnalytics.countBySku(List.of(l("1", "c", "Z", 1, 1, "x"), l("2", "c", "M", 1, 1, "x"), l("3", "c", "A", 1, 1, "x")));
        assertEquals(List.of("A", "M", "Z"), new ArrayList<>(m.keySet()));
    }

    @Test @DisplayName("first line with qty > 10, or NONE")
    void firstBig() {
        assertEquals("o6", OrderAnalytics.firstBigOrder(sample()));
        assertEquals("NONE", OrderAnalytics.firstBigOrder(sample().subList(0, 5)));
        assertEquals("NONE", OrderAnalytics.firstBigOrder(List.of()));
        assertEquals("NONE", OrderAnalytics.firstBigOrder(List.of(l("x", "c", "s", 10, 1, "p"))));   // exactly 10 is not > 10
    }

    // ---------- LRU ----------

    @Test @DisplayName("LRU evicts the least recently USED entry (get counts as a use)")
    void lru() {
        Lru<String, Integer> c = new Lru<>(2);
        c.put("a", 1); c.put("b", 2);
        c.get("a");              // a is now the most recent
        c.put("c", 3);           // evicts b
        assertEquals(List.of("a", "c"), new ArrayList<>(c.keySet()));
        assertNull(c.get("b"));
        assertEquals(2, c.size());
    }

    @Test @DisplayName("LRU: re-putting an existing key refreshes it and does not evict")
    void lruReput() {
        Lru<Integer, String> c = new Lru<>(2);
        c.put(1, "x"); c.put(2, "y");
        c.put(1, "z");           // refresh 1
        c.put(3, "w");           // evicts 2
        assertEquals(List.of(1, 3), new ArrayList<>(c.keySet()));
        assertEquals("z", c.get(1));
    }

    @Test @DisplayName("LRU capacity 1")
    void lruOne() {
        Lru<Integer, Integer> c = new Lru<>(1);
        c.put(1, 1); c.put(2, 2);
        assertEquals(List.of(2), new ArrayList<>(c.keySet()));
    }
}
