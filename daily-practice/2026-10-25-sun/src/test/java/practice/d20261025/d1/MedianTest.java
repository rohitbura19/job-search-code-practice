package practice.d20261025.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class MedianTest {
    static double brute(int[] a, int[] b) {
        long[] all = new long[a.length + b.length];
        int k = 0;
        for (int x : a) all[k++] = x;
        for (int x : b) all[k++] = x;
        Arrays.sort(all);
        int n = all.length;
        return n % 2 == 1 ? all[n / 2] : (all[n / 2 - 1] + all[n / 2]) / 2.0;
    }

    @Test void examples() {
        assertEquals(2.0, Median.findMedian(new int[]{1, 3}, new int[]{2}), 1e-9);
        assertEquals(2.5, Median.findMedian(new int[]{1, 2}, new int[]{3, 4}), 1e-9);
        assertEquals(1.0, Median.findMedian(new int[]{}, new int[]{1}), 1e-9);
        assertEquals(2.5, Median.findMedian(new int[]{2, 3}, new int[]{}), 1e-9);
        assertEquals(0.0, Median.findMedian(new int[]{0, 0}, new int[]{0, 0}), 1e-9);
    }

    @Test void bothEmptyIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Median.findMedian(new int[]{}, new int[]{}));
    }

    @Test void extremesDoNotOverflow() {
        assertEquals(-0.5, Median.findMedian(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}, new int[]{}), 1e-9);
        assertEquals((double) Integer.MAX_VALUE, Median.findMedian(new int[]{Integer.MAX_VALUE}, new int[]{Integer.MAX_VALUE}), 1e-9);
        assertEquals(brute(new int[]{Integer.MIN_VALUE}, new int[]{Integer.MIN_VALUE, 5, 6}),
                Median.findMedian(new int[]{Integer.MIN_VALUE}, new int[]{Integer.MIN_VALUE, 5, 6}), 1e-6);
        assertEquals(brute(new int[]{Integer.MIN_VALUE, -5}, new int[]{7, Integer.MAX_VALUE}),
                Median.findMedian(new int[]{Integer.MIN_VALUE, -5}, new int[]{7, Integer.MAX_VALUE}), 1e-9);
    }

    @Test void disjointRangesAndDuplicates() {
        assertEquals(5.5, Median.findMedian(new int[]{1, 2, 3, 4, 5}, new int[]{6, 7, 8, 9, 10}), 1e-9);
        assertEquals(5.5, Median.findMedian(new int[]{6, 7, 8, 9, 10}, new int[]{1, 2, 3, 4, 5}), 1e-9);
        assertEquals(3.0, Median.findMedian(new int[]{3, 3, 3}, new int[]{3, 3}), 1e-9);
    }

    @Test void matchesBruteForce() {
        Random rnd = new Random(12);
        for (int it = 0; it < 20000; it++) {
            int[] a = rnd.ints(rnd.nextInt(8), -6, 7).sorted().toArray(), b = rnd.ints(rnd.nextInt(8), -6, 7).sorted().toArray();
            if (a.length + b.length == 0) continue;
            assertEquals(brute(a, b), Median.findMedian(a, b), 1e-9, Arrays.toString(a) + Arrays.toString(b));
        }
    }

    @Test void largeInput() {
        Random rnd = new Random(3);
        int[] a = rnd.ints(3_000_000, Integer.MIN_VALUE, Integer.MAX_VALUE).sorted().toArray();
        int[] b = rnd.ints(10, -5, 5).sorted().toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            for (int i = 0; i < 100_000; i++) Median.findMedian(a, b);
            assertEquals(brute(a, b), Median.findMedian(a, b), 1e-6);
        });
    }
}
