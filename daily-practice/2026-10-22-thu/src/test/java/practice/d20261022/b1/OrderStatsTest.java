package practice.d20261022.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class OrderStatsTest {
    static final List<Order> ORDERS = List.of(
            new Order("o1", "zoe", "book", 2, 500),     // 1000
            new Order("o2", "amy", "toy", 1, 1500),     // 1500
            new Order("o3", "zoe", "toy", 3, 500),      // 1500
            new Order("o4", "bob", "book", 1, 300),     //  300
            new Order("o5", "amy", "book", 1, 1000),    // 1000
            new Order("o6", "zoe", "book", 1, 0));      //    0

    @Test void revenue() {
        Map<String, Long> m = OrderStats.revenueByCustomer(ORDERS);
        assertEquals(List.of("amy", "bob", "zoe"), new ArrayList<>(m.keySet()));
        assertEquals(List.of(2500L, 300L, 2500L), new ArrayList<>(m.values()));
        assertTrue(OrderStats.revenueByCustomer(List.of()).isEmpty());
    }

    @Test void partition() {
        Map<Boolean, List<String>> p = OrderStats.partitionByTotal(ORDERS, 1000);
        assertEquals(List.of("o1", "o2", "o3", "o5"), p.get(true));
        assertEquals(List.of("o4", "o6"), p.get(false));
        Map<Boolean, List<String>> none = OrderStats.partitionByTotal(List.of(), 5);
        assertEquals(List.of(), none.get(true));
        assertEquals(List.of(), none.get(false));
    }

    @Test void categories() {
        Map<String, TreeSet<String>> m = OrderStats.categoriesByCustomer(ORDERS);
        assertEquals(List.of("amy", "bob", "zoe"), new ArrayList<>(m.keySet()));
        assertEquals(List.of("book", "toy"), new ArrayList<>(m.get("zoe")));
        assertEquals(Set.of("book"), m.get("bob"));
    }

    @Test void topCustomerTieGoesToAlphabeticallyFirst() {
        assertEquals(Optional.of("amy"), OrderStats.topCustomer(ORDERS));
        assertEquals(Optional.empty(), OrderStats.topCustomer(List.of()));
    }

    @Test void minMax() {
        assertEquals(Optional.of(new MinMax(0, 1500)), OrderStats.minMaxTotal(ORDERS));
        assertEquals(Optional.empty(), OrderStats.minMaxTotal(List.of()));
        assertEquals(Optional.of(new MinMax(7, 7)), OrderStats.minMaxTotal(List.of(new Order("x", "c", "k", 1, 7))));
    }

    @Test void totalsDoNotOverflowInt() {
        Order big = new Order("big", "c", "k", 100_000, 100_000);   // 10^10 > Integer.MAX_VALUE
        assertEquals(Optional.of(new MinMax(10_000_000_000L, 10_000_000_000L)), OrderStats.minMaxTotal(List.of(big)));
        assertEquals(10_000_000_000L, OrderStats.revenueByCustomer(List.of(big)).get("c"));
    }

    @Test void topIds() {
        assertEquals(List.of("o2", "o3", "o1"), OrderStats.topIds(ORDERS, 3));
        assertEquals(List.of("o2", "o3", "o1", "o5", "o4", "o6"), OrderStats.topIds(ORDERS, 99));
        assertEquals(List.of(), OrderStats.topIds(ORDERS, 0));
    }
}
