package practice.d20261023.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class HistogramTest {
    static long brute(int[] h) {
        long best = 0;
        for (int i = 0; i < h.length; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = i; j < h.length; j++) {
                min = Math.min(min, h[j]);
                best = Math.max(best, (long) min * (j - i + 1));
            }
        }
        return best;
    }

    @Test void examples() {
        assertEquals(10, Histogram.largestRectangle(new int[]{2, 1, 5, 6, 2, 3}));
        assertEquals(4, Histogram.largestRectangle(new int[]{2, 4}));
        assertEquals(0, Histogram.largestRectangle(new int[]{}));
        assertEquals(7, Histogram.largestRectangle(new int[]{7}));
        assertEquals(0, Histogram.largestRectangle(new int[]{0, 0}));
    }

    @Test void shapes() {
        assertEquals(12, Histogram.largestRectangle(new int[]{3, 3, 3, 3}));                 // equal bars merge
        assertEquals(9, Histogram.largestRectangle(new int[]{1, 2, 3, 4, 5}));               // 3 x 3
        assertEquals(9, Histogram.largestRectangle(new int[]{5, 4, 3, 2, 1}));
        assertEquals(12, Histogram.largestRectangle(new int[]{6, 2, 5, 4, 5, 1, 6}));
    }

    @Test void areaDoesNotOverflowInt() {
        int[] h = new int[100_000];
        Arrays.fill(h, 1_000_000_000);
        assertEquals(100_000L * 1_000_000_000L, Histogram.largestRectangle(h));
    }

    @Test void randomAgainstQuadratic() {
        Random rnd = new Random(2);
        for (int t = 0; t < 2000; t++) {
            int[] h = new int[rnd.nextInt(20)];
            for (int i = 0; i < h.length; i++) h[i] = rnd.nextInt(8);
            assertEquals(brute(h), Histogram.largestRectangle(h), Arrays.toString(h));
        }
    }

    @Test void largeInput() {
        int[] h = new int[1_000_000];
        for (int i = 0; i < h.length; i++) h[i] = i;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(250_000_000_000L, Histogram.largestRectangle(h)));
    }
}
