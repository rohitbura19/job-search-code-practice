package practice.d20261030.a1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class NumbersTest {
    @Test void sqrtExamples() {
        assertEquals(0, Numbers.sqrt(0));
        assertEquals(1, Numbers.sqrt(1));
        assertEquals(1, Numbers.sqrt(3));
        assertEquals(2, Numbers.sqrt(8));
        assertEquals(4, Numbers.sqrt(16));
        assertEquals(46340, Numbers.sqrt(Integer.MAX_VALUE));
        assertEquals(46340, Numbers.sqrt(46340 * 46340));
        assertEquals(46339, Numbers.sqrt(46340 * 46340 - 1));
    }

    @Test void sqrtRandomAgainstLongReference() {
        Random r = new Random(1);
        for (int i = 0; i < 20000; i++) {
            int x = i < 10000 ? r.nextInt(100000) : r.nextInt(Integer.MAX_VALUE);
            long s = (long) Math.sqrt(x);
            while (s * s > x) s--;
            while ((s + 1) * (s + 1) <= x) s++;
            assertEquals(s, Numbers.sqrt(x), "x=" + x);
        }
    }

    @Test void powExamples() {
        assertEquals(1024.0, Numbers.pow(2, 10), 0);
        assertEquals(0.25, Numbers.pow(2, -2), 0);
        assertEquals(1.0, Numbers.pow(7.5, 0), 0);
        assertEquals(-8.0, Numbers.pow(-2, 3), 0);
        assertEquals(0.0, Numbers.pow(0, 5), 0);
        assertEquals(9.261, Numbers.pow(2.1, 3), 1e-9);
    }

    @Test void powExtremeExponents() {
        assertEquals(1.0, Numbers.pow(1, Integer.MIN_VALUE), 0);
        assertEquals(1.0, Numbers.pow(-1, Integer.MIN_VALUE), 0);
        assertEquals(-1.0, Numbers.pow(-1, Integer.MAX_VALUE), 0);
        assertEquals(0.0, Numbers.pow(2, Integer.MIN_VALUE), 0);
        assertEquals(Double.POSITIVE_INFINITY, Numbers.pow(2, Integer.MAX_VALUE), 0);
    }

    @Test @Timeout(2) void powRandomAgainstMath() {
        Random r = new Random(2);
        for (int i = 0; i < 20000; i++) {
            double x = 0.5 + r.nextDouble();
            int n = r.nextInt(101) - 50;
            double expected = Math.pow(x, n);
            assertEquals(expected, Numbers.pow(x, n), Math.abs(expected) * 1e-9, x + "^" + n);
        }
        double big = Math.pow(1.0000001, Integer.MAX_VALUE);
        assertEquals(big, Numbers.pow(1.0000001, Integer.MAX_VALUE), big * 1e-5);
    }

    @Test void reverseExamples() {
        assertEquals(321, Numbers.reverse(123));
        assertEquals(-321, Numbers.reverse(-123));
        assertEquals(21, Numbers.reverse(120));
        assertEquals(0, Numbers.reverse(0));
        assertEquals(7, Numbers.reverse(7));
    }

    @Test void reverseOverflow() {
        assertEquals(0, Numbers.reverse(1534236469));
        assertEquals(0, Numbers.reverse(-1563847412));
        assertEquals(0, Numbers.reverse(Integer.MIN_VALUE));
        assertEquals(0, Numbers.reverse(Integer.MAX_VALUE));
        assertEquals(1463847412, Numbers.reverse(2147483641));  // fits
        assertEquals(-2147483412, Numbers.reverse(-2143847412)); // fits, and is near MIN_VALUE
    }

    @Test void reverseRandomAgainstLong() {
        Random r = new Random(3);
        for (int i = 0; i < 50000; i++) {
            int x = r.nextInt();
            long rev = 0, v = x;
            while (v != 0) { rev = rev * 10 + v % 10; v /= 10; }
            int expected = rev > Integer.MAX_VALUE || rev < Integer.MIN_VALUE ? 0 : (int) rev;
            assertEquals(expected, Numbers.reverse(x), "x=" + x);
        }
    }
}
