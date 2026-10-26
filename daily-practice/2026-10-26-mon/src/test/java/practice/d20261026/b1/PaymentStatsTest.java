package practice.d20261026.b1;

import static org.junit.jupiter.api.Assertions.*;
import static practice.d20261026.b1.Status.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;

class PaymentStatsTest {
    static final LocalDate D1 = LocalDate.of(2026, 10, 1), D2 = D1.plusDays(1);

    static Payment p(String id, String merchant, String method, long amt, Status st, LocalDate d) { return new Payment(id, merchant, method, amt, st, d); }

    static final List<Payment> SAMPLE = List.of(
            p("p1", "zeta", "UPI", 500, SETTLED, D1),
            p("p2", "alpha", "CARD", 700, SETTLED, D1),
            p("p3", "alpha", "UPI", 300, FAILED, D1),
            p("p4", "zeta", "UPI", 500, SETTLED, D2),
            p("p5", "beta", "CARD", 100, REFUNDED, D2),
            p("p6", "alpha", "UPI", 200, SETTLED, D2),
            p("p7", "gamma", "WALLET", 900, FAILED, D2));

    @Test void totalsAreSortedAndSettledOnly() {
        Map<String, Long> m = PaymentStats.settledTotalByMerchant(SAMPLE);
        assertEquals(List.of("alpha", "zeta"), new ArrayList<>(m.keySet()));
        assertEquals(900L, m.get("alpha"));
        assertEquals(1000L, m.get("zeta"));
        assertTrue(PaymentStats.settledTotalByMerchant(List.of()).isEmpty());
    }

    @Test void countByStatusHasEveryKey() {
        Map<Status, Long> m = PaymentStats.countByStatus(SAMPLE.subList(0, 3));
        assertEquals(List.of(SETTLED, FAILED, REFUNDED), new ArrayList<>(m.keySet()));
        assertEquals(2L, m.get(SETTLED));
        assertEquals(1L, m.get(FAILED));
        assertEquals(0L, m.get(REFUNDED));
        assertEquals(Map.of(SETTLED, 0L, FAILED, 0L, REFUNDED, 0L), PaymentStats.countByStatus(List.of()));
    }

    @Test void topMerchantBreaksTiesAlphabetically() {
        assertEquals(Optional.of("zeta"), PaymentStats.topMerchant(SAMPLE));
        List<Payment> tie = List.of(p("a", "m2", "UPI", 10, SETTLED, D1), p("b", "m1", "UPI", 10, SETTLED, D1));
        assertEquals(Optional.of("m1"), PaymentStats.topMerchant(tie));
        assertEquals(Optional.empty(), PaymentStats.topMerchant(List.of(p("x", "m", "UPI", 5, FAILED, D1))));
    }

    @Test void largestPerMethodUsesSmallerIdOnTies() {
        Map<String, Payment> m = PaymentStats.largestSettledPerMethod(SAMPLE);
        assertEquals(List.of("CARD", "UPI"), new ArrayList<>(m.keySet()));
        assertEquals("p2", m.get("CARD").id());
        assertEquals("p1", m.get("UPI").id(), "p1 and p4 both 500: smaller id wins, in either input order");
        List<Payment> reversed = new ArrayList<>(SAMPLE);
        Collections.reverse(reversed);
        assertEquals("p1", PaymentStats.largestSettledPerMethod(reversed).get("UPI").id());
    }

    @Test void averagePerDay() {
        SortedMap<LocalDate, Double> m = PaymentStats.averageSettledPerDay(SAMPLE);
        assertEquals(600.0, m.get(D1));
        assertEquals(350.0, m.get(D2));
        assertEquals(D1, m.firstKey());
        assertTrue(PaymentStats.averageSettledPerDay(List.of(p("x", "m", "UPI", 5, FAILED, D1))).isEmpty());
    }

    @Test void rangeInOnePass() {
        assertEquals(Optional.of(new AmountRange(200, 700)), PaymentStats.settledRange(SAMPLE));
        assertEquals(Optional.empty(), PaymentStats.settledRange(List.of()));
        assertEquals(Optional.of(new AmountRange(Long.MAX_VALUE, Long.MAX_VALUE)),
                PaymentStats.settledRange(List.of(p("x", "m", "UPI", Long.MAX_VALUE, SETTLED, D1))));
    }

    @Test void methodsByMerchantIncludesEveryStatus() {
        Map<String, Set<String>> m = PaymentStats.methodsByMerchant(SAMPLE);
        assertEquals(List.of("alpha", "beta", "gamma", "zeta"), new ArrayList<>(m.keySet()));
        assertEquals(List.of("CARD", "UPI"), new ArrayList<>(m.get("alpha")));
        assertEquals(Set.of("WALLET"), m.get("gamma"));
    }

    @Test void topIds() {
        assertEquals("p2,p1,p4", PaymentStats.topIds(SAMPLE, 3));
        assertEquals("", PaymentStats.topIds(SAMPLE, 0));
        assertEquals("p2,p1,p4,p6", PaymentStats.topIds(SAMPLE, 99));
        assertEquals("", PaymentStats.topIds(List.of(), 3));
    }

    @Test void randomAgainstLoops() {
        Random r = new Random(11);
        String[] merchants = {"a", "b", "c", "d"}, methods = {"UPI", "CARD", "WALLET"};
        for (int t = 0; t < 300; t++) {
            List<Payment> list = new ArrayList<>();
            for (int i = 0; i < r.nextInt(25); i++)
                list.add(p("id" + String.format("%03d", i), merchants[r.nextInt(4)], methods[r.nextInt(3)], 1 + r.nextInt(5) * 100L,
                        Status.values()[r.nextInt(3)], D1.plusDays(r.nextInt(3))));
            Map<String, Long> totals = new TreeMap<>();
            for (Payment x : list) if (x.status() == SETTLED) totals.merge(x.merchant(), x.amountPaise(), Long::sum);
            assertEquals(totals, PaymentStats.settledTotalByMerchant(list));
            String best = null;
            for (var e : totals.entrySet()) if (best == null || e.getValue() > totals.get(best)) best = e.getKey();
            assertEquals(Optional.ofNullable(best), PaymentStats.topMerchant(list));
            List<Payment> settled = list.stream().filter(x -> x.status() == SETTLED).sorted((x, y) ->
                    x.amountPaise() != y.amountPaise() ? Long.compare(y.amountPaise(), x.amountPaise()) : x.id().compareTo(y.id())).toList();
            StringJoiner sj = new StringJoiner(",");
            for (int i = 0; i < Math.min(3, settled.size()); i++) sj.add(settled.get(i).id());
            assertEquals(sj.toString(), PaymentStats.topIds(list, 3));
        }
    }

    @Test void largeInput() {
        List<Payment> big = new ArrayList<>();
        for (int i = 0; i < 300_000; i++) big.add(p("id" + i, "m" + (i % 1000), "UPI", i % 977 + 1, Status.values()[i % 3], D1.plusDays(i % 30)));
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            PaymentStats.settledTotalByMerchant(big);
            PaymentStats.topMerchant(big);
            PaymentStats.largestSettledPerMethod(big);
            PaymentStats.averageSettledPerDay(big);
            PaymentStats.settledRange(big);
            PaymentStats.methodsByMerchant(big);
            PaymentStats.topIds(big, 10);
        });
    }
}
