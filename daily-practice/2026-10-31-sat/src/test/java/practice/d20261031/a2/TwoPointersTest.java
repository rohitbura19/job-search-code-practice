package practice.d20261031.a2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class TwoPointersTest {
    @Test void twoSumExamples() {
        assertArrayEquals(new int[] {1, 2}, TwoPointers.twoSumSorted(new int[] {2, 7, 11, 15}, 9));
        assertArrayEquals(new int[] {1, 3}, TwoPointers.twoSumSorted(new int[] {2, 3, 4}, 6));
        assertArrayEquals(new int[] {1, 2}, TwoPointers.twoSumSorted(new int[] {-1, 0}, -1));
    }

    @Test void twoSumEdges() {
        assertEquals(0, TwoPointers.twoSumSorted(new int[] {}, 0).length);
        assertEquals(0, TwoPointers.twoSumSorted(new int[] {4}, 8).length);       // one element cannot pair with itself
        assertArrayEquals(new int[] {1, 2}, TwoPointers.twoSumSorted(new int[] {3, 3}, 6));
        assertEquals(0, TwoPointers.twoSumSorted(new int[] {1, 2, 3}, 100).length);
        // overflow: the sum does not fit in an int
        int[] big = {Integer.MAX_VALUE - 1, Integer.MAX_VALUE};
        assertArrayEquals(new int[] {1, 2}, TwoPointers.twoSumSorted(big, 2L * Integer.MAX_VALUE - 1));
        assertEquals(0, TwoPointers.twoSumSorted(new int[] {Integer.MIN_VALUE, Integer.MAX_VALUE}, Long.MAX_VALUE).length);
    }

    @Test void twoSumRandomVsBrute() {
        Random r = new Random(3);
        for (int t = 0; t < 400; t++) {
            int[] a = new int[r.nextInt(10)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(21) - 10;
            Arrays.sort(a);
            int target = r.nextInt(31) - 15;
            boolean exists = false;
            for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length; j++) if (a[i] + a[j] == target) exists = true;
            int[] got = TwoPointers.twoSumSorted(a, target);
            if (!exists) assertEquals(0, got.length);
            else {
                assertEquals(2, got.length);
                assertTrue(got[0] < got[1] && got[0] >= 1 && got[1] <= a.length);
                assertEquals(target, a[got[0] - 1] + a[got[1] - 1]);
            }
        }
    }

    @Test void areaExamples() {
        assertEquals(49, TwoPointers.maxArea(new int[] {1, 8, 6, 2, 5, 4, 8, 3, 7}));
        assertEquals(1, TwoPointers.maxArea(new int[] {1, 1}));
        assertEquals(0, TwoPointers.maxArea(new int[] {}));
        assertEquals(0, TwoPointers.maxArea(new int[] {9}));
        assertEquals(0, TwoPointers.maxArea(new int[] {0, 0, 0}));
    }

    @Test void areaOverflowAndRandom() {
        int[] h = new int[100_000];
        Arrays.fill(h, Integer.MAX_VALUE);
        assertEquals((long) Integer.MAX_VALUE * (h.length - 1), TwoPointers.maxArea(h));
        Random r = new Random(4);
        for (int t = 0; t < 300; t++) {
            int[] a = new int[r.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(15);
            long expect = 0;
            for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length; j++) expect = Math.max(expect, (long) Math.min(a[i], a[j]) * (j - i));
            assertEquals(expect, TwoPointers.maxArea(a));
        }
    }

    @Test void large() {
        int[] a = new int[3_000_000];
        for (int i = 0; i < a.length; i++) a[i] = i;
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertArrayEquals(new int[] {a.length - 1, a.length}, TwoPointers.twoSumSorted(a, 2L * a.length - 3));
            TwoPointers.maxArea(a);
        });
    }
}
