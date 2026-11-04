package practice.d20261104.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class LongestIncreasingPathTest {
    @Test void examples() {
        assertEquals(4, LongestIncreasingPath.longestIncreasingPath(new int[][]{{9, 9, 4}, {6, 6, 8}, {2, 1, 1}}));   // 1 2 6 9
        assertEquals(4, LongestIncreasingPath.longestIncreasingPath(new int[][]{{3, 4, 5}, {3, 2, 6}, {2, 2, 1}}));   // 3 4 5 6
        assertEquals(1, LongestIncreasingPath.longestIncreasingPath(new int[][]{{1}}));
    }

    @Test void flatAndEqualNeighboursDoNotCount() {
        assertEquals(1, LongestIncreasingPath.longestIncreasingPath(new int[][]{{7, 7}, {7, 7}}));
        assertEquals(2, LongestIncreasingPath.longestIncreasingPath(new int[][]{{1, 1, 2}}));
        assertEquals(3, LongestIncreasingPath.longestIncreasingPath(new int[][]{{1}, {2}, {3}}));
        assertEquals(3, LongestIncreasingPath.longestIncreasingPath(new int[][]{{3}, {2}, {1}}));
    }

    @Test void extremeValuesAndNoDiagonals() {
        assertEquals(2, LongestIncreasingPath.longestIncreasingPath(new int[][]{{Integer.MIN_VALUE, Integer.MAX_VALUE}}));
        assertEquals(3, LongestIncreasingPath.longestIncreasingPath(new int[][]{{1, 5}, {5, 9}}));
        assertEquals(2, LongestIncreasingPath.longestIncreasingPath(new int[][]{{1, 9}, {9, 2}}), "diagonal 1 -> 2 is not allowed, 1 -> 9 is");
    }

    @Test void snakeOfFortyThousandCellsDoesNotOverflowTheStack() {
        int n = 200;
        int[][] g = new int[n][n];
        int v = 0;
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++) g[r][r % 2 == 0 ? c : n - 1 - c] = v++;
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(n * n, LongestIncreasingPath.longestIncreasingPath(g)));
    }

    @Test void randomVsPlainDfs() {
        Random rnd = new Random(9);
        for (int t = 0; t < 400; t++) {
            int r = 1 + rnd.nextInt(5), c = 1 + rnd.nextInt(5);
            int[][] g = new int[r][c];
            for (int[] row : g) for (int j = 0; j < c; j++) row[j] = rnd.nextInt(7);
            int best = 0;
            for (int i = 0; i < r; i++) for (int j = 0; j < c; j++) best = Math.max(best, dfs(g, i, j));
            assertEquals(best, LongestIncreasingPath.longestIncreasingPath(g), Arrays.deepToString(g));
        }
    }

    static int dfs(int[][] g, int i, int j) {
        int best = 1;
        int[] dr = {1, -1, 0, 0}, dc = {0, 0, 1, -1};
        for (int d = 0; d < 4; d++) {
            int a = i + dr[d], b = j + dc[d];
            if (a >= 0 && a < g.length && b >= 0 && b < g[0].length && g[a][b] > g[i][j]) best = Math.max(best, 1 + dfs(g, a, b));
        }
        return best;
    }
}
