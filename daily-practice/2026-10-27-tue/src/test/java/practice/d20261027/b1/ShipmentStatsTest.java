package practice.d20261027.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import org.junit.jupiter.api.Test;
import practice.d20261027.b1.Shipment.Status;

class ShipmentStatsTest {
    static Shipment sh(String id, String wh, String carrier, Status st, int kg, long cost, String date, String... tags) {
        return new Shipment(id, wh, carrier, st, kg, cost, LocalDate.parse(date), List.of(tags));
    }

    static final List<Shipment> S = List.of(
            sh("s1", "BLR", "dhl", Status.DELIVERED, 10, 500, "2026-09-30", "fragile", "cold"),
            sh("s2", "BLR", "bluedart", Status.DELAYED, 40, 900, "2026-10-01", "heavy"),
            sh("s3", "DEL", "dhl", Status.IN_TRANSIT, 40, 700, "2026-10-01", "fragile"),
            sh("s4", "BLR", "dhl", Status.DELAYED, 40, 300, "2026-10-01"),
            sh("s5", "DEL", "fedex", Status.RETURNED, 5, 1200, "2026-10-02", "cold", "heavy"));

    @Test void totalCost() {
        assertEquals(Map.of("dhl", 1500L, "bluedart", 900L, "fedex", 1200L), ShipmentStats.totalCostByCarrier(S));
        assertEquals(Map.of(), ShipmentStats.totalCostByCarrier(List.of()));
    }

    @Test void carriersByWarehouseAreSorted() {
        Map<String, Set<String>> m = ShipmentStats.carriersByWarehouse(S);
        assertEquals(List.of("BLR", "DEL"), new ArrayList<>(m.keySet()));
        assertEquals(List.of("bluedart", "dhl"), new ArrayList<>(m.get("BLR")));
        assertEquals(List.of("dhl", "fedex"), new ArrayList<>(m.get("DEL")));
    }

    @Test void heaviestTieGoesToSmallerId() {
        assertEquals(Map.of("BLR", "s2", "DEL", "s3"), ShipmentStats.heaviestIdByWarehouse(S));
    }

    @Test void delayedSplitAlwaysHasBothKeys() {
        assertEquals(Map.of(true, 2L, false, 3L), ShipmentStats.delayedSplit(S));
        assertEquals(Map.of(true, 0L, false, 0L), ShipmentStats.delayedSplit(List.of()));
    }

    @Test void tags() {
        assertEquals("cold,fragile,heavy", ShipmentStats.tagsCsv(S));
        assertEquals("", ShipmentStats.tagsCsv(List.of()));
        assertEquals("", ShipmentStats.tagsCsv(List.of(sh("x", "A", "c", Status.DELIVERED, 1, 1, "2026-01-01"))));
    }

    @Test void monthlyStatsAreSortedAndComplete() {
        Map<YearMonth, IntSummaryStatistics> m = ShipmentStats.weightStatsByMonth(S);
        assertEquals(List.of(YearMonth.of(2026, 9), YearMonth.of(2026, 10)), new ArrayList<>(m.keySet()));
        IntSummaryStatistics oct = m.get(YearMonth.of(2026, 10));
        assertEquals(4, oct.getCount());
        assertEquals(125, oct.getSum());
        assertEquals(5, oct.getMin());
        assertEquals(40, oct.getMax());
        assertEquals(31.25, oct.getAverage(), 1e-9);
    }

    @Test void topCarriers() {
        assertEquals(List.of("dhl", "fedex"), ShipmentStats.topCarriersByCost(S, 2));
        assertEquals(List.of("dhl", "fedex", "bluedart"), ShipmentStats.topCarriersByCost(S, 10));
        assertEquals(List.of(), ShipmentStats.topCarriersByCost(S, 0));
        List<Shipment> tie = List.of(sh("a", "W", "zed", Status.DELIVERED, 1, 10, "2026-01-01"), sh("b", "W", "abc", Status.DELIVERED, 1, 10, "2026-01-01"));
        assertEquals(List.of("abc", "zed"), ShipmentStats.topCarriersByCost(tie, 2));
    }

    @Test void busiestDayTieGoesToEarliest() {
        assertEquals(Optional.of(LocalDate.parse("2026-10-01")), ShipmentStats.busiestDay(S));
        List<Shipment> tie = List.of(sh("a", "W", "c", Status.DELIVERED, 1, 1, "2026-02-02"), sh("b", "W", "c", Status.DELIVERED, 1, 1, "2026-01-01"));
        assertEquals(Optional.of(LocalDate.parse("2026-01-01")), ShipmentStats.busiestDay(tie));
        assertEquals(Optional.empty(), ShipmentStats.busiestDay(List.of()));
    }

    @Test void costSumUsesLongNotInt() {
        List<Shipment> big = List.of(sh("a", "W", "c", Status.DELIVERED, 1, 2_000_000_000L, "2026-01-01"), sh("b", "W", "c", Status.DELIVERED, 1, 2_000_000_000L, "2026-01-01"));
        assertEquals(4_000_000_000L, ShipmentStats.totalCostByCarrier(big).get("c"));
    }
}
