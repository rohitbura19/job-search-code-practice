package practice.d20261102.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class SlidingWindowTest {
    @Test void examples() {
        assertArrayEquals(new int[]{3, 3, 5, 5, 6, 7}, SlidingWindow.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
        assertArrayEquals(new int[]{1}, SlidingWindow.maxSlidingWindow(new int[]{1}, 1));
        assertArrayEquals(new int[]{9, 11}, SlidingWindow.maxSlidingWindow(new int[]{9, 11}, 1));
        assertArrayEquals(new int[]{11}, SlidingWindow.maxSlidingWindow(new int[]{9, 11}, 2));
        assertArrayEquals(new int[]{}, SlidingWindow.maxSlidingWindow(new int[]{}, 1));
    }

    @Test void duplicatesAndExtremes() {
        assertArrayEquals(new int[]{2, 2, 2}, SlidingWindow.maxSlidingWindow(new int[]{2, 2, 2, 2, 2}, 3));
        assertArrayEquals(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, 0},
                SlidingWindow.maxSlidingWindow(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, 0}, 2));
        assertArrayEquals(new int[]{5, 4, 3, 2, 1}, SlidingWindow.maxSlidingWindow(new int[]{5, 4, 3, 2, 1}, 1));
        assertArrayEquals(new int[]{3, 4, 5}, SlidingWindow.maxSlidingWindow(new int[]{1, 2, 3, 4, 5}, 3));
    }

    @Test void randomAgainstBruteForce() {
        Random r = new Random(13);
        for (int t = 0; t < 500; t++) {
            int n = 1 + r.nextInt(40);
            int[] a = r.ints(n, -10, 10).toArray();
            int k = 1 + r.nextInt(n);
            int[] exp = new int[n - k + 1];
            for (int i = 0; i < exp.length; i++) {
                int m = Integer.MIN_VALUE;
                for (int j = i; j < i + k; j++) m = Math.max(m, a[j]);
                exp[i] = m;
            }
            assertArrayEquals(exp, SlidingWindow.maxSlidingWindow(a, k));
        }
    }

    @Test void largeInputsWithinTimeout() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            int n = 1_000_000;
            int[] inc = new int[n], dec = new int[n];
            for (int i = 0; i < n; i++) { inc[i] = i; dec[i] = n - i; }
            int[] r1 = SlidingWindow.maxSlidingWindow(inc, 500_000);
            assertEquals(n - 1, r1[r1.length - 1]);
            assertEquals(n, SlidingWindow.maxSlidingWindow(dec, 500_000)[0]);
            int[] rnd = new Random(14).ints(n).toArray();
            assertEquals(n, SlidingWindow.maxSlidingWindow(rnd, 1).length);
            assertEquals(1, SlidingWindow.maxSlidingWindow(rnd, n).length);
        });
    }
}
