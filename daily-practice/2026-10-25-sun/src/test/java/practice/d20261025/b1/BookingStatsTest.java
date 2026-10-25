package practice.d20261025.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import org.junit.jupiter.api.Test;

class BookingStatsTest {
    static Booking b(String id, String city, String guest, int nights, long amount, String date) {
        return new Booking(id, city, guest, nights, amount, LocalDate.parse(date));
    }

    final List<Booking> data = List.of(
            b("b1", "Goa", "asha", 3, 9000, "2026-01-10"),
            b("b2", "Goa", "Ravi", 7, 21000, "2026-01-20"),
            b("b3", "Pune", "Asha", 2, 4000, "2026-02-01"),
            b("b4", "Goa", "asha", 4, 12000, "2026-02-14"),
            b("b5", "Pune", "Meera", 8, 16000, "2026-02-15"),
            b("b6", "Delhi", "ravi", 1, 3000, "2026-01-05"));

    @Test void revenueByCityIsOrderedByRevenueThenCity() {
        Map<String, Long> m = BookingStats.revenueByCity(data);
        assertEquals(List.of("Goa", "Pune", "Delhi"), new ArrayList<>(m.keySet()));
        assertEquals(42000L, m.get("Goa"));
        assertEquals(List.of("A", "B"), new ArrayList<>(BookingStats.revenueByCity(List.of(b("1", "B", "x", 1, 5, "2026-01-01"), b("2", "A", "y", 1, 5, "2026-01-01"))).keySet()));
        assertTrue(BookingStats.revenueByCity(List.of()).isEmpty());
    }

    @Test void topGuestPerCitySumsNightsAndBreaksTiesAlphabetically() {
        Map<String, String> m = BookingStats.topGuestPerCity(data);
        assertEquals(List.of("Delhi", "Goa", "Pune"), new ArrayList<>(m.keySet()));
        assertEquals("Ravi", m.get("Goa"), "Ravi 7 vs asha 3+4 = 7: tie -> smaller name; 'Ravi' < 'asha' in natural order");
        assertEquals("Meera", m.get("Pune"));
        assertEquals("ravi", m.get("Delhi"));
    }

    @Test void avgNightsByMonth() {
        Map<YearMonth, Double> m = BookingStats.avgNightsByMonth(data);
        assertEquals(List.of(YearMonth.of(2026, 1), YearMonth.of(2026, 2)), new ArrayList<>(m.keySet()));
        assertEquals((3 + 7 + 1) / 3.0, m.get(YearMonth.of(2026, 1)), 1e-9);
        assertEquals((2 + 4 + 8) / 3.0, m.get(YearMonth.of(2026, 2)), 1e-9);
    }

    @Test void splitLongStaysHasBothKeysAndKeepsOrder() {
        Map<Boolean, List<String>> m = BookingStats.splitLongStays(data);
        assertEquals(List.of("b2", "b5"), m.get(true));
        assertEquals(List.of("b1", "b3", "b4", "b6"), m.get(false));
        Map<Boolean, List<String>> empty = BookingStats.splitLongStays(List.of());
        assertEquals(Map.of(true, List.of(), false, List.of()), empty);
    }

    @Test void countAndRevenue() {
        assertEquals(new BookingStats.CountRevenue(6, 65000), BookingStats.countAndRevenue(data));
        assertEquals(new BookingStats.CountRevenue(0, 0), BookingStats.countAndRevenue(List.of()));
        long big = Long.MAX_VALUE / 2;
        assertEquals(big, BookingStats.countAndRevenue(List.of(b("x", "c", "g", 1, big, "2026-01-01"))).revenuePaise());
    }

    @Test void guestRollIsDistinctCaseInsensitiveSortedAndKeepsFirstSpelling() {
        assertEquals("asha, Meera, Ravi", BookingStats.guestRoll(data));
        assertEquals("", BookingStats.guestRoll(List.of()));
    }

    @Test void biggestBreaksTiesByEarliestCheckInThenId() {
        assertEquals("b2", BookingStats.biggest(data).orElseThrow().id());
        List<Booking> ties = List.of(b("z", "c", "g", 1, 10, "2026-03-02"), b("y", "c", "g", 1, 10, "2026-03-01"), b("x", "c", "g", 1, 10, "2026-03-01"));
        assertEquals("x", BookingStats.biggest(ties).orElseThrow().id());
        assertTrue(BookingStats.biggest(List.of()).isEmpty());
    }
}
