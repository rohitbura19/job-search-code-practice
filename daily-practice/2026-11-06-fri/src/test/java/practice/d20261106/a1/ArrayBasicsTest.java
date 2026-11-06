package practice.d20261106.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ArrayBasicsTest {
    @Test void profitExamples() {
        assertEquals(5, ArrayBasics.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
        assertEquals(0, ArrayBasics.maxProfit(new int[]{7, 6, 4, 3, 1}));
        assertEquals(0, ArrayBasics.maxProfit(new int[]{}));
        assertEquals(0, ArrayBasics.maxProfit(new int[]{5}));
        assertEquals(Integer.MAX_VALUE, ArrayBasics.maxProfit(new int[]{0, Integer.MAX_VALUE}));
    }

    @Test void profitRandomVsBrute() {
        Random r = new Random(1);
        for (int t = 0; t < 300; t++) {
            int[] p = r.ints(r.nextInt(12), 0, 50).toArray();
            int best = 0;
            for (int i = 0; i < p.length; i++) for (int j = i + 1; j < p.length; j++) best = Math.max(best, p[j] - p[i]);
            assertEquals(best, ArrayBasics.maxProfit(p), Arrays.toString(p));
        }
    }

    @Test void nearbyDuplicate() {
        assertTrue(ArrayBasics.containsNearbyDuplicate(new int[]{1, 2, 3, 1}, 3));
        assertFalse(ArrayBasics.containsNearbyDuplicate(new int[]{1, 2, 3, 1}, 2));
        assertTrue(ArrayBasics.containsNearbyDuplicate(new int[]{1, 0, 1, 1}, 1));
        assertFalse(ArrayBasics.containsNearbyDuplicate(new int[]{}, 5));
        assertFalse(ArrayBasics.containsNearbyDuplicate(new int[]{7}, 0));
        assertFalse(ArrayBasics.containsNearbyDuplicate(new int[]{7, 7}, 0));
        assertTrue(ArrayBasics.containsNearbyDuplicate(new int[]{9, 9}, Integer.MAX_VALUE));
    }

    @Test void nearbyDuplicateLargeIsLinear() {
        int[] big = new int[500_000];
        for (int i = 0; i < big.length; i++) big[i] = i;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertFalse(ArrayBasics.containsNearbyDuplicate(big, 1000)));
    }

    @Test void mergeExamples() {
        int[] a = {1, 3, 5, 0, 0, 0};
        ArrayBasics.mergeSorted(a, 3, new int[]{2, 4, 6}, 3);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, a);
        int[] e = {0, 0};
        ArrayBasics.mergeSorted(e, 0, new int[]{4, 4}, 2);
        assertArrayEquals(new int[]{4, 4}, e);
        int[] f = {2, 2, 2};
        ArrayBasics.mergeSorted(f, 3, new int[]{}, 0);
        assertArrayEquals(new int[]{2, 2, 2}, f);
        int[] g = {5, 0};
        ArrayBasics.mergeSorted(g, 1, new int[]{Integer.MIN_VALUE}, 1);
        assertArrayEquals(new int[]{Integer.MIN_VALUE, 5}, g);
    }

    @Test void mergeRandomVsSort() {
        Random r = new Random(2);
        for (int t = 0; t < 300; t++) {
            int[] x = r.ints(r.nextInt(8), -5, 5).sorted().toArray(), y = r.ints(r.nextInt(8), -5, 5).sorted().toArray();
            int[] a = Arrays.copyOf(x, x.length + y.length);
            int[] expected = Arrays.copyOf(a, a.length);
            System.arraycopy(y, 0, expected, x.length, y.length);
            Arrays.sort(expected);
            ArrayBasics.mergeSorted(a, x.length, y, y.length);
            assertArrayEquals(expected, a);
        }
    }
}
