package practice.d20261028.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class ShortestPathsTest {
    @Test void networkDelayExamples() {
        assertEquals(2, ShortestPaths.networkDelay(new int[][] {{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 4, 2));
        assertEquals(1, ShortestPaths.networkDelay(new int[][] {{1, 2, 1}}, 2, 1));
        assertEquals(-1, ShortestPaths.networkDelay(new int[][] {{1, 2, 1}}, 2, 2));
        assertEquals(0, ShortestPaths.networkDelay(new int[][] {}, 1, 1));
        assertEquals(-1, ShortestPaths.networkDelay(new int[][] {}, 2, 1));
        // cheaper long path beats a direct expensive edge; zero weights and parallel edges
        assertEquals(3, ShortestPaths.networkDelay(new int[][] {{1, 2, 10}, {1, 3, 1}, {3, 2, 2}, {1, 2, 5}, {2, 2, 0}}, 3, 1));
    }

    @Test void cheapestExamples() {
        int[][] f = {{0, 1, 100}, {1, 2, 100}, {2, 0, 100}, {1, 3, 600}, {2, 3, 200}};
        assertEquals(700, ShortestPaths.cheapestWithStops(4, f, 0, 3, 1));
        assertEquals(400, ShortestPaths.cheapestWithStops(4, f, 0, 3, 2));
        assertEquals(-1, ShortestPaths.cheapestWithStops(4, f, 3, 0, 5));
        assertEquals(0, ShortestPaths.cheapestWithStops(4, f, 2, 2, 0));
        assertEquals(500, ShortestPaths.cheapestWithStops(3, new int[][] {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}}, 0, 2, 0));
        assertEquals(200, ShortestPaths.cheapestWithStops(3, new int[][] {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}}, 0, 2, 1));
    }

    @Test void networkDelayMatchesFloydWarshall() {
        Random r = new Random(2810);
        for (int iter = 0; iter < 400; iter++) {
            int n = 1 + r.nextInt(7), m = r.nextInt(15);
            int[][] t = new int[m][];
            long[][] d = new long[n + 1][n + 1];
            for (long[] row : d) Arrays.fill(row, Long.MAX_VALUE / 4);
            for (int i = 1; i <= n; i++) d[i][i] = 0;
            for (int i = 0; i < m; i++) {
                t[i] = new int[] {1 + r.nextInt(n), 1 + r.nextInt(n), r.nextInt(10)};
                d[t[i][0]][t[i][1]] = Math.min(d[t[i][0]][t[i][1]], t[i][2]);
            }
            for (int x = 1; x <= n; x++) for (int a = 1; a <= n; a++) for (int b = 1; b <= n; b++) d[a][b] = Math.min(d[a][b], d[a][x] + d[x][b]);
            int k = 1 + r.nextInt(n);
            long want = 0;
            for (int i = 1; i <= n; i++) want = Math.max(want, d[k][i]);
            assertEquals(want >= Long.MAX_VALUE / 4 ? -1 : (int) want, ShortestPaths.networkDelay(t, n, k));
        }
    }

    @Test void cheapestMatchesLayeredBruteForce() {
        Random r = new Random(77);
        for (int iter = 0; iter < 400; iter++) {
            int n = 2 + r.nextInt(5), m = r.nextInt(14);
            int[][] f = new int[m][];
            for (int i = 0; i < m; i++) f[i] = new int[] {r.nextInt(n), r.nextInt(n), r.nextInt(20)};
            int src = r.nextInt(n), dst = r.nextInt(n), k = r.nextInt(5);
            // exhaustive DFS over walks of at most k+1 flights
            long want = dfs(f, src, dst, k + 1);
            assertEquals(want == Long.MAX_VALUE ? -1 : (int) want, ShortestPaths.cheapestWithStops(n, f, src, dst, k));
        }
    }

    static long dfs(int[][] f, int at, int dst, int left) {
        long best = at == dst ? 0 : Long.MAX_VALUE;
        if (left == 0) return best;
        for (int[] e : f) {
            if (e[0] != at) continue;
            long sub = dfs(f, e[1], dst, left - 1);
            if (sub != Long.MAX_VALUE) best = Math.min(best, sub + e[2]);
        }
        return best;
    }

    @Test void largeGraphsAreFast() {
        int n = 100_000;
        int[][] t = new int[2 * n][];
        Random r = new Random(5);
        for (int i = 0; i < n; i++) t[i] = new int[] {i + 1 <= n - 1 ? i + 1 : n, Math.min(n, i + 2), 1 + r.nextInt(100)};
        for (int i = 0; i < n; i++) t[n + i] = new int[] {1 + r.nextInt(n), 1 + r.nextInt(n), 1 + r.nextInt(100)};
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertTrue(ShortestPaths.networkDelay(t, n, 1) >= -1));
        int[][] fl = new int[200_000][];
        for (int i = 0; i < fl.length; i++) fl[i] = new int[] {r.nextInt(5000), r.nextInt(5000), r.nextInt(1000)};
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertTrue(ShortestPaths.cheapestWithStops(5000, fl, 0, 4999, 5) >= -1));
    }
}
