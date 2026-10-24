package practice.d20261024.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class GridsTest {
    @Test void rottingExamples() {
        assertEquals(4, Grids.minutesToRot(new int[][]{{2, 1, 1}, {1, 1, 0}, {0, 1, 1}}));
        assertEquals(-1, Grids.minutesToRot(new int[][]{{2, 1, 1}, {0, 1, 1}, {1, 0, 1}}));
        assertEquals(0, Grids.minutesToRot(new int[][]{{0, 2}}));
        assertEquals(0, Grids.minutesToRot(new int[][]{{0}}), "nothing fresh");
        assertEquals(-1, Grids.minutesToRot(new int[][]{{1}}), "fresh but no source");
        assertEquals(0, Grids.minutesToRot(new int[][]{}));
        assertEquals(1, Grids.minutesToRot(new int[][]{{2, 1, 2}}), "two sources rot in parallel");
    }

    @Test void rottingDoesNotMutateTheInput() {
        int[][] g = {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}};
        Grids.minutesToRot(g);
        assertArrayEquals(new int[]{2, 1, 1}, g[0]);
        assertArrayEquals(new int[]{0, 1, 1}, g[2]);
    }

    @Test void rottingMatchesSimulation() {
        Random rnd = new Random(5);
        for (int it = 0; it < 2000; it++) {
            int r = 1 + rnd.nextInt(5), c = 1 + rnd.nextInt(5);
            int[][] g = new int[r][c];
            for (int[] row : g) for (int j = 0; j < c; j++) row[j] = rnd.nextInt(3);
            assertEquals(simulate(g), Grids.minutesToRot(g), Arrays.deepToString(g));
        }
    }

    static int simulate(int[][] g) {
        int r = g.length, c = g[0].length;
        int[][] cur = new int[r][];
        for (int i = 0; i < r; i++) cur[i] = g[i].clone();
        int t = 0;
        while (true) {
            boolean fresh = false, changed = false;
            int[][] next = new int[r][];
            for (int i = 0; i < r; i++) next[i] = cur[i].clone();
            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    if (cur[i][j] == 1) {
                        fresh = true;
                        if ((i > 0 && cur[i - 1][j] == 2) || (i < r - 1 && cur[i + 1][j] == 2)
                                || (j > 0 && cur[i][j - 1] == 2) || (j < c - 1 && cur[i][j + 1] == 2)) { next[i][j] = 2; changed = true; }
                    }
                }
            }
            if (!fresh) return t;
            if (!changed) return -1;
            cur = next;
            t++;
        }
    }

    @Test void shortestPathExamples() {
        assertEquals(2, Grids.shortestPath(new int[][]{{0, 1}, {1, 0}}));
        assertEquals(4, Grids.shortestPath(new int[][]{{0, 0, 0}, {1, 1, 0}, {1, 1, 0}}));
        assertEquals(-1, Grids.shortestPath(new int[][]{{1, 0, 0}, {1, 1, 0}, {1, 1, 0}}));
        assertEquals(-1, Grids.shortestPath(new int[][]{{0, 0, 0}, {1, 1, 0}, {1, 1, 1}}));
        assertEquals(1, Grids.shortestPath(new int[][]{{0}}));
        assertEquals(-1, Grids.shortestPath(new int[][]{{1}}));
        assertEquals(-1, Grids.shortestPath(new int[][]{{0, 1}, {1, 1}}));
    }

    @Test void largeGrids() {
        int n = 1500;
        int[][] open = new int[n][n];
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(n, Grids.shortestPath(open)));
        int[][] fresh = new int[n][n];
        for (int[] row : fresh) Arrays.fill(row, 1);
        fresh[0][0] = 2;
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(2 * (n - 1), Grids.minutesToRot(fresh)));
    }
}
