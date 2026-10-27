package practice.d20261027.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class CountSmallerTest {
    @Test void examples() {
        assertArrayEquals(new int[]{2, 1, 1, 0}, CountSmaller.countSmaller(new int[]{5, 2, 6, 1}));
        assertArrayEquals(new int[]{0}, CountSmaller.countSmaller(new int[]{-1}));
        assertArrayEquals(new int[]{0, 0}, CountSmaller.countSmaller(new int[]{-1, -1}));
    }

    @Test void emptyAndSorted() {
        assertArrayEquals(new int[]{}, CountSmaller.countSmaller(new int[]{}));
        assertArrayEquals(new int[]{0, 0, 0}, CountSmaller.countSmaller(new int[]{1, 2, 3}));
        assertArrayEquals(new int[]{2, 1, 0}, CountSmaller.countSmaller(new int[]{3, 2, 1}));
    }

    @Test void extremeValuesDoNotOverflow() {
        assertArrayEquals(new int[]{2, 1, 0}, CountSmaller.countSmaller(new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE}));
        assertArrayEquals(new int[]{1, 0}, CountSmaller.countSmaller(new int[]{0, Integer.MIN_VALUE}));
    }

    @Test void equalValuesAreNotCounted() {
        assertArrayEquals(new int[]{0, 0, 0, 0}, CountSmaller.countSmaller(new int[]{4, 4, 4, 4}));
        assertArrayEquals(new int[]{3, 0, 0, 0}, CountSmaller.countSmaller(new int[]{4, 2, 2, 2}));
        assertArrayEquals(new int[]{1, 1, 0, 0}, CountSmaller.countSmaller(new int[]{3, 3, 1, 3}));
    }

    @Test void randomAgainstBruteForce() {
        Random r = new Random(9);
        for (int t = 0; t < 500; t++) {
            int[] a = new int[r.nextInt(60)];
            for (int i = 0; i < a.length; i++) a[i] = r.nextInt(15) - 7;
            int[] expect = new int[a.length];
            long inv = 0;
            for (int i = 0; i < a.length; i++)
                for (int j = i + 1; j < a.length; j++) if (a[j] < a[i]) { expect[i]++; inv++; }
            assertArrayEquals(expect, CountSmaller.countSmaller(a));
            assertEquals(inv, CountSmaller.inversions(a));
        }
    }

    @Test void inversionsOfReversedLargeArrayNeedALong() {
        int n = 200_000;
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - i;
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals((long) n * (n - 1) / 2, CountSmaller.inversions(a)));
    }
}
