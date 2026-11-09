package practice.d20261109.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class KadaneFamilyTest {
    static long bruteSum(int[] a) {
        long best = Long.MIN_VALUE;
        for (int i = 0; i < a.length; i++) { long s = 0; for (int j = i; j < a.length; j++) { s += a[j]; best = Math.max(best, s); } }
        return best;
    }

    static long bruteProduct(int[] a) {
        long best = Long.MIN_VALUE;
        for (int i = 0; i < a.length; i++) { long p = 1; for (int j = i; j < a.length; j++) { p *= a[j]; best = Math.max(best, p); } }
        return best;
    }

    static long bruteCircular(int[] a) {
        int n = a.length;
        long best = Long.MIN_VALUE;
        for (int i = 0; i < n; i++) { long s = 0; for (int len = 1; len <= n; len++) { s += a[(i + len - 1) % n]; best = Math.max(best, s); } }
        return best;
    }

    @Test void maxSubarrayExamples() {
        assertEquals(6, KadaneFamily.maxSubarraySum(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        assertEquals(-1, KadaneFamily.maxSubarraySum(new int[] {-3, -1, -2}));
        assertEquals(7, KadaneFamily.maxSubarraySum(new int[] {7}));
        assertEquals(4_294_967_294L, KadaneFamily.maxSubarraySum(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}));
    }

    @Test void maxProductExamples() {
        assertEquals(6, KadaneFamily.maxProductSubarray(new int[] {2, 3, -2, 4}));
        assertEquals(0, KadaneFamily.maxProductSubarray(new int[] {-2, 0, -1}));
        assertEquals(24, KadaneFamily.maxProductSubarray(new int[] {-2, 3, -4}));
        assertEquals(-2, KadaneFamily.maxProductSubarray(new int[] {-2}));
        assertEquals(24, KadaneFamily.maxProductSubarray(new int[] {2, -5, -2, -4, 3}));
    }

    @Test void circularExamples() {
        assertEquals(3, KadaneFamily.maxCircularSubarraySum(new int[] {1, -2, 3, -2}));
        assertEquals(10, KadaneFamily.maxCircularSubarraySum(new int[] {5, -3, 5}));
        assertEquals(4, KadaneFamily.maxCircularSubarraySum(new int[] {3, -1, 2, -1}));
        assertEquals(-2, KadaneFamily.maxCircularSubarraySum(new int[] {-3, -2, -3}));
        assertEquals(-5, KadaneFamily.maxCircularSubarraySum(new int[] {-5}));
        assertEquals(10, KadaneFamily.maxCircularSubarraySum(new int[] {2, 3, 5}));
    }

    @Test void randomVsBrute() {
        Random r = new Random(21);
        for (int t = 0; t < 3000; t++) {
            int n = 1 + r.nextInt(9);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(9) - 4;
            String m = java.util.Arrays.toString(a);
            assertEquals(bruteSum(a), KadaneFamily.maxSubarraySum(a), m);
            assertEquals(bruteProduct(a), KadaneFamily.maxProductSubarray(a), m);
            assertEquals(bruteCircular(a), KadaneFamily.maxCircularSubarraySum(a), m);
        }
    }

    @Test void largeInput() {
        int n = 3_000_000;
        int[] a = new int[n];
        Random r = new Random(2);
        for (int i = 0; i < n; i++) a[i] = r.nextInt(3) - 1; // -1, 0, 1: products stay in range
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            KadaneFamily.maxSubarraySum(a);
            assertTrue(KadaneFamily.maxProductSubarray(a) <= 1);
            KadaneFamily.maxCircularSubarraySum(a);
        });
    }
}
