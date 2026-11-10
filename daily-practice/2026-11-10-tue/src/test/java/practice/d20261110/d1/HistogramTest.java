package practice.d20261110.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class HistogramTest {
    @Test void examples() {
        assertEquals(10, Histogram.largestRectangle(new int[]{2, 1, 5, 6, 2, 3}));
        assertEquals(4, Histogram.largestRectangle(new int[]{2, 4}));
        assertEquals(0, Histogram.largestRectangle(new int[]{}));
        assertEquals(7, Histogram.largestRectangle(new int[]{7}));
        assertEquals(0, Histogram.largestRectangle(new int[]{0, 0}));
        assertEquals(12, Histogram.largestRectangle(new int[]{3, 3, 3, 3}));
        assertEquals(12, Histogram.largestRectangle(new int[]{6, 2, 5, 4, 5, 1, 6}));
    }

    @Test void overflowNeedsLong() {
        int[] a = new int[100_000];
        Arrays.fill(a, 1_000_000_000);
        assertEquals(100_000L * 1_000_000_000L, Histogram.largestRectangle(a));
        assertEquals(2_000_000_000L, Histogram.largestRectangle(new int[]{1_000_000_000, 1_000_000_000}));
    }

    private static long brute(int[] h) {
        long best = 0;
        for (int i = 0; i < h.length; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = i; j < h.length; j++) { min = Math.min(min, h[j]); best = Math.max(best, (long) min * (j - i + 1)); }
        }
        return best;
    }

    @Test void randomAgainstBruteForce() {
        Random r = new Random(11);
        for (int t = 0; t < 1000; t++) {
            int[] h = r.ints(r.nextInt(15), 0, t % 2 == 0 ? 4 : 30).toArray();
            assertEquals(brute(h), Histogram.largestRectangle(h), Arrays.toString(h));
        }
    }

    @Test void maximalRectangle() {
        assertEquals(6, Histogram.maximalRectangle(new char[][]{"10100".toCharArray(), "10111".toCharArray(), "11111".toCharArray(), "10010".toCharArray()}));
        assertEquals(0, Histogram.maximalRectangle(new char[][]{"0".toCharArray()}));
        assertEquals(0, Histogram.maximalRectangle(new char[0][]));
        assertEquals(4, Histogram.maximalRectangle(new char[][]{"1111".toCharArray()}));
    }

    @Test void largeInputsWithinTimeout() {
        int n = 1_000_000;
        int[] asc = new int[n], desc = new int[n], rnd = new Random(12).ints(n, 0, 1_000_000_000).toArray();
        for (int i = 0; i < n; i++) { asc[i] = i; desc[i] = n - i; }
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(brute(new int[]{1, 2, 3, 4}), Histogram.largestRectangle(new int[]{1, 2, 3, 4}));
            assertEquals(250_000_000_000L, Histogram.largestRectangle(asc));        // max over i of i * (n - i)
            assertEquals(250_000_500_000L, Histogram.largestRectangle(desc));       // max over k of (n - k) * (k + 1)
            Histogram.largestRectangle(rnd);
        });
    }
}
