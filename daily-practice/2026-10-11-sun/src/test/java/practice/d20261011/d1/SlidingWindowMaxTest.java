package practice.d20261011.d1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class SlidingWindowMaxTest {

    static String s(int[] a) { return Arrays.toString(a); }

    @Test @DisplayName("example from the question")
    void example() {
        assertEquals("[3, 3, 5, 5, 6, 7]", s(SlidingWindowMax.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
    }

    @Test @DisplayName("k = 1, k = n, single element, empty")
    void edges() {
        assertEquals("[4, -2, 9]", s(SlidingWindowMax.maxSlidingWindow(new int[]{4, -2, 9}, 1)));
        assertEquals("[9]", s(SlidingWindowMax.maxSlidingWindow(new int[]{4, -2, 9}, 3)));
        assertEquals("[7]", s(SlidingWindowMax.maxSlidingWindow(new int[]{7}, 1)));
        assertEquals("[]", s(SlidingWindowMax.maxSlidingWindow(new int[]{}, 1)));
    }

    @Test @DisplayName("duplicates, negatives, monotonic runs, extreme values")
    void shapes() {
        assertEquals("[2, 2, 2]", s(SlidingWindowMax.maxSlidingWindow(new int[]{2, 2, 2, 2, 2}, 3)));
        assertEquals("[-1, -2, -3]", s(SlidingWindowMax.maxSlidingWindow(new int[]{-1, -2, -3, -4, -5}, 3)));
        assertEquals("[3, 4, 5]", s(SlidingWindowMax.maxSlidingWindow(new int[]{1, 2, 3, 4, 5}, 3)));
        assertEquals("[5, 4, 3]", s(SlidingWindowMax.maxSlidingWindow(new int[]{5, 4, 3, 2, 1}, 3)));
        assertEquals("[2147483647, 2147483647]", s(SlidingWindowMax.maxSlidingWindow(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE}, 2)));
        assertEquals("[3, 3, 2, 5]", s(SlidingWindowMax.maxSlidingWindow(new int[]{1, 3, 1, 2, 0, 5}, 3)));
    }

    @Test @DisplayName("random arrays match the O(n*k) brute force")
    void randomAgainstBruteForce() {
        Random rnd = new Random(9);
        for (int t = 0; t < 3000; t++) {
            int n = rnd.nextInt(14) + 1; int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = rnd.nextInt(11) - 5;
            int k = rnd.nextInt(n) + 1;
            int[] expect = new int[n - k + 1];
            for (int i = 0; i + k <= n; i++) { int m = Integer.MIN_VALUE; for (int j = i; j < i + k; j++) m = Math.max(m, a[j]); expect[i] = m; }
            assertEquals(s(expect), s(SlidingWindowMax.maxSlidingWindow(a, k)), s(a) + " k=" + k);
        }
    }

    @Test @DisplayName("1 million elements with k = 50000 must be O(n), not O(n*k)")
    void largeInput() {
        int n = 1_000_000, k = 50_000; int[] a = new int[n]; Random rnd = new Random(2);
        for (int i = 0; i < n; i++) a[i] = rnd.nextInt();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            int[] r = SlidingWindowMax.maxSlidingWindow(a, k);
            assertEquals(n - k + 1, r.length);
            int m = Integer.MIN_VALUE; for (int j = 0; j < k; j++) m = Math.max(m, a[j]);
            assertEquals(m, r[0]);
        });
    }
}
