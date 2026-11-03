package practice.d20261103.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;

class TrappingRain2Test {
    @Test void examples() {
        assertEquals(4, TrappingRain2.trapRainWater(new int[][]{{1, 4, 3, 1, 3, 2}, {3, 2, 1, 3, 2, 4}, {2, 3, 3, 2, 3, 1}}));
        assertEquals(10, TrappingRain2.trapRainWater(new int[][]{{3, 3, 3, 3, 3}, {3, 2, 2, 2, 3}, {3, 2, 1, 2, 3}, {3, 2, 2, 2, 3}, {3, 3, 3, 3, 3}}));
        assertEquals(1, TrappingRain2.trapRainWater(new int[][]{{2, 2, 2}, {2, 1, 2}, {2, 2, 2}}));
    }

    @Test void tooSmallOrFlatOrLeaky() {
        assertEquals(0, TrappingRain2.trapRainWater(new int[][]{{5}}));
        assertEquals(0, TrappingRain2.trapRainWater(new int[][]{{1, 2, 3}, {3, 2, 1}}));
        assertEquals(0, TrappingRain2.trapRainWater(new int[][]{{1, 2}, {3, 4}, {5, 6}}));
        assertEquals(0, TrappingRain2.trapRainWater(new int[][]{{3, 3, 3}, {3, 3, 3}, {3, 3, 3}}));
        // a hole in the wall lets the water out through the lowest border cell
        assertEquals(0, TrappingRain2.trapRainWater(new int[][]{{5, 5, 5}, {5, 1, 0}, {5, 5, 5}}));
    }

    @Test void waterLevelIsLimitedByLowestExit() {
        // interior pool of two cells at height 0; lowest rim is 2 -> each holds 2
        int[][] g = {{9, 9, 9, 9}, {9, 0, 0, 2}, {9, 9, 9, 9}};
        assertEquals(4, TrappingRain2.trapRainWater(g));
        // all walls are 9: every interior cell fills to 9 (6 + 9 + 6)
        int[][] h = {{9, 9, 9, 9, 9}, {9, 3, 0, 3, 9}, {9, 9, 9, 9, 9}};
        assertEquals(21, TrappingRain2.trapRainWater(h));
    }

    @Test void largeValuesDoNotOverflow() {
        int n = 300;
        int[][] g = new int[n][n];
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) g[i][j] = (i == 0 || j == 0 || i == n - 1 || j == n - 1) ? 20_000 : 0;
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(298 * 298 * 20_000, TrappingRain2.trapRainWater(g)));
    }

    /** Fixpoint: level = height on the border; inside level = max(height, min neighbour level); iterate until stable from +infinity. */
    static int brute(int[][] h) {
        int m = h.length, n = h[0].length;
        int[][] lv = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) lv[i][j] = (i == 0 || j == 0 || i == m - 1 || j == n - 1) ? h[i][j] : Integer.MAX_VALUE;
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 1; i < m - 1; i++)
                for (int j = 1; j < n - 1; j++) {
                    int min = Math.min(Math.min(lv[i - 1][j], lv[i + 1][j]), Math.min(lv[i][j - 1], lv[i][j + 1]));
                    int nv = Math.max(h[i][j], min);
                    if (nv < lv[i][j]) { lv[i][j] = nv; changed = true; }
                }
        }
        int sum = 0;
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) sum += lv[i][j] - h[i][j];
        return sum;
    }

    @Test void randomVsFixpoint() {
        Random rnd = new Random(8);
        for (int t = 0; t < 300; t++) {
            int m = 1 + rnd.nextInt(7), n = 1 + rnd.nextInt(7);
            int[][] g = new int[m][n];
            for (int[] row : g) for (int j = 0; j < n; j++) row[j] = rnd.nextInt(9);
            int expect = (m < 3 || n < 3) ? 0 : brute(g);
            assertEquals(expect, TrappingRain2.trapRainWater(g));
        }
    }

    @Test void randomLargeWithinTimeout() {
        Random rnd = new Random(9);
        int[][] g = new int[300][300];
        for (int[] row : g) for (int j = 0; j < 300; j++) row[j] = rnd.nextInt(20_001);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertTrue(TrappingRain2.trapRainWater(g) >= 0));
    }
}
