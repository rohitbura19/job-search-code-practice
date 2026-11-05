package practice.d20261105.c2;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class BfsProblemsTest {
    @Test void orangesExamples() {
        assertEquals(4, BfsProblems.orangesRotting(new int[][] {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}}));
        assertEquals(-1, BfsProblems.orangesRotting(new int[][] {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}}));
        assertEquals(0, BfsProblems.orangesRotting(new int[][] {{0, 2}}));
        assertEquals(0, BfsProblems.orangesRotting(new int[][] {{0}}));
        assertEquals(-1, BfsProblems.orangesRotting(new int[][] {{1}}));
        assertEquals(0, BfsProblems.orangesRotting(new int[][] {}));
        assertEquals(1, BfsProblems.orangesRotting(new int[][] {{2, 1, 2}}));
        assertEquals(2, BfsProblems.orangesRotting(new int[][] {{2, 1, 1}}));
    }

    @Test void orangesDoesNotMutateInput() {
        int[][] g = {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}};
        BfsProblems.orangesRotting(g);
        assertArrayEquals(new int[] {2, 1, 1}, g[0]);
        assertArrayEquals(new int[] {0, 1, 1}, g[2]);
    }

    /** Brute force: simulate minute by minute. */
    static int simulate(int[][] g0) {
        int m = g0.length, n = m == 0 ? 0 : g0[0].length;
        int[][] g = new int[m][];
        for (int i = 0; i < m; i++) g[i] = g0[i].clone();
        for (int minutes = 0; ; minutes++) {
            boolean fresh = false, changed = false;
            int[][] next = new int[m][];
            for (int i = 0; i < m; i++) next[i] = g[i].clone();
            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++) {
                    if (g[i][j] == 1) fresh = true;
                    if (g[i][j] == 1 && ((i > 0 && g[i - 1][j] == 2) || (j > 0 && g[i][j - 1] == 2) || (i + 1 < m && g[i + 1][j] == 2) || (j + 1 < n && g[i][j + 1] == 2))) { next[i][j] = 2; changed = true; }
                }
            if (!fresh) return minutes;
            if (!changed) return -1;
            g = next;
        }
    }

    @Test void orangesBrute() {
        Random r = new Random(9);
        for (int t = 0; t < 1000; t++) {
            int m = 1 + r.nextInt(5), n = 1 + r.nextInt(5);
            int[][] g = new int[m][n];
            for (int[] row : g) for (int j = 0; j < n; j++) row[j] = r.nextInt(3);
            assertEquals(simulate(g), BfsProblems.orangesRotting(g), Arrays.deepToString(g));
        }
    }

    @Test void nearestZeroExamples() {
        assertArrayEquals(new int[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}, BfsProblems.nearestZero(new int[][] {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));
        assertArrayEquals(new int[][] {{0, 0, 0}, {0, 1, 0}, {1, 2, 1}}, BfsProblems.nearestZero(new int[][] {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}}));
        assertArrayEquals(new int[][] {{0}}, BfsProblems.nearestZero(new int[][] {{0}}));
        assertArrayEquals(new int[][] {{3, 2, 1, 0}}, BfsProblems.nearestZero(new int[][] {{1, 1, 1, 0}}));
    }

    @Test void nearestZeroBrute() {
        Random r = new Random(10);
        for (int t = 0; t < 500; t++) {
            int m = 1 + r.nextInt(6), n = 1 + r.nextInt(6);
            int[][] g = new int[m][n];
            for (int[] row : g) for (int j = 0; j < n; j++) row[j] = r.nextInt(4) == 0 ? 0 : 1;
            g[r.nextInt(m)][r.nextInt(n)] = 0;
            int[][] expected = new int[m][n];
            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++) {
                    int best = Integer.MAX_VALUE;
                    for (int a = 0; a < m; a++) for (int b = 0; b < n; b++) if (g[a][b] == 0) best = Math.min(best, Math.abs(a - i) + Math.abs(b - j));
                    expected[i][j] = best;
                }
            assertArrayEquals(expected, BfsProblems.nearestZero(g));
        }
    }

    @Test void nearestZeroLargeGrid() {
        int n = 1500;
        int[][] g = new int[n][n];
        for (int[] row : g) Arrays.fill(row, 1);
        g[0][0] = 0;
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertEquals(2 * (n - 1), BfsProblems.nearestZero(g)[n - 1][n - 1]));
    }

    @Test void openLockExamples() {
        assertEquals(6, BfsProblems.openLock(new String[] {"0201", "0101", "0102", "1212", "2002"}, "0202"));
        assertEquals(4, BfsProblems.openLock(new String[] {"8888"}, "9999"));
        assertEquals(-1, BfsProblems.openLock(new String[] {"8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"}, "8888"));
        assertEquals(-1, BfsProblems.openLock(new String[] {"0000"}, "8888"));
        assertEquals(0, BfsProblems.openLock(new String[] {}, "0000"));
        assertEquals(1, BfsProblems.openLock(new String[] {}, "0009"));
        assertEquals(-1, BfsProblems.openLock(new String[] {"0202"}, "0202"));
        assertEquals(20, BfsProblems.openLock(new String[] {}, "5555"));
    }

    @Test void openLockLargeDeadSet() {
        String[] dead = new String[5000];
        for (int i = 0; i < dead.length; i++) dead[i] = String.format("%04d", 9999 - i * 2);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> BfsProblems.openLock(dead, "5555"));
    }
}
