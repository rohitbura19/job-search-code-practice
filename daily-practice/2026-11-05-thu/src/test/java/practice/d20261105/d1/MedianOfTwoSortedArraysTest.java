package practice.d20261105.d1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class MedianOfTwoSortedArraysTest {
    static double brute(int[] a, int[] b) {
        long[] all = new long[a.length + b.length];
        int k = 0;
        for (int v : a) all[k++] = v;
        for (int v : b) all[k++] = v;
        Arrays.sort(all);
        int n = all.length;
        return n % 2 == 1 ? all[n / 2] : (all[n / 2 - 1] + all[n / 2]) / 2.0;
    }

    @Test void examples() {
        assertEquals(2.0, MedianOfTwoSortedArrays.findMedian(new int[] {1, 3}, new int[] {2}));
        assertEquals(2.5, MedianOfTwoSortedArrays.findMedian(new int[] {1, 2}, new int[] {3, 4}));
    }

    @Test void edges() {
        assertEquals(1.0, MedianOfTwoSortedArrays.findMedian(new int[] {}, new int[] {1}));
        assertEquals(2.5, MedianOfTwoSortedArrays.findMedian(new int[] {2, 3}, new int[] {}));
        assertEquals(1.0, MedianOfTwoSortedArrays.findMedian(new int[] {1, 1}, new int[] {1, 1}));
        assertEquals(5.0, MedianOfTwoSortedArrays.findMedian(new int[] {5}, new int[] {5}));
        assertEquals(3.0, MedianOfTwoSortedArrays.findMedian(new int[] {1, 2}, new int[] {3, 4, 5}));
        assertEquals(3.0, MedianOfTwoSortedArrays.findMedian(new int[] {3, 4, 5}, new int[] {1, 2}));
        assertEquals(-0.5, MedianOfTwoSortedArrays.findMedian(new int[] {Integer.MIN_VALUE}, new int[] {Integer.MAX_VALUE}));
        assertEquals(Integer.MAX_VALUE, MedianOfTwoSortedArrays.findMedian(new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE}, new int[] {Integer.MAX_VALUE}));
        assertEquals(Integer.MAX_VALUE, MedianOfTwoSortedArrays.findMedian(new int[] {Integer.MAX_VALUE}, new int[] {Integer.MAX_VALUE}));
        assertEquals(-0.5, MedianOfTwoSortedArrays.findMedian(new int[] {Integer.MIN_VALUE, 0}, new int[] {-1, Integer.MAX_VALUE}));
    }

    @Test void disjointRanges() {
        assertEquals(5.5, MedianOfTwoSortedArrays.findMedian(new int[] {1, 2, 3, 4, 5}, new int[] {6, 7, 8, 9, 10}));
        assertEquals(5.5, MedianOfTwoSortedArrays.findMedian(new int[] {6, 7, 8, 9, 10}, new int[] {1, 2, 3, 4, 5}));
    }

    @Test void matchesBruteForce() {
        Random r = new Random(5);
        for (int t = 0; t < 5000; t++) {
            int[] a = sorted(r, r.nextInt(8), 12), b = sorted(r, r.nextInt(8), 12);
            if (a.length + b.length == 0) continue;
            assertEquals(brute(a, b), MedianOfTwoSortedArrays.findMedian(a, b), Arrays.toString(a) + " " + Arrays.toString(b));
        }
        for (int t = 0; t < 500; t++) {
            int[] a = sortedWide(r, 1 + r.nextInt(6)), b = sortedWide(r, r.nextInt(6));
            assertEquals(brute(a, b), MedianOfTwoSortedArrays.findMedian(a, b), Arrays.toString(a) + " " + Arrays.toString(b));
        }
    }

    static int[] sorted(Random r, int n, int bound) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(bound) - bound / 2;
        Arrays.sort(a);
        return a;
    }

    static int[] sortedWide(Random r, int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextBoolean() ? r.nextInt() : (r.nextBoolean() ? Integer.MAX_VALUE : Integer.MIN_VALUE);
        Arrays.sort(a);
        return a;
    }

    @Test void largeInputIsLogarithmic() {
        int[] a = new int[5_000_000], b = new int[3_000_000];
        for (int i = 0; i < a.length; i++) a[i] = 2 * i;
        for (int i = 0; i < b.length; i++) b[i] = 2 * i + 1;
        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            for (int i = 0; i < 20_000; i++) MedianOfTwoSortedArrays.findMedian(a, b);
        });
        assertEquals(brute(new int[] {0, 2, 4}, new int[] {1, 3}), MedianOfTwoSortedArrays.findMedian(new int[] {0, 2, 4}, new int[] {1, 3}));
    }
}
