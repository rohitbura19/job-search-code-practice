package practice.d20261031.a1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class ArrayBasicsTest {
    @Test void nearbyDuplicateExamples() {
        assertTrue(ArrayBasics.containsNearbyDuplicate(new int[] {1, 2, 3, 1}, 3));
        assertTrue(ArrayBasics.containsNearbyDuplicate(new int[] {1, 0, 1, 1}, 1));
        assertFalse(ArrayBasics.containsNearbyDuplicate(new int[] {1, 2, 3, 1, 2, 3}, 2));
    }

    @Test void nearbyDuplicateEdges() {
        assertFalse(ArrayBasics.containsNearbyDuplicate(new int[] {}, 5));
        assertFalse(ArrayBasics.containsNearbyDuplicate(new int[] {7}, 5));
        assertFalse(ArrayBasics.containsNearbyDuplicate(new int[] {7, 7}, 0));
        assertTrue(ArrayBasics.containsNearbyDuplicate(new int[] {7, 7}, 1));
        assertTrue(ArrayBasics.containsNearbyDuplicate(new int[] {Integer.MIN_VALUE, 1, Integer.MIN_VALUE}, Integer.MAX_VALUE));
        // the closest pair counts, not the first: 5 . . 5 5
        assertTrue(ArrayBasics.containsNearbyDuplicate(new int[] {5, 1, 2, 5, 5}, 1));
    }

    @Test void nearbyDuplicateRandomVsBrute() {
        Random r = new Random(1);
        for (int t = 0; t < 400; t++) {
            int[] a = new int[r.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(6);
            int k = r.nextInt(6);
            boolean expect = false;
            for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length && j - i <= k; j++) if (a[i] == a[j]) expect = true;
            assertEquals(expect, ArrayBasics.containsNearbyDuplicate(a, k));
        }
    }

    @Test void profitOnce() {
        assertEquals(5, ArrayBasics.maxProfitOnce(new int[] {7, 1, 5, 3, 6, 4}));
        assertEquals(0, ArrayBasics.maxProfitOnce(new int[] {7, 6, 4, 3, 1}));
        assertEquals(0, ArrayBasics.maxProfitOnce(new int[] {}));
        assertEquals(0, ArrayBasics.maxProfitOnce(new int[] {3}));
        assertEquals(Integer.MAX_VALUE, ArrayBasics.maxProfitOnce(new int[] {0, Integer.MAX_VALUE}));
    }

    @Test void profitManyExamplesAndOverflow() {
        assertEquals(7, ArrayBasics.maxProfitMany(new int[] {7, 1, 5, 3, 6, 4}));
        assertEquals(4, ArrayBasics.maxProfitMany(new int[] {1, 2, 3, 4, 5}));
        assertEquals(0, ArrayBasics.maxProfitMany(new int[] {5, 4, 3}));
        assertEquals(0, ArrayBasics.maxProfitMany(new int[] {}));
        int[] zig = new int[1000];
        for (int i = 0; i < zig.length; i++) zig[i] = i % 2 == 0 ? 0 : Integer.MAX_VALUE;
        assertEquals(500L * Integer.MAX_VALUE, ArrayBasics.maxProfitMany(zig));
    }

    @Test void profitManyRandomVsDp() {
        Random r = new Random(2);
        for (int t = 0; t < 300; t++) {
            int[] p = new int[r.nextInt(15)];
            for (int i = 0; i < p.length; i++) p[i] = r.nextInt(20);
            long hold = Long.MIN_VALUE / 2, free = 0;
            for (int x : p) {
                long nh = Math.max(hold, free - x), nf = Math.max(free, hold + x);
                hold = nh; free = nf;
            }
            assertEquals(free, ArrayBasics.maxProfitMany(p));
        }
    }

    @Test void large() {
        int[] a = new int[2_000_000];
        for (int i = 0; i < a.length; i++) a[i] = i;
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertFalse(ArrayBasics.containsNearbyDuplicate(a, 1000));
            assertEquals(a.length - 1, ArrayBasics.maxProfitOnce(a));
            assertEquals(a.length - 1, ArrayBasics.maxProfitMany(a));
        });
    }
}
