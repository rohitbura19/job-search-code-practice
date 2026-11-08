package practice.d20261108.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class ShortestPathsTest {
    @Test void networkExamples() {
        assertEquals(2, ShortestPaths.networkDelayTime(new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 4, 2));
        assertEquals(1, ShortestPaths.networkDelayTime(new int[][]{{1, 2, 1}}, 2, 1));
        assertEquals(-1, ShortestPaths.networkDelayTime(new int[][]{{1, 2, 1}}, 2, 2));
        assertEquals(0, ShortestPaths.networkDelayTime(new int[][]{}, 1, 1));
        assertEquals(3, ShortestPaths.networkDelayTime(new int[][]{{1, 2, 1}, {2, 3, 2}, {1, 3, 4}, {1, 3, 4}}, 3, 1));
        assertEquals(0, ShortestPaths.networkDelayTime(new int[][]{{1, 2, 0}, {2, 1, 0}}, 2, 1)); // zero weights, cycle
    }

    @Test void effortExamples() {
        assertEquals(2, ShortestPaths.minimumEffortPath(new int[][]{{1, 2, 2}, {3, 8, 2}, {5, 3, 5}}));
        assertEquals(1, ShortestPaths.minimumEffortPath(new int[][]{{1, 2, 3}, {3, 8, 4}, {5, 3, 5}}));
        assertEquals(0, ShortestPaths.minimumEffortPath(new int[][]{{7}}));
        assertEquals(0, ShortestPaths.minimumEffortPath(new int[][]{{4, 4, 4, 4}}));
        assertEquals(1_000_000_000, ShortestPaths.minimumEffortPath(new int[][]{{1, 1_000_000_001}}));
        assertEquals(Integer.MAX_VALUE - 1, ShortestPaths.minimumEffortPath(new int[][]{{1}, {Integer.MAX_VALUE}}));
    }

    @Test void flightsExamples() {
        int[][] f = {{0, 1, 100}, {1, 2, 100}, {2, 0, 100}, {1, 3, 600}, {2, 3, 200}};
        assertEquals(700, ShortestPaths.cheapestFlightWithinKStops(4, f, 0, 3, 1));
        assertEquals(400, ShortestPaths.cheapestFlightWithinKStops(4, f, 0, 3, 2));
        assertEquals(-1, ShortestPaths.cheapestFlightWithinKStops(4, new int[][]{{0, 1, 5}}, 1, 0, 3));
        assertEquals(0, ShortestPaths.cheapestFlightWithinKStops(3, new int[][]{{0, 1, 5}}, 2, 2, 0));
        assertEquals(500, ShortestPaths.cheapestFlightWithinKStops(3, new int[][]{{0, 1, 100}, {1, 2, 100}, {0, 2, 500}}, 0, 2, 0));
    }

    @Test void randomNetworkVsFloyd() {
        Random r = new Random(21);
        for (int t = 0; t < 1500; t++) {
            int n = 1 + r.nextInt(6), m = r.nextInt(12);
            int[][] times = new int[m][];
            for (int i = 0; i < m; i++) times[i] = new int[]{1 + r.nextInt(n), 1 + r.nextInt(n), r.nextInt(6)};
            int k = 1 + r.nextInt(n);
            long[][] d = new long[n + 1][n + 1];
            for (long[] row : d) Arrays.fill(row, 1L << 40);
            for (int i = 1; i <= n; i++) d[i][i] = 0;
            for (int[] e : times) d[e[0]][e[1]] = Math.min(d[e[0]][e[1]], e[2]);
            for (int x = 1; x <= n; x++) for (int i = 1; i <= n; i++) for (int j = 1; j <= n; j++) d[i][j] = Math.min(d[i][j], d[i][x] + d[x][j]);
            long worst = 0;
            for (int j = 1; j <= n; j++) worst = Math.max(worst, d[k][j]);
            assertEquals(worst >= (1L << 40) ? -1 : (int) worst, ShortestPaths.networkDelayTime(times, n, k), Arrays.deepToString(times));
        }
    }

    @Test void randomEffortVsMinimaxFloyd() {
        Random r = new Random(22);
        for (int t = 0; t < 1500; t++) {
            int rows = 1 + r.nextInt(3), cols = 1 + r.nextInt(4);
            int[][] h = new int[rows][cols];
            for (int[] row : h) for (int c = 0; c < cols; c++) row[c] = r.nextInt(10);
            int N = rows * cols;
            int[][] d = new int[N][N];
            for (int[] row : d) Arrays.fill(row, Integer.MAX_VALUE);
            for (int i = 0; i < N; i++) d[i][i] = 0;
            for (int i = 0; i < N; i++) for (int j = 0; j < N; j++) {
                int r1 = i / cols, c1 = i % cols, r2 = j / cols, c2 = j % cols;
                if (Math.abs(r1 - r2) + Math.abs(c1 - c2) == 1) d[i][j] = Math.abs(h[r1][c1] - h[r2][c2]);
            }
            for (int x = 0; x < N; x++) for (int i = 0; i < N; i++) for (int j = 0; j < N; j++)
                d[i][j] = Math.min(d[i][j], Math.max(d[i][x], d[x][j]));
            assertEquals(d[0][N - 1], ShortestPaths.minimumEffortPath(h), Arrays.deepToString(h));
        }
    }

    @Test void randomFlightsVsWalkEnumeration() {
        Random r = new Random(23);
        for (int t = 0; t < 1500; t++) {
            int n = 1 + r.nextInt(5), m = r.nextInt(9), k = r.nextInt(4);
            int[][] f = new int[m][];
            for (int i = 0; i < m; i++) f[i] = new int[]{r.nextInt(n), r.nextInt(n), 1 + r.nextInt(9)};
            int src = r.nextInt(n), dst = r.nextInt(n);
            long best = walk(f, src, dst, k + 1);
            assertEquals(best == Long.MAX_VALUE ? -1 : (int) best, ShortestPaths.cheapestFlightWithinKStops(n, f, src, dst, k));
        }
    }

    private static long walk(int[][] f, int at, int dst, int left) {
        long best = at == dst ? 0 : Long.MAX_VALUE;
        if (left == 0) return best;
        for (int[] e : f) if (e[0] == at) {
            long sub = walk(f, e[1], dst, left - 1);
            if (sub != Long.MAX_VALUE) best = Math.min(best, sub + e[2]);
        }
        return best;
    }

    @Test void large() {
        int n = 100_000;
        Random r = new Random(24);
        int[][] times = new int[300_000][];
        for (int i = 0; i < times.length; i++) times[i] = new int[]{1 + r.nextInt(n), 1 + r.nextInt(n), r.nextInt(100)};
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> ShortestPaths.networkDelayTime(times, n, 1));
        int[][] grid = new int[1000][1000];
        for (int[] row : grid) for (int c = 0; c < row.length; c++) row[c] = r.nextInt(1_000_000);
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> ShortestPaths.minimumEffortPath(grid));
        int[][] flights = new int[100_000][];
        for (int i = 0; i < flights.length; i++) flights[i] = new int[]{r.nextInt(1000), r.nextInt(1000), 1 + r.nextInt(100)};
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> ShortestPaths.cheapestFlightWithinKStops(1000, flights, 0, 999, 20));
    }
}
