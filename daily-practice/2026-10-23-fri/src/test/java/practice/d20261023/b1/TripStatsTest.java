package practice.d20261023.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class TripStatsTest {
    static Trip t(String d, String c, int km, long fare, boolean x) { return new Trip(d, c, km, fare, x); }

    static final List<Trip> TRIPS = List.of(
            t("asha", "Pune", 10, 1000, false), t("asha", "Pune", 20, 2500, false), t("bala", "Mumbai", 5, 700, false),
            t("bala", "Pune", 7, 900, true), t("chen", "Mumbai", 15, 3000, false), t("chen", "Delhi", 8, 800, true),
            t("dev", "Mumbai", 1, 3500, false));

    @Test void revenueByCityIgnoresCancelledAndIsSorted() {
        Map<String, Long> m = TripStats.revenueByCity(TRIPS);
        assertEquals(List.of("Mumbai", "Pune"), new ArrayList<>(m.keySet()));
        assertEquals(7200L, m.get("Mumbai"));
        assertEquals(3500L, m.get("Pune"));
        assertEquals(Map.of(), TripStats.revenueByCity(List.of()));
    }

    @Test void avgKm() {
        Map<String, Double> m = TripStats.avgKmByDriver(TRIPS);
        assertEquals(List.of("asha", "bala", "chen", "dev"), new ArrayList<>(m.keySet()));
        assertEquals(15.0, m.get("asha"), 1e-9);
        assertEquals(5.0, m.get("bala"), 1e-9);
        assertEquals(15.0, m.get("chen"), 1e-9);
    }

    @Test void driversByCityAreDistinctSortedAndIncludeCancelled() {
        Map<String, List<String>> m = TripStats.driversByCity(TRIPS);
        assertEquals(List.of("Delhi", "Mumbai", "Pune"), new ArrayList<>(m.keySet()));
        assertEquals(List.of("chen"), m.get("Delhi"));
        assertEquals(List.of("bala", "chen", "dev"), m.get("Mumbai"));
        assertEquals(List.of("asha", "bala"), m.get("Pune"));
        assertThrows(UnsupportedOperationException.class, () -> m.get("Pune").add("zed"));
    }

    @Test void topDriverAndTieBreak() {
        assertEquals(Optional.of("asha"), TripStats.topDriverByRevenue(TRIPS));       // asha 3500 ties dev 3500
    }

    @Test void topDriverTieGoesToSmallerName() {
        List<Trip> l = List.of(t("zed", "A", 1, 100, false), t("amy", "A", 1, 100, false), t("bob", "A", 1, 99, false));
        assertEquals(Optional.of("amy"), TripStats.topDriverByRevenue(l));
        assertEquals(Optional.empty(), TripStats.topDriverByRevenue(List.of(t("zed", "A", 1, 100, true))));
        assertEquals(Optional.empty(), TripStats.topDriverByRevenue(List.of()));
    }

    @Test void splitHasBothKeysEvenWhenEmpty() {
        assertEquals(Map.of(true, 2L, false, 5L), TripStats.cancelledSplit(TRIPS));
        assertEquals(Map.of(true, 0L, false, 0L), TripStats.cancelledSplit(List.of()));
        assertEquals(Map.of(true, 0L, false, 1L), TripStats.cancelledSplit(List.of(t("a", "b", 1, 1, false))));
    }

    @Test void report() {
        assertEquals("Mumbai:3,Pune:2", TripStats.cityReport(TRIPS));
        assertEquals("", TripStats.cityReport(List.of()));
        assertEquals("A:1,B:1", TripStats.cityReport(List.of(t("x", "B", 1, 1, false), t("x", "A", 1, 1, false))));
    }

    @Test void fareRange() {
        assertEquals(Optional.of(new FareRange(700, 3500)), TripStats.fareRange(TRIPS));
        assertEquals(Optional.empty(), TripStats.fareRange(List.of(t("a", "b", 1, 5, true))));
        assertEquals(Optional.of(new FareRange(9, 9)), TripStats.fareRange(List.of(t("a", "b", 1, 9, false))));
    }
}
