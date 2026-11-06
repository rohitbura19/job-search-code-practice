package practice.d20261106.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class MaximumGapTest {
    @Test void examples() {
        assertEquals(3, MaximumGap.maximumGap(new int[]{3, 6, 9, 1}));
        assertEquals(0, MaximumGap.maximumGap(new int[]{10}));
        assertEquals(0, MaximumGap.maximumGap(new int[]{}));
        assertEquals(0, MaximumGap.maximumGap(new int[]{5, 5, 5}));
        assertEquals(1, MaximumGap.maximumGap(new int[]{1, 2}));
        assertEquals(1, MaximumGap.maximumGap(new int[]{1, 2, 3, 4, 5}));
        assertEquals(100, MaximumGap.maximumGap(new int[]{1, 1, 1, 101}));
    }

    @Test void extremes() {
        assertEquals(Integer.MAX_VALUE, MaximumGap.maximumGap(new int[]{0, Integer.MAX_VALUE}));
        assertEquals(Integer.MAX_VALUE, MaximumGap.maximumGap(new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE}));
        assertEquals(Integer.MAX_VALUE - 2, MaximumGap.maximumGap(new int[]{1, Integer.MAX_VALUE, 2}));
    }

    @Test void randomVsSort() {
        Random r = new Random(10);
        for (int t = 0; t < 3000; t++) {
            int range = new int[]{3, 20, 1000, Integer.MAX_VALUE}[r.nextInt(4)];
            int[] a = r.ints(r.nextInt(12), 0, range).toArray();
            assertEquals(brute(a), MaximumGap.maximumGap(a), Arrays.toString(a));
        }
    }

    @Test void large() {
        int[] a = new Random(11).ints(2_000_000, 0, 1_000_000_000).toArray();
        int expected = brute(a);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(expected, MaximumGap.maximumGap(a)));
    }

    private static int brute(int[] a) {
        int[] s = a.clone();
        Arrays.sort(s);
        long best = 0;
        for (int i = 1; i < s.length; i++) best = Math.max(best, (long) s[i] - s[i - 1]);
        return (int) Math.min(best, Integer.MAX_VALUE);
    }
}
