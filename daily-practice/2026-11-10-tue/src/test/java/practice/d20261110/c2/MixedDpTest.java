package practice.d20261110.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class MixedDpTest {
    private static char[][] grid(String... rows) { return Arrays.stream(rows).map(String::toCharArray).toArray(char[][]::new); }

    @Test void squareExamples() {
        assertEquals(4, MixedDp.maximalSquare(grid("10100", "10111", "11111", "10010")));
        assertEquals(1, MixedDp.maximalSquare(grid("01", "10")));
        assertEquals(0, MixedDp.maximalSquare(grid("0")));
        assertEquals(9, MixedDp.maximalSquare(grid("111", "111", "111")));
        assertEquals(0, MixedDp.maximalSquare(new char[0][]));
        assertEquals(1, MixedDp.maximalSquare(grid("1")));
        assertEquals(1, MixedDp.maximalSquare(grid("1111")));           // single row
    }

    @Test void squareRandomAgainstBruteForce() {
        Random r = new Random(8);
        for (int t = 0; t < 300; t++) {
            int h = 1 + r.nextInt(7), w = 1 + r.nextInt(7);
            char[][] g = new char[h][w];
            for (char[] row : g) for (int j = 0; j < w; j++) row[j] = r.nextInt(4) == 0 ? '0' : '1';
            int best = 0;
            for (int i = 0; i < h; i++) for (int j = 0; j < w; j++)
                for (int k = 1; i + k <= h && j + k <= w; k++) {
                    boolean ok = true;
                    for (int a = i; a < i + k && ok; a++) for (int b = j; b < j + k; b++) if (g[a][b] != '1') { ok = false; break; }
                    if (ok) best = Math.max(best, k * k);
                }
            assertEquals(best, MixedDp.maximalSquare(g), Arrays.deepToString(g));
        }
    }

    @Test void cooldownExamples() {
        assertEquals(3, MixedDp.maxProfitWithCooldown(new int[]{1, 2, 3, 0, 2}));
        assertEquals(0, MixedDp.maxProfitWithCooldown(new int[]{1}));
        assertEquals(0, MixedDp.maxProfitWithCooldown(new int[]{}));
        assertEquals(0, MixedDp.maxProfitWithCooldown(new int[]{5, 4, 3, 2, 1}));
        assertEquals(4, MixedDp.maxProfitWithCooldown(new int[]{1, 5}));
        assertEquals(6, MixedDp.maxProfitWithCooldown(new int[]{1, 4, 2, 7}));   // buy 1, sell 7 (the 1->4 then 2->7 route is blocked by the cooldown)
    }

    private static int brute(int[] p, int i, boolean holding, boolean cool) {
        if (i >= p.length) return 0;
        int skip = brute(p, i + 1, holding, false);
        if (holding) return Math.max(skip, p[i] + brute(p, i + 1, false, true));
        if (cool) return skip;
        return Math.max(skip, -p[i] + brute(p, i + 1, true, false));
    }

    @Test void cooldownRandomAgainstBruteForce() {
        Random r = new Random(9);
        for (int t = 0; t < 400; t++) {
            int[] p = r.ints(r.nextInt(11), 0, 12).toArray();
            assertEquals(brute(p, 0, false, false), MixedDp.maxProfitWithCooldown(p), Arrays.toString(p));
        }
    }

    @Test void numSquaresExamples() {
        assertEquals(3, MixedDp.numSquares(12));
        assertEquals(2, MixedDp.numSquares(13));
        assertEquals(1, MixedDp.numSquares(1));
        assertEquals(1, MixedDp.numSquares(100));
        assertEquals(4, MixedDp.numSquares(7));
        assertEquals(4, MixedDp.numSquares(15));
    }

    @Test void numSquaresAgainstLagrange() {
        for (int n = 1; n <= 3000; n++) {
            int expected;
            int s = (int) Math.round(Math.sqrt(n));
            int m = n;
            while (m % 4 == 0) m /= 4;
            if (s * s == n) expected = 1;
            else if (m % 8 == 7) expected = 4;
            else {
                expected = 3;
                for (int a = 1; a * a < n; a++) { int rem = n - a * a, q = (int) Math.round(Math.sqrt(rem)); if (q * q == rem) { expected = 2; break; } }
            }
            assertEquals(expected, MixedDp.numSquares(n), "n=" + n);
        }
    }

    @Test void largeInputWithinTimeout() {
        char[][] g = new char[1500][1500];
        for (char[] row : g) Arrays.fill(row, '1');
        int[] prices = new Random(10).ints(2_000_000, 0, 10_000).toArray();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(1500 * 1500, MixedDp.maximalSquare(g));
            MixedDp.maxProfitWithCooldown(prices);
            assertTrue(MixedDp.numSquares(100_000) <= 4);
        });
    }
}
