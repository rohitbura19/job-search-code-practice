package s27_warehouse;

import static check.Assert.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import check.Test;

class WarehouseStreamsCheck {
    static Movement m(String sku, String wh, String type, int qty, String date) { return new Movement(sku, wh, type, qty, LocalDate.parse(date)); }

    static final List<Movement> DATA = List.of(
            m("B", "W2", "IN", 50, "2026-11-01"), m("A", "W1", "IN", 10, "2026-11-01"), m("A", "W2", "IN", 10, "2026-11-02"),
            m("B", "W1", "OUT", 70, "2026-11-02"), m("A", "W1", "OUT", 5, "2026-11-03"), m("C", "W3", "IN", 1, "2026-11-03"));

    @Test void stock() {
        Map<String, Integer> s = WarehouseStreams.stockBySku(DATA);
        assertEquals(List.of("A", "B", "C"), List.copyOf(s.keySet()));
        assertEquals(Map.of("A", 15, "B", -20, "C", 1), s);
        assertEquals(Map.of(), WarehouseStreams.stockBySku(List.of()));
    }
    @Test void busiest() {
        // A: W1 = 15, W2 = 10 -> W1; B: W2 = 50, W1 = 70 -> W1; C: W3
        assertEquals(Map.of("A", "W1", "B", "W1", "C", "W3"), WarehouseStreams.busiestWarehouse(DATA));
        List<Movement> tie = List.of(m("X", "W9", "IN", 5, "2026-11-01"), m("X", "W2", "IN", 5, "2026-11-01"));
        assertEquals("W2", WarehouseStreams.busiestWarehouse(tie).get("X"));
    }
    @Test void negatives() {
        assertEquals(List.of("B"), WarehouseStreams.negativeStockSkus(DATA));
        assertEquals(List.of(), WarehouseStreams.negativeStockSkus(List.of()));
    }
    @Test void totals() {
        assertEquals(new WarehouseStreams.Totals(71, 75), WarehouseStreams.totals(DATA));
        assertEquals(new WarehouseStreams.Totals(0, 0), WarehouseStreams.totals(List.of()));
    }
    @Test void perDay() {
        Map<LocalDate, Set<String>> d = WarehouseStreams.skusPerDay(DATA);
        assertEquals(List.of(LocalDate.parse("2026-11-01"), LocalDate.parse("2026-11-02"), LocalDate.parse("2026-11-03")), List.copyOf(d.keySet()));
        assertEquals(List.of("A", "B"), List.copyOf(d.get(LocalDate.parse("2026-11-01"))));
        assertEquals(List.of("A", "C"), List.copyOf(d.get(LocalDate.parse("2026-11-03"))));
    }
    @Test void topOutgoing() {
        assertEquals("B:70,A:5", WarehouseStreams.topOutgoing(DATA, 5));
        assertEquals("B:70", WarehouseStreams.topOutgoing(DATA, 1));
        assertEquals("", WarehouseStreams.topOutgoing(List.of(m("A", "W", "IN", 1, "2026-11-01")), 3));
        List<Movement> tie = List.of(m("Z", "W", "OUT", 4, "2026-11-01"), m("Y", "W", "OUT", 4, "2026-11-01"));
        assertEquals("Y:4,Z:4", WarehouseStreams.topOutgoing(tie, 2));
    }
    @Test void largeInputIsLinearish() {
        List<Movement> big = new ArrayList<>();
        Random r = new Random(1);
        for (int i = 0; i < 300_000; i++) big.add(m("S" + r.nextInt(1000), "W" + r.nextInt(20), r.nextBoolean() ? "IN" : "OUT", 1 + r.nextInt(9), "2026-11-01"));
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            assertEquals(1000, WarehouseStreams.busiestWarehouse(big).size());
            WarehouseStreams.topOutgoing(big, 10);
            WarehouseStreams.totals(big);
        });
    }

    public static void main(String[] args) {
        check.Runner.run(WarehouseStreamsCheck.class);
    }
}
