package practice.d20261025.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class PrefixTest {
    @Test void productExamples() {
        assertArrayEquals(new int[]{24, 12, 8, 6}, Prefix.productExceptSelf(new int[]{1, 2, 3, 4}));
        assertArrayEquals(new int[]{0, 0, 9, 0, 0}, Prefix.productExceptSelf(new int[]{-1, 1, 0, -3, 3}));
        assertArrayEquals(new int[]{0, 0, 0}, Prefix.productExceptSelf(new int[]{0, 0, 5}));
        assertArrayEquals(new int[]{}, Prefix.productExceptSelf(new int[]{}));
        assertArrayEquals(new int[]{1}, Prefix.productExceptSelf(new int[]{7}), "empty product is 1");
        assertArrayEquals(new int[]{3, 2}, Prefix.productExceptSelf(new int[]{2, 3}));
    }

    @Test void productMatchesBruteForce() {
        Random rnd = new Random(2);
        for (int it = 0; it < 3000; it++) {
            int[] a = rnd.ints(rnd.nextInt(9), -3, 4).toArray();
            int[] expected = new int[a.length];
            for (int i = 0; i < a.length; i++) {
                int p = 1;
                for (int j = 0; j < a.length; j++) if (j != i) p *= a[j];
                expected[i] = p;
            }
            assertArrayEquals(expected, Prefix.productExceptSelf(a), Arrays.toString(a));
        }
    }

    @Test void subarraySumExamples() {
        assertEquals(2, Prefix.subarraySum(new int[]{1, 1, 1}, 2));
        assertEquals(2, Prefix.subarraySum(new int[]{1, 2, 3}, 3));
        assertEquals(0, Prefix.subarraySum(new int[]{}, 0));
        assertEquals(1, Prefix.subarraySum(new int[]{5}, 5));
        assertEquals(0, Prefix.subarraySum(new int[]{5}, 0));
        assertEquals(6, Prefix.subarraySum(new int[]{0, 0, 0}, 0), "n(n+1)/2 zero-sum subarrays");
        assertEquals(4, Prefix.subarraySum(new int[]{1, -1, 1, -1}, 0));
    }

    @Test void subarraySumOverflowNeedsLong() {
        int[] a = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE};
        assertEquals(1, Prefix.subarraySum(a, 2L * Integer.MAX_VALUE));
        assertEquals(1, Prefix.subarraySum(a, Integer.MAX_VALUE + (long) Integer.MIN_VALUE + Integer.MAX_VALUE));
    }

    @Test void subarraySumMatchesBruteForce() {
        Random rnd = new Random(4);
        for (int it = 0; it < 3000; it++) {
            int[] a = rnd.ints(rnd.nextInt(14), -3, 4).toArray();
            int k = rnd.nextInt(7) - 3, expected = 0;
            for (int i = 0; i < a.length; i++) {
                long s = 0;
                for (int j = i; j < a.length; j++) { s += a[j]; if (s == k) expected++; }
            }
            assertEquals(expected, Prefix.subarraySum(a, k), Arrays.toString(a) + " k=" + k);
        }
    }

    @Test void largeInput() {
        Random rnd = new Random(8);
        int[] a = rnd.ints(2_000_000, 1, 4).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(2_000_000, Prefix.productExceptSelf(a).length);
            assertTrue(Prefix.subarraySum(a, 1000) > 0);
        });
    }
}
