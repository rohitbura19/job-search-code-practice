package practice.d20261107.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SlidingWindowMedianTest {
    @Test void examples() {
        assertArrayEquals(new double[]{1, -1, -1, 3, 5, 6}, SlidingWindowMedian.medianSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
        assertArrayEquals(new double[]{2, 3, 3, 3, 2, 3, 2}, SlidingWindowMedian.medianSlidingWindow(new int[]{1, 2, 3, 4, 2, 3, 1, 4, 2}, 3));
        assertArrayEquals(new double[]{1.5, 2.5, 3.5}, SlidingWindowMedian.medianSlidingWindow(new int[]{1, 2, 3, 4}, 2), 0);
    }

    @Test void edgeCases() {
        assertArrayEquals(new double[]{7}, SlidingWindowMedian.medianSlidingWindow(new int[]{7}, 1), 0);
        assertArrayEquals(new double[]{4, 4, 4}, SlidingWindowMedian.medianSlidingWindow(new int[]{4, 4, 4}, 1), 0);
        assertArrayEquals(new double[]{5, 5}, SlidingWindowMedian.medianSlidingWindow(new int[]{5, 5, 5, 5}, 3), 0);
        assertArrayEquals(new double[]{2}, SlidingWindowMedian.medianSlidingWindow(new int[]{1, 3, 2}, 3), 0);
    }

    @Test void overflow() {
        double max = Integer.MAX_VALUE, min = Integer.MIN_VALUE;
        assertArrayEquals(new double[]{max}, SlidingWindowMedian.medianSlidingWindow(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE}, 2), 0);
        assertArrayEquals(new double[]{-0.5}, SlidingWindowMedian.medianSlidingWindow(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, 2), 0);
        assertArrayEquals(new double[]{min}, SlidingWindowMedian.medianSlidingWindow(new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE}, 2), 0);
    }

    @Test void randomVsSort() {
        Random r = new Random(12);
        for (int t = 0; t < 3000; t++) {
            int n = 1 + r.nextInt(15), k = 1 + r.nextInt(n);
            int[] a = r.ints(n, -4, 5).toArray();
            assertArrayEquals(brute(a, k), SlidingWindowMedian.medianSlidingWindow(a, k), 0, Arrays.toString(a) + " k=" + k);
        }
    }

    @Test void large() {
        int[] a = new Random(13).ints(200_000, Integer.MIN_VALUE, Integer.MAX_VALUE).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(200_000 - 5_000 + 1, SlidingWindowMedian.medianSlidingWindow(a, 5_000).length));
        int[] dup = new int[200_000];
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> SlidingWindowMedian.medianSlidingWindow(dup, 1_001));
    }

    private static double[] brute(int[] a, int k) {
        double[] out = new double[a.length - k + 1];
        for (int i = 0; i + k <= a.length; i++) {
            int[] w = Arrays.copyOfRange(a, i, i + k);
            Arrays.sort(w);
            out[i] = k % 2 == 1 ? w[k / 2] : ((double) w[k / 2 - 1] + w[k / 2]) / 2;
        }
        return out;
    }
}
