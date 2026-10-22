package practice.d20261013.d1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class TrapRainWaterTest {

    @Test @DisplayName("sheet examples (two pointers)")
    void examples() {
        assertEquals(6, TrapRainWater.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
        assertEquals(9, TrapRainWater.trap(new int[]{4, 2, 0, 3, 2, 5}));
    }

    @Test @DisplayName("sheet examples (monotonic stack)")
    void examplesStack() {
        assertEquals(6, TrapRainWater.trapStack(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
        assertEquals(9, TrapRainWater.trapStack(new int[]{4, 2, 0, 3, 2, 5}));
    }

    @Test @DisplayName("nothing can hold water: empty, 1, 2 bars, flat, ascending, descending")
    void none() {
        for (int[] h : new int[][]{{}, {5}, {3, 7}, {2, 2, 2, 2}, {1, 2, 3, 4}, {4, 3, 2, 1}}) {
            assertEquals(0, TrapRainWater.trap(h), Arrays.toString(h));
            assertEquals(0, TrapRainWater.trapStack(h), Arrays.toString(h));
        }
    }

    @Test @DisplayName("a single pit, a pit with a taller wall on one side only")
    void pits() {
        assertEquals(2, TrapRainWater.trap(new int[]{2, 0, 2}));
        assertEquals(2, TrapRainWater.trapStack(new int[]{2, 0, 2}));
        assertEquals(1, TrapRainWater.trap(new int[]{1, 0, 3}));     // limited by the LOWER wall
        assertEquals(1, TrapRainWater.trapStack(new int[]{3, 0, 1}));
    }

    static int oracle(int[] h) {   // prefix / suffix max, O(n) but different from both solutions under test
        int n = h.length, w = 0;
        for (int i = 0; i < n; i++) {
            int l = 0, r = 0;
            for (int j = 0; j <= i; j++) l = Math.max(l, h[j]);
            for (int j = i; j < n; j++) r = Math.max(r, h[j]);
            w += Math.min(l, r) - h[i];
        }
        return w;
    }

    @Test @DisplayName("random arrays agree with the brute force (both variants)")
    void random() {
        Random rnd = new Random(13);
        for (int t = 0; t < 500; t++) {
            int[] h = new int[rnd.nextInt(40)];
            for (int i = 0; i < h.length; i++) h[i] = rnd.nextInt(12);
            assertEquals(oracle(h), TrapRainWater.trap(h), Arrays.toString(h));
            assertEquals(oracle(h), TrapRainWater.trapStack(h), Arrays.toString(h));
        }
    }

    @Test @DisplayName("input array is not modified")
    void notModified() {
        int[] h = {4, 2, 0, 3, 2, 5};
        TrapRainWater.trap(h); TrapRainWater.trapStack(h);
        assertEquals("[4, 2, 0, 3, 2, 5]", Arrays.toString(h));
    }

    @Test @DisplayName("1,000,000 bars, linear time; large heights do not overflow the answer here")
    void large() {
        int n = 1_000_000;
        int[] h = new int[n];
        h[0] = 1000; h[n - 1] = 1000;   // a lake 1000 deep and 999,998 wide
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            assertEquals(1000 * (n - 2), TrapRainWater.trap(h));
            assertEquals(1000 * (n - 2), TrapRainWater.trapStack(h));
        });
    }
}
