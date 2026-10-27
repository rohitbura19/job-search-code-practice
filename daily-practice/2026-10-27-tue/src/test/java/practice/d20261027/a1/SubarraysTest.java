package practice.d20261027.a1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SubarraysTest {
    @Test void sumExamples() {
        assertEquals(6, Subarrays.maxSubarray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        assertEquals(1, Subarrays.maxSubarray(new int[]{1}));
        assertEquals(23, Subarrays.maxSubarray(new int[]{5, 4, -1, 7, 8}));
    }

    @Test void sumAllNegativeTakesTheLeastNegative() {
        assertEquals(-1, Subarrays.maxSubarray(new int[]{-3, -1, -2}));
        assertEquals(-5, Subarrays.maxSubarray(new int[]{-5}));
    }

    @Test void productExamples() {
        assertEquals(6, Subarrays.maxProduct(new int[]{2, 3, -2, 4}));
        assertEquals(0, Subarrays.maxProduct(new int[]{-2, 0, -1}));
        assertEquals(24, Subarrays.maxProduct(new int[]{-2, 3, -4}));
    }

    @Test void productEdgeCases() {
        assertEquals(-2, Subarrays.maxProduct(new int[]{-2}));
        assertEquals(0, Subarrays.maxProduct(new int[]{0}));
        assertEquals(3, Subarrays.maxProduct(new int[]{-3, 0, 3}));
        assertEquals(4, Subarrays.maxProduct(new int[]{-1, -2, -1, -2}));
        assertEquals(1, Subarrays.maxProduct(new int[]{-1, -1}));
    }

    @Test void productKeepsLargeValuesWithoutOverflow() {
        assertEquals(1_000_000_000, Subarrays.maxProduct(new int[]{1000, 1000, 1000}));
        assertEquals(1_000_000_000, Subarrays.maxProduct(new int[]{-1000, -1000, 1000}));
    }

    @Test void randomAgainstBruteForce() {
        Random r = new Random(7);
        for (int t = 0; t < 2000; t++) {
            int[] a = new int[1 + r.nextInt(10)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(7) - 3;
            int bs = Integer.MIN_VALUE, bp = Integer.MIN_VALUE;
            for (int i = 0; i < a.length; i++) {
                int s = 0, p = 1;
                for (int j = i; j < a.length; j++) {
                    s += a[j];
                    p *= a[j];
                    bs = Math.max(bs, s);
                    bp = Math.max(bp, p);
                }
            }
            assertEquals(bs, Subarrays.maxSubarray(a));
            assertEquals(bp, Subarrays.maxProduct(a));
        }
    }

    @Test void largeInputIsLinear() {
        int[] a = new int[2_000_000];
        for (int i = 0; i < a.length; i++) a[i] = (i % 3 == 0) ? -1 : 1;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            Subarrays.maxSubarray(a);
            Subarrays.maxProduct(new int[]{1, -1, 1, 1, 0, 1});
        });
    }
}
