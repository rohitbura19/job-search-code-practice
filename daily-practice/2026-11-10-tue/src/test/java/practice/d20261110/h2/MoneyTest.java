package practice.d20261110.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import org.junit.jupiter.api.Test;

class MoneyTest {
    @Test void factoriesAndToString() {
        assertEquals("12.34 USD", Money.of(1234, "USD").toString());
        assertEquals("-0.05 USD", Money.of(-5, "USD").toString());
        assertEquals("0.00 USD", Money.zero("USD").toString());
        assertEquals("500 JPY", Money.of(500, "JPY").toString());
        assertEquals("1.234 KWD", Money.of(1234, "KWD").toString());
        assertThrows(IllegalArgumentException.class, () -> Money.of(1, "usd"));
        assertThrows(IllegalArgumentException.class, () -> Money.of(1, "XXQ"));
        assertThrows(IllegalArgumentException.class, () -> Money.of(1, null));
    }

    @Test void parse() {
        assertEquals(Money.of(1234, "USD"), Money.parse("12.34 USD"));
        assertEquals(Money.of(500, "USD"), Money.parse("5 USD"));
        assertEquals(Money.of(550, "USD"), Money.parse("5.5 USD"));
        assertEquals(Money.of(-5, "USD"), Money.parse("-0.05 USD"));
        assertEquals(Money.of(500, "JPY"), Money.parse("500 JPY"));
        assertEquals(Money.of(1234, "USD"), Money.parse("12.340 USD"));              // trailing zero is not lost precision
        for (String bad : new String[]{"1.234 USD", "12.5 JPY", "abc USD", "12.34", "12.34  USD", "12.34 usd", "", "1e2x USD", "99999999999999999999 USD"})
            assertThrows(IllegalArgumentException.class, () -> Money.parse(bad), bad);
        assertThrows(IllegalArgumentException.class, () -> Money.parse(null));
    }

    @Test void arithmetic() {
        Money a = Money.of(1000, "USD"), b = Money.of(250, "USD");
        assertEquals(Money.of(1250, "USD"), a.plus(b));
        assertEquals(Money.of(750, "USD"), a.minus(b));
        assertEquals(Money.of(-1000, "USD"), a.negate());
        assertTrue(b.minus(a).isNegative());
        assertEquals(Money.of(3000, "USD"), a.times(3));
        assertThrows(IllegalArgumentException.class, () -> a.plus(Money.of(1, "EUR")));
        assertThrows(IllegalArgumentException.class, () -> a.minus(Money.of(1, "EUR")));
        assertThrows(ArithmeticException.class, () -> Money.of(Long.MAX_VALUE, "USD").plus(Money.of(1, "USD")));
        assertThrows(ArithmeticException.class, () -> Money.of(Long.MIN_VALUE, "USD").minus(Money.of(1, "USD")));
        assertThrows(ArithmeticException.class, () -> Money.of(Long.MIN_VALUE, "USD").negate());
        assertThrows(ArithmeticException.class, () -> Money.of(Long.MAX_VALUE / 2 + 1, "USD").times(2));
    }

    @Test void timesWithRounding() {
        Money m = Money.of(1999, "USD");
        assertEquals(Money.of(150, "USD"), m.times(new BigDecimal("0.075"), RoundingMode.HALF_UP));   // 149.925 -> 150
        assertEquals(Money.of(149, "USD"), m.times(new BigDecimal("0.075"), RoundingMode.DOWN));
        assertEquals(Money.of(-150, "USD"), m.negate().times(new BigDecimal("0.075"), RoundingMode.HALF_UP));
        assertEquals(Money.of(-149, "USD"), m.negate().times(new BigDecimal("0.075"), RoundingMode.CEILING));
    }

    @Test void allocateSumsExactly() {
        assertEquals(List.of(Money.of(34, "USD"), Money.of(33, "USD"), Money.of(33, "USD")), Money.of(100, "USD").split(3));
        assertEquals(List.of(Money.of(50, "USD"), Money.of(50, "USD")), Money.of(100, "USD").allocate(1, 1));
        assertEquals(List.of(Money.of(5, "USD"), Money.of(3, "USD")), Money.of(8, "USD").allocate(3, 2));      // 4.8 / 3.2 -> floor 4,3 + 1 leftover to the first part
        assertEquals(List.of(Money.of(0, "USD"), Money.of(100, "USD")), Money.of(100, "USD").allocate(0, 7));
        assertEquals(List.of(Money.of(-34, "USD"), Money.of(-33, "USD"), Money.of(-33, "USD")), Money.of(-100, "USD").split(3));
        assertEquals(List.of(Money.of(1, "USD"), Money.of(1, "USD"), Money.of(0, "USD")), Money.of(2, "USD").split(3));
        assertEquals(List.of(Money.zero("USD")), Money.zero("USD").split(1));
    }

    @Test void allocateValidation() {
        Money m = Money.of(100, "USD");
        assertThrows(IllegalArgumentException.class, () -> m.allocate());
        assertThrows(IllegalArgumentException.class, () -> m.allocate(0, 0));
        assertThrows(IllegalArgumentException.class, () -> m.allocate(1, -1, 3));
        assertThrows(IllegalArgumentException.class, () -> m.split(0));
    }

    @Test void allocateRandomInvariants() {
        Random r = new Random(13);
        for (int t = 0; t < 2000; t++) {
            long amount = r.nextInt(2_000_001) - 1_000_000;
            long[] ratios = r.longs(1 + r.nextInt(6), 0, 50).toArray();
            if (Arrays.stream(ratios).sum() == 0) ratios[0] = 1;
            List<Money> parts = Money.of(amount, "USD").allocate(ratios);
            assertEquals(ratios.length, parts.size());
            assertEquals(amount, parts.stream().mapToLong(Money::minor).sum());
            long total = Arrays.stream(ratios).sum();
            for (int i = 0; i < ratios.length; i++) {
                double ideal = (double) amount * ratios[i] / total;
                assertTrue(Math.abs(parts.get(i).minor() - ideal) < 1.0 + 1e-9, "part " + i + " of " + amount + Arrays.toString(ratios));
                if (ratios[i] == 0) assertEquals(0, parts.get(i).minor());
            }
        }
    }

    @Test void allocateHugeAmountsDoNotOverflowIntermediate() {
        List<Money> p = Money.of(Long.MAX_VALUE, "USD").allocate(Long.MAX_VALUE / 2, Long.MAX_VALUE / 2);
        assertEquals(Long.MAX_VALUE, p.get(0).minor() + p.get(1).minor());
    }

    @Test void equalsHashCodeCompare() {
        Money a = Money.of(100, "USD"), b = Money.of(100, "USD");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, Money.of(100, "EUR"));
        assertNotEquals(a, Money.of(101, "USD"));
        assertNotEquals(a, null);
        assertNotEquals(a, "1.00 USD");
        assertEquals(1, Money.of(101, "USD").compareTo(a));
        assertEquals(0, a.compareTo(b));
        assertThrows(IllegalArgumentException.class, () -> a.compareTo(Money.of(1, "EUR")));
        assertEquals(1, new HashSet<>(List.of(a, b)).size());
        assertEquals(List.of(Money.of(-1, "USD"), a, Money.of(300, "USD")), new TreeSet<>(List.of(Money.of(300, "USD"), a, Money.of(-1, "USD"))).stream().toList());
    }

    @Test void immutable() {
        Money a = Money.of(100, "USD");
        List<Money> parts = a.split(2);
        assertThrows(UnsupportedOperationException.class, () -> parts.add(a));
        assertEquals(100, a.minor());
    }
}
