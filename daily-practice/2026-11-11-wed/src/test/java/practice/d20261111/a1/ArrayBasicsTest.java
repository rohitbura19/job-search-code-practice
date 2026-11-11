package practice.d20261111.a1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

class ArrayBasicsTest {
    @Test void thirdMaxExamples() {
        assertEquals(1, ArrayBasics.thirdMax(new int[]{3, 2, 1}));
        assertEquals(2, ArrayBasics.thirdMax(new int[]{1, 2}));
        assertEquals(1, ArrayBasics.thirdMax(new int[]{2, 2, 3, 1}));
        assertEquals(5, ArrayBasics.thirdMax(new int[]{5}));
        assertEquals(7, ArrayBasics.thirdMax(new int[]{7, 7, 7}));
    }

    @Test void thirdMaxExtremes() {
        assertEquals(Integer.MIN_VALUE, ArrayBasics.thirdMax(new int[]{1, Integer.MIN_VALUE, 2}));
        assertEquals(Integer.MAX_VALUE, ArrayBasics.thirdMax(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}));
        assertEquals(Integer.MIN_VALUE, ArrayBasics.thirdMax(new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE}));
    }

    @Test void thirdMaxRandomVsTreeSet() {
        Random r = new Random(1);
        for (int t = 0; t < 2000; t++) {
            int[] a = new int[1 + r.nextInt(8)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(7) - 3;
            TreeSet<Integer> s = new TreeSet<>();
            for (int x : a) s.add(x);
            int expected = s.size() >= 3 ? s.descendingSet().stream().skip(2).findFirst().get() : s.last();
            assertEquals(expected, ArrayBasics.thirdMax(a));
        }
    }

    @Test void flipExamples() {
        assertEquals(4, ArrayBasics.longestOnesWithOneFlip(new int[]{1, 0, 1, 1, 0}));
        assertEquals(1, ArrayBasics.longestOnesWithOneFlip(new int[]{0}));
        assertEquals(3, ArrayBasics.longestOnesWithOneFlip(new int[]{1, 1, 1}));
        assertEquals(1, ArrayBasics.longestOnesWithOneFlip(new int[]{0, 0, 0}));
        assertEquals(0, ArrayBasics.longestOnesWithOneFlip(new int[]{}));
    }

    @Test void flipRandomVsBrute() {
        Random r = new Random(2);
        for (int t = 0; t < 1000; t++) {
            int[] a = new int[r.nextInt(12)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(2);
            int best = 0;
            for (int i = 0; i < a.length; i++)
                for (int j = i; j < a.length; j++) {
                    int zeros = 0;
                    for (int k = i; k <= j; k++) if (a[k] == 0) zeros++;
                    if (zeros <= 1) best = Math.max(best, j - i + 1);
                }
            assertEquals(best, ArrayBasics.longestOnesWithOneFlip(a));
        }
    }

    @Test void flowersExamples() {
        assertTrue(ArrayBasics.canPlaceFlowers(new int[]{1, 0, 0, 0, 1}, 1));
        assertFalse(ArrayBasics.canPlaceFlowers(new int[]{1, 0, 0, 0, 1}, 2));
        assertTrue(ArrayBasics.canPlaceFlowers(new int[]{0}, 1));
        assertTrue(ArrayBasics.canPlaceFlowers(new int[]{0, 0}, 1));
        assertFalse(ArrayBasics.canPlaceFlowers(new int[]{0, 0}, 2));
        assertTrue(ArrayBasics.canPlaceFlowers(new int[]{1, 1}, 0));
        assertTrue(ArrayBasics.canPlaceFlowers(new int[]{}, 0));
    }

    @Test void flowersRandomVsBackTracking() {
        Random r = new Random(3);
        for (int t = 0; t < 1000; t++) {
            int[] bed = new int[r.nextInt(10)];
            for (int i = 0; i < bed.length; i++) bed[i] = r.nextInt(4) == 0 ? 1 : 0;
            for (int i = 1; i < bed.length; i++) if (bed[i] == 1 && bed[i - 1] == 1) bed[i] = 0;
            int[] copy = bed.clone();
            int max = most(copy, 0);
            for (int n = 0; n <= max + 1; n++) assertEquals(n <= max, ArrayBasics.canPlaceFlowers(bed, n), java.util.Arrays.toString(bed) + " n=" + n);
            assertArrayEquals(copy, bed, "input must not change");
        }
    }

    private static int most(int[] b, int i) {
        if (i >= b.length) return 0;
        int best = most(b, i + 1);
        boolean ok = b[i] == 0 && (i == 0 || b[i - 1] == 0) && (i == b.length - 1 || b[i + 1] == 0);
        if (ok) { b[i] = 1; best = Math.max(best, 1 + most(b, i + 1)); b[i] = 0; }
        return best;
    }

    @Test void largeInputIsFast() {
        int[] a = new int[3_000_000];
        for (int i = 0; i < a.length; i++) a[i] = (i * 31) % 2;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            ArrayBasics.longestOnesWithOneFlip(a);
            ArrayBasics.canPlaceFlowers(new int[3_000_000], 1_000_000);
            ArrayBasics.thirdMax(a);
        });
    }
}
