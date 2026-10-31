package practice.d20261031.c2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class GridsTest {
    static char[][] chars(String... rows) { return java.util.Arrays.stream(rows).map(String::toCharArray).toArray(char[][]::new); }

    static int[][] ints(String... rows) {
        return java.util.Arrays.stream(rows).map(s -> s.chars().map(ch -> ch - '0').toArray()).toArray(int[][]::new);
    }

    @Test void islandsExamples() {
        assertEquals(1, Grids.numIslands(chars("11110", "11010", "11000", "00000")));
        assertEquals(3, Grids.numIslands(chars("11000", "11000", "00100", "00011")));
        assertEquals(0, Grids.numIslands(new char[0][0]));
        assertEquals(0, Grids.numIslands(chars("000", "000")));
        assertEquals(1, Grids.numIslands(chars("1")));
        assertEquals(5, Grids.numIslands(chars("101", "010", "101")));      // diagonals do not connect
    }

    @Test void areaExamples() {
        assertEquals(3, Grids.maxAreaOfIsland(ints("0110", "0100", "0001", "0011")));
        assertEquals(0, Grids.maxAreaOfIsland(ints("000")));
        assertEquals(0, Grids.maxAreaOfIsland(new int[0][0]));
        assertEquals(6, Grids.maxAreaOfIsland(ints("111", "111")));
    }

    @Test void pacificAtlanticExample() {
        int[][] h = {{1, 2, 2, 3, 5}, {3, 2, 3, 4, 4}, {2, 4, 5, 3, 1}, {6, 7, 1, 4, 5}, {5, 1, 1, 2, 4}};
        assertEquals("[[0, 4], [1, 3], [1, 4], [2, 2], [3, 0], [3, 1], [4, 0]]", Grids.pacificAtlantic(h).toString());
        assertEquals("[[0, 0]]", Grids.pacificAtlantic(new int[][] {{7}}).toString());
        assertEquals(List.of(), Grids.pacificAtlantic(new int[0][0]));
        // flat grid: every cell reaches both oceans
        assertEquals(6, Grids.pacificAtlantic(new int[][] {{1, 1, 1}, {1, 1, 1}}).size());
    }

    static int bruteIslands(char[][] g) {
        boolean[][] seen = new boolean[g.length][g.length == 0 ? 0 : g[0].length];
        int n = 0;
        for (int r = 0; r < g.length; r++) for (int c = 0; c < g[0].length; c++) if (g[r][c] == '1' && !seen[r][c]) { n++; fill(g, seen, r, c); }
        return n;
    }

    static void fill(char[][] g, boolean[][] seen, int r, int c) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || seen[r][c] || g[r][c] != '1') return;
        seen[r][c] = true;
        fill(g, seen, r + 1, c); fill(g, seen, r - 1, c); fill(g, seen, r, c + 1); fill(g, seen, r, c - 1);
    }

    static boolean reaches(int[][] h, int r, int c, boolean[][] seen, boolean pacific) {
        if (pacific ? (r == 0 || c == 0) : (r == h.length - 1 || c == h[0].length - 1)) return true;
        seen[r][c] = true;
        int[] dr = {1, -1, 0, 0}, dc = {0, 0, 1, -1};
        for (int d = 0; d < 4; d++) {
            int nr = r + dr[d], nc = c + dc[d];
            if (nr >= 0 && nc >= 0 && nr < h.length && nc < h[0].length && !seen[nr][nc] && h[nr][nc] <= h[r][c] && reaches(h, nr, nc, seen, pacific)) return true;
        }
        return false;
    }

    @Test void randomVsBrute() {
        Random rnd = new Random(7);
        for (int t = 0; t < 200; t++) {
            int R = 1 + rnd.nextInt(7), C = 1 + rnd.nextInt(7);
            char[][] g = new char[R][C];
            int[][] ig = new int[R][C], h = new int[R][C];
            for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) { ig[r][c] = rnd.nextInt(2); g[r][c] = (char) ('0' + ig[r][c]); h[r][c] = rnd.nextInt(4); }
            int expectIslands = bruteIslands(copy(g));
            assertEquals(expectIslands, Grids.numIslands(g));
            List<List<Integer>> expect = new ArrayList<>();
            for (int r = 0; r < R; r++) for (int c = 0; c < C; c++)
                if (reaches(h, r, c, new boolean[R][C], true) && reaches(h, r, c, new boolean[R][C], false)) expect.add(List.of(r, c));
            assertEquals(expect, Grids.pacificAtlantic(h));
            int area = Grids.maxAreaOfIsland(ig);
            assertEquals(expectIslands == 0, area == 0);
        }
    }

    static char[][] copy(char[][] g) { return java.util.Arrays.stream(g).map(char[]::clone).toArray(char[][]::new); }

    @Test void hugeGridNoStackOverflowAndFast() {
        int n = 1000;
        char[][] g = new char[n][n];
        int[][] a = new int[n][n], h = new int[n][n];
        for (int r = 0; r < n; r++) for (int c = 0; c < n; c++) { g[r][c] = '1'; a[r][c] = 1; h[r][c] = r + c; }
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            assertEquals(1, Grids.numIslands(g));
            assertEquals(n * n, Grids.maxAreaOfIsland(a));
            // heights r + c: everything reaches the Pacific, only the bottom row and right column reach the Atlantic
            assertEquals(2 * n - 1, Grids.pacificAtlantic(h).size());
        });
    }
}
