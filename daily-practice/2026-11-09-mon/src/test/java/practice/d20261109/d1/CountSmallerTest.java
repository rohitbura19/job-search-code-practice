package practice.d20261109.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class CountSmallerTest {
    static int[] brute(int[] a) {
        int[] r = new int[a.length];
        for (int i = 0; i < a.length; i++) for (int j = i + 1; j < a.length; j++) if (a[j] < a[i]) r[i]++;
        return r;
    }

    @Test void examples() {
        assertArrayEquals(new int[] {2, 1, 1, 0}, CountSmaller.countSmallerAfter(new int[] {5, 2, 6, 1}));
        assertArrayEquals(new int[] {0}, CountSmaller.countSmallerAfter(new int[] {-1}));
        assertArrayEquals(new int[] {0, 0}, CountSmaller.countSmallerAfter(new int[] {-1, -1}));
        assertArrayEquals(new int[0], CountSmaller.countSmallerAfter(new int[0]));
    }

    @Test void sortedAndReversed() {
        assertArrayEquals(new int[] {0, 0, 0, 0}, CountSmaller.countSmallerAfter(new int[] {1, 2, 3, 4}));
        assertArrayEquals(new int[] {3, 2, 1, 0}, CountSmaller.countSmallerAfter(new int[] {4, 3, 2, 1}));
    }

    @Test void duplicatesAreNotCounted() {
        assertArrayEquals(new int[] {0, 0, 0, 0}, CountSmaller.countSmallerAfter(new int[] {7, 7, 7, 7}));
        assertArrayEquals(new int[] {2, 0, 1, 0}, CountSmaller.countSmallerAfter(new int[] {3, 1, 3, 2}));
    }

    @Test void extremeValues() {
        int[] a = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, Integer.MAX_VALUE, Integer.MIN_VALUE};
        assertArrayEquals(brute(a), CountSmaller.countSmallerAfter(a));
    }

    @Test void randomVsBrute() {
        Random r = new Random(9);
        for (int t = 0; t < 1500; t++) {
            int n = r.nextInt(25);
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = r.nextInt(12) - 6;
            assertArrayEquals(brute(a), CountSmaller.countSmallerAfter(a), Arrays.toString(a));
        }
    }

    @Test void largeInput() {
        int n = 200_000;
        int[] a = new int[n];
        Random r = new Random(4);
        for (int i = 0; i < n; i++) a[i] = r.nextInt();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            int[] res = CountSmaller.countSmallerAfter(a);
            assertEquals(n, res.length);
            assertEquals(0, res[n - 1]);
        });
        int[] desc = new int[n];
        for (int i = 0; i < n; i++) desc[i] = n - i;
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(n - 1, CountSmaller.countSmallerAfter(desc)[0]));
    }
}
