package practice.d20261109.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class NumberBasicsTest {
    private static long refGcd(long a, long b) { return b == 0 ? a : refGcd(b, a % b); }

    @Test void gcdAndLcm() {
        assertEquals(6, NumberBasics.gcd(12, 18));
        assertEquals(1, NumberBasics.gcd(17, 5));
        assertEquals(5, NumberBasics.gcd(0, 5));
        assertEquals(5, NumberBasics.gcd(5, 0));
        assertEquals(0, NumberBasics.gcd(0, 0));
        assertEquals(Integer.MAX_VALUE, NumberBasics.gcd(Integer.MAX_VALUE, Integer.MAX_VALUE));
        assertEquals(36, NumberBasics.lcm(12, 18));
        assertEquals(0, NumberBasics.lcm(0, 9));
        assertEquals(0, NumberBasics.lcm(9, 0));
        assertEquals((long) Integer.MAX_VALUE * (Integer.MAX_VALUE - 1), NumberBasics.lcm(Integer.MAX_VALUE, Integer.MAX_VALUE - 1));
    }

    @Test void gcdRandom() {
        Random r = new Random(1);
        for (int i = 0; i < 2000; i++) {
            int a = r.nextInt(100_000), b = r.nextInt(100_000);
            assertEquals(refGcd(a, b), NumberBasics.gcd(a, b));
            if (a > 0 && b > 0) assertEquals((long) a / refGcd(a, b) * b, NumberBasics.lcm(a, b));
        }
    }

    @Test void happyNumbers() {
        assertTrue(NumberBasics.isHappy(1));
        assertTrue(NumberBasics.isHappy(19));
        assertTrue(NumberBasics.isHappy(7));
        assertTrue(NumberBasics.isHappy(100));
        assertFalse(NumberBasics.isHappy(2));
        assertFalse(NumberBasics.isHappy(4));
        assertFalse(NumberBasics.isHappy(116));
        assertFalse(NumberBasics.isHappy(Integer.MAX_VALUE));
        int happyBelow50 = 0;
        for (int i = 1; i < 50; i++) if (NumberBasics.isHappy(i)) happyBelow50++;
        assertEquals(11, happyBelow50); // 1 7 10 13 19 23 28 31 32 44 49
    }

    @Test void excelColumns() {
        assertEquals("A", NumberBasics.excelTitle(1));
        assertEquals("Z", NumberBasics.excelTitle(26));
        assertEquals("AA", NumberBasics.excelTitle(27));
        assertEquals("AZ", NumberBasics.excelTitle(52));
        assertEquals("ZZ", NumberBasics.excelTitle(702));
        assertEquals("AAA", NumberBasics.excelTitle(703));
        assertEquals("FXSHRXW", NumberBasics.excelTitle(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, NumberBasics.excelNumber("FXSHRXW"));
        assertEquals(701, NumberBasics.excelNumber("ZY"));
        for (int n = 1; n < 20_000; n++) assertEquals(n, NumberBasics.excelNumber(NumberBasics.excelTitle(n)));
    }

    @Test void countPrimes() {
        assertEquals(0, NumberBasics.countPrimes(0));
        assertEquals(0, NumberBasics.countPrimes(2));
        assertEquals(1, NumberBasics.countPrimes(3));
        assertEquals(4, NumberBasics.countPrimes(10));
        assertEquals(25, NumberBasics.countPrimes(100));
        assertEquals(9592, NumberBasics.countPrimes(100_000));
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(348_513, NumberBasics.countPrimes(5_000_000)));
    }
}
