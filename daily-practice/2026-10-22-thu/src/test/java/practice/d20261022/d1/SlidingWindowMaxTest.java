package practice.d20261022.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SlidingWindowMaxTest {
    @Test void examples() {
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, SlidingWindowMax.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
        assertArrayEquals(new int[]{1}, SlidingWindowMax.maxSlidingWindow(new int[]{1}, 1));
        assertArrayEquals(new int[]{9, 11}, SlidingWindowMax.maxSlidingWindow(new int[]{9, 11}, 1));
    }

    @Test void edgeCases() {
        assertArrayEquals(new int[0], SlidingWindowMax.maxSlidingWindow(new int[0], 3));
        assertArrayEquals(new int[]{7, 7, 7}, SlidingWindowMax.maxSlidingWindow(new int[]{7, 7, 7, 7, 7}, 3));   // duplicates
        assertArrayEquals(new int[]{5}, SlidingWindowMax.maxSlidingWindow(new int[]{5, 4, 3, 2, 1}, 5));
        assertArrayEquals(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE},
                SlidingWindowMax.maxSlidingWindow(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0}, 2));
        assertArrayEquals(new int[]{-1, -1}, SlidingWindowMax.maxSlidingWindow(new int[]{-5, -1, -2}, 2));
        assertThrows(IllegalArgumentException.class, () -> SlidingWindowMax.maxSlidingWindow(new int[]{1, 2}, 0));
        assertThrows(IllegalArgumentException.class, () -> SlidingWindowMax.maxSlidingWindow(new int[]{1, 2}, 3));
    }

    @Test void randomAgainstBrute() {
        Random r = new Random(7);
        for (int t = 0; t < 1000; t++) {
            int n = 1 + r.nextInt(20), k = 1 + r.nextInt(n);
            int[] a = r.ints(n, -5, 6).toArray();
            int[] exp = new int[n - k + 1];
            for (int i = 0; i + k <= n; i++) {
                int m = Integer.MIN_VALUE;
                for (int j = i; j < i + k; j++) m = Math.max(m, a[j]);
                exp[i] = m;
            }
            assertArrayEquals(exp, SlidingWindowMax.maxSlidingWindow(a, k));
        }
    }

    @Test void largeInput() {
        int n = 1_000_000;
        int[] a = new Random(8).ints(n).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(n - 500_000 + 1, SlidingWindowMax.maxSlidingWindow(a, 500_000).length));
    }
}
