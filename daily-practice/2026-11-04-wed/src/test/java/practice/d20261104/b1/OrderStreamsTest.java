package practice.d20261104.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import org.junit.jupiter.api.Test;
import practice.d20261104.b1.OrderStreams.Order;

class OrderStreamsTest {
    static Order o(String id, String cust, String cat, int cents, String date) { return new Order(id, cust, cat, cents, LocalDate.parse(date)); }

    static final List<Order> SAMPLE = List.of(
            o("o1", "bob", "books", 1200, "2026-10-30"),
            o("o2", "ann", "books", 1200, "2026-10-31"),
            o("o3", "cy", "toys", 5000, "2026-11-01"),
            o("o4", "ann", "toys", 700, "2026-11-01"),
            o("o5", "bob", "books", 300, "2026-11-03"),
            o("o6", "dee", "games", 0, "2026-11-04"));

    @Test void totals() {
        Map<String, Long> t = OrderStreams.totalByCustomer(SAMPLE);
        assertEquals(List.of("ann", "bob", "cy", "dee"), new ArrayList<>(t.keySet()));
        assertEquals(List.of(1900L, 1500L, 5000L, 0L), new ArrayList<>(t.values()));
        assertTrue(OrderStreams.totalByCustomer(List.of()).isEmpty());
    }

    @Test void totalsDoNotOverflowInt() {
        List<Order> big = List.of(o("a", "x", "c", Integer.MAX_VALUE, "2026-01-01"), o("b", "x", "c", Integer.MAX_VALUE, "2026-01-02"));
        assertEquals(2L * Integer.MAX_VALUE, OrderStreams.totalByCustomer(big).get("x"));
        assertEquals(2L * Integer.MAX_VALUE, OrderStreams.revenueByMonth(big).get(YearMonth.of(2026, 1)));
    }

    @Test void topCustomers() {
        assertEquals(List.of("cy", "ann", "bob"), OrderStreams.topCustomers(SAMPLE, 3));
        assertEquals(List.of("cy"), OrderStreams.topCustomers(SAMPLE, 1));
        assertEquals(List.of(), OrderStreams.topCustomers(SAMPLE, 0));
        assertEquals(List.of("cy", "ann", "bob", "dee"), OrderStreams.topCustomers(SAMPLE, 99));
        List<Order> tie = List.of(o("1", "zed", "c", 10, "2026-01-01"), o("2", "amy", "c", 10, "2026-01-01"));
        assertEquals(List.of("amy", "zed"), OrderStreams.topCustomers(tie, 2));
    }

    @Test void largestPerCategoryWithTieBreaks() {
        Map<String, String> m = OrderStreams.largestOrderIdPerCategory(SAMPLE);
        assertEquals(List.of("books", "games", "toys"), new ArrayList<>(m.keySet()));
        assertEquals("o1", m.get("books"));    // 1200 twice: earlier date wins
        assertEquals("o3", m.get("toys"));
        assertEquals("o6", m.get("games"));
        List<Order> sameDay = List.of(o("b2", "x", "c", 5, "2026-01-01"), o("b1", "y", "c", 5, "2026-01-01"));
        assertEquals("b1", OrderStreams.largestOrderIdPerCategory(sameDay).get("c"));
    }

    @Test void monthlyRevenueIsOrdered() {
        Map<YearMonth, Long> m = OrderStreams.revenueByMonth(SAMPLE);
        assertEquals(List.of(YearMonth.of(2026, 10), YearMonth.of(2026, 11)), new ArrayList<>(m.keySet()));
        assertEquals(2400L, m.get(YearMonth.of(2026, 10)));
        assertEquals(6000L, m.get(YearMonth.of(2026, 11)));
        List<Order> gap = List.of(o("1", "a", "c", 1, "2027-02-01"), o("2", "a", "c", 1, "2026-12-31"));
        assertEquals(List.of(YearMonth.of(2026, 12), YearMonth.of(2027, 2)), new ArrayList<>(OrderStreams.revenueByMonth(gap).keySet()));
    }

    @Test void customersPerCategoryString() {
        assertEquals("books: ann,bob; games: dee; toys: ann,cy", OrderStreams.customersPerCategory(SAMPLE));
        assertEquals("", OrderStreams.customersPerCategory(List.of()));
        assertEquals("c: x", OrderStreams.customersPerCategory(List.of(o("1", "x", "c", 1, "2026-01-01"), o("2", "x", "c", 1, "2026-01-02"))));
    }

    @Test void repeatCustomersNeedDifferentDates() {
        assertEquals(List.of("ann", "bob"), OrderStreams.repeatCustomers(SAMPLE));
        List<Order> sameDay = List.of(o("1", "x", "c", 1, "2026-01-01"), o("2", "x", "d", 1, "2026-01-01"));
        assertEquals(List.of(), OrderStreams.repeatCustomers(sameDay));
        assertEquals(List.of(), OrderStreams.repeatCustomers(List.of()));
    }

    @Test void dailyStreak() {
        assertEquals(3, OrderStreams.longestDailyStreak(SAMPLE.subList(0, 4)));        // Oct 30, 31, Nov 1 (two orders on Nov 1)
        assertEquals(3, OrderStreams.longestDailyStreak(SAMPLE));                      // Nov 2 is missing: 30,31,1 then 3,4
        assertEquals(0, OrderStreams.longestDailyStreak(List.of()));
        assertEquals(1, OrderStreams.longestDailyStreak(List.of(o("1", "a", "c", 1, "2026-01-01"))));
        List<Order> yearEnd = List.of(o("1", "a", "c", 1, "2026-12-31"), o("2", "a", "c", 1, "2027-01-01"), o("3", "a", "c", 1, "2026-12-30"));
        assertEquals(3, OrderStreams.longestDailyStreak(yearEnd));                     // unsorted input, crosses the year
    }

    @Test void largeInputIsFast() {
        List<Order> big = new ArrayList<>();
        Random rnd = new Random(1);
        for (int i = 0; i < 200_000; i++)
            big.add(o("o" + i, "c" + rnd.nextInt(5000), "cat" + rnd.nextInt(20), rnd.nextInt(10_000), LocalDate.of(2026, 1, 1).plusDays(rnd.nextInt(365)).toString()));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            OrderStreams.totalByCustomer(big);
            OrderStreams.topCustomers(big, 10);
            OrderStreams.largestOrderIdPerCategory(big);
            OrderStreams.revenueByMonth(big);
            OrderStreams.customersPerCategory(big);
            OrderStreams.repeatCustomers(big);
            OrderStreams.longestDailyStreak(big);
        });
    }
}
