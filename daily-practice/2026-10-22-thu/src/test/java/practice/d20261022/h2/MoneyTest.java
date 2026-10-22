package practice.d20261022.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class MoneyTest {
    static Money inr(long m) { return Money.of("INR", m); }

    static List<Long> units(List<Money> l) { return l.stream().map(Money::minorUnits).toList(); }

    @Test void factoryAndAccessors() {
        assertEquals("INR", Money.of(" inr ", 5).currency());
        assertEquals(5, inr(5).minorUnits());
        assertEquals("INR 1234", inr(1234).toString());
        assertThrows(NullPointerException.class, () -> Money.of(null, 1));
        assertThrows(IllegalArgumentException.class, () -> Money.of("  ", 1));
    }

    @Test void equalsHashCodeAndMapKey() {
        assertEquals(inr(100), Money.of("inr", 100));
        assertEquals(inr(100).hashCode(), Money.of("inr", 100).hashCode());
        assertNotEquals(inr(100), Money.of("USD", 100));
        assertNotEquals(inr(100), inr(101));
        assertNotEquals(inr(100), null);
        assertNotEquals(inr(100), "INR 100");
        assertEquals(1, new HashSet<>(List.of(inr(1), Money.of("INR", 1), inr(1))).size());
    }

    @Test void compareToSortsAndRejectsMixedCurrency() {
        List<Money> l = new ArrayList<>(List.of(inr(30), inr(-5), inr(10)));
        Collections.sort(l);
        assertEquals(List.of(-5L, 10L, 30L), units(l));
        assertThrows(IllegalArgumentException.class, () -> inr(1).compareTo(Money.of("USD", 1)));
        assertEquals(0, inr(7).compareTo(inr(7)));
    }

    @Test void plusAndTimes() {
        assertEquals(inr(350), inr(100).plus(inr(250)));
        assertEquals(inr(-300), inr(100).times(-3));
        assertThrows(IllegalArgumentException.class, () -> inr(1).plus(Money.of("USD", 1)));
        assertThrows(ArithmeticException.class, () -> inr(Long.MAX_VALUE).plus(inr(1)));
        assertThrows(ArithmeticException.class, () -> inr(Long.MAX_VALUE).times(2));
    }

    @Test void allocateEvenly() {
        assertEquals(List.of(34L, 33L, 33L), units(inr(100).allocate(3)));
        assertEquals(List.of(-34L, -33L, -33L), units(inr(-100).allocate(3)));
        assertEquals(List.of(0L, 0L), units(inr(0).allocate(2)));
        assertEquals(List.of(1L, 1L, 0L, 0L), units(inr(2).allocate(4)));
        assertEquals(List.of(5L), units(inr(5).allocate(1)));
        assertThrows(IllegalArgumentException.class, () -> inr(5).allocate(0));
    }

    @Test void allocateByRatio() {
        assertEquals(List.of(34L, 33L, 33L), units(inr(100).allocate(1L, 1L, 1L)));
        assertEquals(List.of(60L, 40L), units(inr(100).allocate(3L, 2L)));
        assertEquals(List.of(0L, 100L), units(inr(100).allocate(0L, 5L)));
        assertEquals(List.of(-5L, -4L), units(inr(-9).allocate(1L, 1L)));
        assertThrows(IllegalArgumentException.class, () -> inr(5).allocate(0L, 0L));
        assertThrows(IllegalArgumentException.class, () -> inr(5).allocate(1L, -1L));
        assertThrows(IllegalArgumentException.class, () -> inr(5).allocate(new long[0]));
    }

    @Test void allocationAlwaysSumsToTheTotalAndSharesDifferByAtMostOne() {
        Random r = new Random(9);
        for (int t = 0; t < 1000; t++) {
            long total = r.nextLong() % 1_000_000_007L;
            int parts = 1 + r.nextInt(9);
            List<Money> l = inr(total).allocate(parts);
            assertEquals(total, l.stream().mapToLong(Money::minorUnits).sum());
            long max = l.stream().mapToLong(Money::minorUnits).max().getAsLong(), min = l.stream().mapToLong(Money::minorUnits).min().getAsLong();
            assertTrue(max - min <= 1);
            long[] ratios = r.longs(parts, 0, 50).toArray();
            if (Arrays.stream(ratios).sum() > 0)
                assertEquals(total, inr(total).allocate(ratios).stream().mapToLong(Money::minorUnits).sum());
        }
    }

    @Test void hugeTotalsDoNotOverflowIntermediateProduct() {
        List<Money> l = inr(Long.MAX_VALUE).allocate(1L << 40, 1L << 40);
        assertEquals(Long.MAX_VALUE, l.get(0).minorUnits() + l.get(1).minorUnits());
    }
}
