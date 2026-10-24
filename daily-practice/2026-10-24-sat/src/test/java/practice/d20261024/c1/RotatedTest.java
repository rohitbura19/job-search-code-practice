package practice.d20261024.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class RotatedTest {
    @Test void searchExamples() {
        assertEquals(4, Rotated.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
        assertEquals(-1, Rotated.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
        assertEquals(-1, Rotated.search(new int[]{1}, 0));
        assertEquals(0, Rotated.search(new int[]{1}, 1));
        assertEquals(-1, Rotated.search(new int[]{}, 1));
        assertEquals(1, Rotated.search(new int[]{3, 1}, 1));
        assertEquals(0, Rotated.search(new int[]{3, 1}, 3));
        assertEquals(2, Rotated.search(new int[]{1, 2, 3}, 3), "not rotated");
    }

    @Test void searchMatchesBruteForceOnEveryRotation() {
        Random rnd = new Random(3);
        for (int it = 0; it < 3000; it++) {
            int n = 1 + rnd.nextInt(12);
            int[] sorted = rnd.ints(n, -20, 20).distinct().sorted().toArray();
            int k = rnd.nextInt(sorted.length);
            int[] a = rotate(sorted, k);
            for (int t = -22; t <= 22; t++) {
                int idx = Rotated.search(a, t);
                int expected = Arrays.stream(a).boxed().toList().indexOf(t);
                assertEquals(expected, idx, Arrays.toString(a) + " t=" + t);
            }
        }
    }

    @Test void findMinExamples() {
        assertEquals(0, Rotated.findMin(new int[]{4, 5, 6, 7, 0, 1, 2}));
        assertEquals(0, Rotated.findMin(new int[]{2, 2, 2, 0, 1}));
        assertEquals(1, Rotated.findMin(new int[]{1, 3, 3}));
        assertEquals(1, Rotated.findMin(new int[]{3, 3, 1, 3}));
        assertEquals(1, Rotated.findMin(new int[]{3, 1, 3, 3, 3}));
        assertEquals(5, Rotated.findMin(new int[]{5}));
        assertEquals(2, Rotated.findMin(new int[]{2, 2, 2, 2}));
        assertEquals(Integer.MIN_VALUE, Rotated.findMin(new int[]{3, Integer.MIN_VALUE, Integer.MAX_VALUE}));
    }

    @Test void findMinMatchesBruteForceWithDuplicates() {
        Random rnd = new Random(11);
        for (int it = 0; it < 5000; it++) {
            int n = 1 + rnd.nextInt(12);
            int[] sorted = rnd.ints(n, 0, 5).sorted().toArray();
            int[] a = rotate(sorted, rnd.nextInt(n));
            assertEquals(Arrays.stream(a).min().getAsInt(), Rotated.findMin(a), Arrays.toString(a));
        }
    }

    @Test void largeInput() {
        int n = 5_000_000;
        int[] sorted = new int[n];
        for (int i = 0; i < n; i++) sorted[i] = 2 * i;
        int[] a = rotate(sorted, 3_000_000);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            for (int j = 0; j < n; j += 25) assertEquals(Math.floorMod(j - 3_000_000, n), Rotated.search(a, 2 * j));
            assertEquals(0, Rotated.findMin(a));
            assertEquals(-1, Rotated.search(a, 1));
        });
    }

    static int[] rotate(int[] sorted, int k) {
        int n = sorted.length;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = sorted[(i + k) % n];
        return a;
    }
}
