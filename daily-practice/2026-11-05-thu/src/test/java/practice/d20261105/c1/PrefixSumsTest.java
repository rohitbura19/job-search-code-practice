package practice.d20261105.c1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class PrefixSumsTest {
    @Test void sumEqualsKExamples() {
        assertEquals(2, PrefixSums.subarraySumEqualsK(new int[] {1, 1, 1}, 2));
        assertEquals(2, PrefixSums.subarraySumEqualsK(new int[] {1, 2, 3}, 3));
        assertEquals(3, PrefixSums.subarraySumEqualsK(new int[] {1, -1, 0}, 0));
        assertEquals(0, PrefixSums.subarraySumEqualsK(new int[] {}, 0));
        assertEquals(1, PrefixSums.subarraySumEqualsK(new int[] {5}, 5));
        assertEquals(10, PrefixSums.subarraySumEqualsK(new int[] {0, 0, 0, 0}, 0));
    }

    @Test void sumEqualsKOverflow() {
        int[] a = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE};
        assertEquals(1, PrefixSums.subarraySumEqualsK(a, 2L * Integer.MAX_VALUE));
        assertEquals(1, PrefixSums.subarraySumEqualsK(a, 2L * Integer.MAX_VALUE + Integer.MIN_VALUE));
    }

    @Test void sumEqualsKBrute() {
        Random r = new Random(6);
        for (int t = 0; t < 500; t++) {
            int[] a = new int[r.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(7) - 3;
            long k = r.nextInt(7) - 3, expected = 0;
            for (int i = 0; i < a.length; i++) { long s = 0; for (int j = i; j < a.length; j++) { s += a[j]; if (s == k) expected++; } }
            assertEquals(expected, PrefixSums.subarraySumEqualsK(a, k));
        }
    }

    @Test void balancedExamples() {
        assertEquals(2, PrefixSums.longestBalancedBinary(new int[] {0, 1}));
        assertEquals(2, PrefixSums.longestBalancedBinary(new int[] {0, 1, 0}));
        assertEquals(6, PrefixSums.longestBalancedBinary(new int[] {0, 0, 1, 0, 0, 0, 1, 1}));
        assertEquals(0, PrefixSums.longestBalancedBinary(new int[] {1, 1, 1}));
        assertEquals(0, PrefixSums.longestBalancedBinary(new int[] {}));
        assertEquals(0, PrefixSums.longestBalancedBinary(new int[] {0}));
        assertEquals(4, PrefixSums.longestBalancedBinary(new int[] {1, 1, 0, 0}));
    }

    @Test void balancedBrute() {
        Random r = new Random(7);
        for (int t = 0; t < 500; t++) {
            int[] a = new int[r.nextInt(14)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(2);
            int best = 0;
            for (int i = 0; i < a.length; i++) { int bal = 0; for (int j = i; j < a.length; j++) { bal += a[j] == 1 ? 1 : -1; if (bal == 0) best = Math.max(best, j - i + 1); } }
            assertEquals(best, PrefixSums.longestBalancedBinary(a));
        }
    }

    @Test void divisibleExamples() {
        assertEquals(7, PrefixSums.subarraysDivisibleByK(new int[] {4, 5, 0, -2, -3, 1}, 5));
        assertEquals(0, PrefixSums.subarraysDivisibleByK(new int[] {5}, 9));
        assertEquals(1, PrefixSums.subarraysDivisibleByK(new int[] {5}, 5));
        assertEquals(6, PrefixSums.subarraysDivisibleByK(new int[] {1, 1, 1}, 1));
        assertEquals(0, PrefixSums.subarraysDivisibleByK(new int[] {}, 3));
        assertEquals(1, PrefixSums.subarraysDivisibleByK(new int[] {-1}, 1));
        assertEquals(0, PrefixSums.subarraysDivisibleByK(new int[] {-1}, 7));
    }

    @Test void divisibleBruteWithNegativesAndBigK() {
        Random r = new Random(8);
        for (int t = 0; t < 500; t++) {
            int[] a = new int[r.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(21) - 10;
            int k = 1 + r.nextInt(9);
            long expected = 0;
            for (int i = 0; i < a.length; i++) { long s = 0; for (int j = i; j < a.length; j++) { s += a[j]; if (s % k == 0) expected++; } }
            assertEquals(expected, PrefixSums.subarraysDivisibleByK(a, k));
        }
        assertEquals(3, PrefixSums.subarraysDivisibleByK(new int[] {Integer.MIN_VALUE, Integer.MIN_VALUE}, 1 << 20));
    }

    @Test void largeInputIsLinear() {
        int[] a = new int[1_000_000];
        for (int i = 0; i < a.length; i++) a[i] = i % 5 - 2;
        int[] bin = new int[a.length];
        for (int i = 0; i < bin.length; i++) bin[i] = (i * 7 + i / 3) % 2;
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            PrefixSums.subarraySumEqualsK(a, 0);
            PrefixSums.longestBalancedBinary(bin);
            PrefixSums.subarraysDivisibleByK(a, 1000);
        });
    }
}
