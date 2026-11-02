package practice.d20261102.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class BitsAndStringsTest {
    @Test void excelExamples() {
        assertEquals("A", BitsAndStrings.excelTitle(1));
        assertEquals("Z", BitsAndStrings.excelTitle(26));
        assertEquals("AA", BitsAndStrings.excelTitle(27));
        assertEquals("AZ", BitsAndStrings.excelTitle(52));
        assertEquals("ZY", BitsAndStrings.excelTitle(701));
        assertEquals("FXSHRXW", BitsAndStrings.excelTitle(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, BitsAndStrings.excelNumber("FXSHRXW"));
        assertEquals(1, BitsAndStrings.excelNumber("A"));
        assertEquals(703, BitsAndStrings.excelNumber("AAA"));
    }

    @Test void excelRoundTrip() {
        for (int n = 1; n <= 20_000; n++) assertEquals(n, BitsAndStrings.excelNumber(BitsAndStrings.excelTitle(n)));
        Random r = new Random(1);
        for (int i = 0; i < 5000; i++) {
            int n = 1 + r.nextInt(Integer.MAX_VALUE - 1);
            assertEquals(n, BitsAndStrings.excelNumber(BitsAndStrings.excelTitle(n)));
        }
    }

    @Test void addBinaryExamples() {
        assertEquals("100", BitsAndStrings.addBinary("11", "1"));
        assertEquals("10101", BitsAndStrings.addBinary("1010", "1011"));
        assertEquals("0", BitsAndStrings.addBinary("0", "0"));
        assertEquals("0", BitsAndStrings.addBinary("000", "0"));
        assertEquals("10", BitsAndStrings.addBinary("001", "1"));
        assertEquals("1", BitsAndStrings.addBinary("0001", "000"));
    }

    @Test void addBinaryRandomAgainstBigInteger() {
        Random r = new Random(2);
        for (int t = 0; t < 500; t++) {
            String a = bits(r, 1 + r.nextInt(200)), b = bits(r, 1 + r.nextInt(200));
            String expected = new BigInteger(a, 2).add(new BigInteger(b, 2)).toString(2);
            assertEquals(expected, BitsAndStrings.addBinary(a, b), a + " + " + b);
        }
    }

    @Test void addBinaryLargeWithinTimeout() {
        String ones = "1".repeat(100_000);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () ->
                assertEquals("1" + "0".repeat(100_000), BitsAndStrings.addBinary(ones, "1")));
    }

    @Test void countBitsExamples() {
        assertArrayEquals(new int[]{0}, BitsAndStrings.countBits(0));
        assertArrayEquals(new int[]{0, 1, 1, 2, 1, 2}, BitsAndStrings.countBits(5));
    }

    @Test void countBitsAgainstJdkAndLarge() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            int[] c = BitsAndStrings.countBits(5_000_000);
            assertEquals(5_000_001, c.length);
            for (int i = 0; i < c.length; i += 997) assertEquals(Integer.bitCount(i), c[i]);
            assertEquals(Integer.bitCount(5_000_000), c[5_000_000]);
        });
    }

    private static String bits(Random r, int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) sb.append(r.nextBoolean() ? '1' : '0');
        return sb.toString();
    }
}
