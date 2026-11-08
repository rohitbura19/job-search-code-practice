package practice.d20261108.b1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.Test;

class TxnStreamsTest {
    private static Txn t(String a, String c, long cents, String d) { return new Txn(a, c, cents, LocalDate.parse(d)); }

    private final List<Txn> data = List.of(
            t("B", "food", -2000, "2026-11-01"), t("A", "salary", 100000, "2026-11-01"),
            t("A", "food", -1500, "2026-11-02"), t("A", "rent", -50000, "2026-11-03"),
            t("A", "food", -2500, "2026-11-04"), t("B", "fun", -2000, "2026-11-05"),
            t("B", "food", 500, "2026-11-05"), t("A", "fun", -500, "2026-11-04"));

    @Test void totals() {
        Map<String, Long> m = TxnStreams.totalByAccount(data);
        assertEquals(List.of("A", "B"), new ArrayList<>(m.keySet()));
        assertEquals(45500L, m.get("A"));
        assertEquals(-3500L, m.get("B"));
        assertTrue(TxnStreams.totalByAccount(List.of()).isEmpty());
    }

    @Test void topCategories() {
        assertEquals(Map.of("A", List.of("rent", "food"), "B", List.of("fun", "food")), TxnStreams.topSpendCategories(data, 2));
        // B: food = -1500, fun = -2000 -> fun first
        assertEquals(List.of("fun"), TxnStreams.topSpendCategories(data, 1).get("B"));
        assertEquals(List.of(), TxnStreams.topSpendCategories(data, 0).get("A"));
        assertEquals(4, TxnStreams.topSpendCategories(data, 99).get("A").size());
        // tie by name
        List<Txn> ties = List.of(t("Z", "b", -5, "2026-01-01"), t("Z", "a", -5, "2026-01-01"));
        assertEquals(List.of("a", "b"), TxnStreams.topSpendCategories(ties, 2).get("Z"));
    }

    @Test void latest() {
        List<Txn> r = TxnStreams.latestPerAccountAndCategory(data);
        assertEquals(List.of("A/food/-2500", "A/fun/-500", "A/rent/-50000", "A/salary/100000", "B/food/500", "B/fun/-2000"),
                r.stream().map(x -> x.account() + "/" + x.category() + "/" + x.cents()).toList());
        List<Txn> same = List.of(t("A", "x", 1, "2026-01-01"), t("A", "x", 9, "2026-01-01"), t("A", "x", 5, "2026-01-01"));
        assertEquals(9, TxnStreams.latestPerAccountAndCategory(same).get(0).cents());
        assertTrue(TxnStreams.latestPerAccountAndCategory(List.of()).isEmpty());
    }

    @Test void largeSmall() {
        Map<Boolean, Long> m = TxnStreams.countLargeAndSmall(data, 2000);
        assertEquals(5L, m.get(true));
        assertEquals(3L, m.get(false));
        Map<Boolean, Long> empty = TxnStreams.countLargeAndSmall(List.of(), 1);
        assertEquals(0L, empty.get(true));
        assertEquals(0L, empty.get(false));
        assertEquals(8L, TxnStreams.countLargeAndSmall(data, 0).get(true));
    }

    @Test void summary() {
        assertEquals("[A=45500, B=-3500]", TxnStreams.summary(data));
        assertEquals("[]", TxnStreams.summary(List.of()));
        assertEquals("[Q=-1]", TxnStreams.summary(List.of(t("Q", "c", -1, "2026-01-01"))));
    }

    @Test void largeInput() {
        Random r = new Random(7);
        List<Txn> big = new ArrayList<>();
        long expected = 0;
        for (int i = 0; i < 200_000; i++) {
            long c = r.nextInt(2001) - 1000;
            expected += c;
            big.add(new Txn("acc" + (i % 50), "c" + (i % 7), c, LocalDate.of(2026, 1, 1).plusDays(i % 300)));
        }
        long sum = TxnStreams.totalByAccount(big).values().stream().mapToLong(Long::longValue).sum();
        assertEquals(expected, sum);
        assertEquals(350, TxnStreams.latestPerAccountAndCategory(big).size());
    }
}
