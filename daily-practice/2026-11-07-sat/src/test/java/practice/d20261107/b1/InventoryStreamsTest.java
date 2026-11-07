package practice.d20261107.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class InventoryStreamsTest {
    private static final List<Product> PS = List.of(
            new Product("pen", "office", 10, 150, List.of("write", "blue")),
            new Product("ink", "office", 0, 900, List.of("write", "blue", "blue")),
            new Product("lamp", "home", 3, 2500, List.of("light")),
            new Product("bulb", "home", 40, 300, List.of("light", "write")),
            new Product("desk", "home", 1, 2500, List.of()));

    @Test void stockByCategory() {
        Map<String, Long> m = InventoryStreams.stockByCategory(PS);
        assertEquals(List.of("home", "office"), new ArrayList<>(m.keySet()));
        assertEquals(44L, m.get("home"));
        assertEquals(10L, m.get("office"));
        assertTrue(InventoryStreams.stockByCategory(List.of()).isEmpty());
    }

    @Test void priciest() {
        Map<String, String> m = InventoryStreams.priciestPerCategory(PS);
        assertEquals("desk", m.get("home")); // tie 2500 -> alphabetically smaller
        assertEquals("ink", m.get("office"));
        assertEquals(List.of("home", "office"), new ArrayList<>(m.keySet()));
    }

    @Test void topTags() {
        assertEquals(List.of("blue", "write", "light"), InventoryStreams.topTags(PS, 3));
        assertEquals(List.of("blue"), InventoryStreams.topTags(PS, 1));
        assertEquals(List.of(), InventoryStreams.topTags(PS, 0));
        assertEquals(3, InventoryStreams.topTags(PS, 10).size());
    }

    @Test void split() {
        Map<Boolean, List<String>> m = InventoryStreams.splitByLowStock(PS, 4);
        assertEquals(List.of("desk", "ink", "lamp"), m.get(true));
        assertEquals(List.of("bulb", "pen"), m.get(false));
        Map<Boolean, List<String>> none = InventoryStreams.splitByLowStock(List.of(), 4);
        assertEquals(List.of(), none.get(true));
        assertEquals(List.of(), none.get(false));
    }

    @Test void averageInStock() {
        Map<String, Double> m = InventoryStreams.averagePriceInStock(PS);
        assertEquals(150.0, m.get("office"), 1e-9); // ink excluded
        assertEquals((2500 + 300 + 2500) / 3.0, m.get("home"), 1e-9);
        assertEquals(Map.of(), InventoryStreams.averagePriceInStock(List.of(new Product("x", "c", 0, 5, List.of()))));
    }
}
