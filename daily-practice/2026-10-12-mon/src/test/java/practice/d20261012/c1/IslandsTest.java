package practice.d20261012.c1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class IslandsTest {

    static char[][] grid(String... rows) {
        char[][] g = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
        return g;
    }

    /** Test-side oracle: recursive flood fill on a copy; only used on small grids. */
    static int oracle(char[][] g) {
        int rows = g.length, cols = rows == 0 ? 0 : g[0].length, n = 0;
        boolean[][] seen = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) for (int c = 0; c < cols; c++)
            if (g[r][c] == '1' && !seen[r][c]) { n++; fill(g, seen, r, c); }
        return n;
    }
    static void fill(char[][] g, boolean[][] seen, int r, int c) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length || g[r][c] != '1' || seen[r][c]) return;
        seen[r][c] = true;
        fill(g, seen, r + 1, c); fill(g, seen, r - 1, c); fill(g, seen, r, c + 1); fill(g, seen, r, c - 1);
    }

    @Test @DisplayName("example from the question -> 3")
    void example() {
        assertEquals(3, Islands.numIslands(grid("11000", "11000", "00100", "00011")));
    }

    @Test @DisplayName("empty grids, single cells, all water")
    void edges() {
        assertEquals(0, Islands.numIslands(new char[0][0]));
        assertEquals(0, Islands.numIslands(new char[][]{{}}));
        assertEquals(1, Islands.numIslands(grid("1")));
        assertEquals(0, Islands.numIslands(grid("0")));
        assertEquals(0, Islands.numIslands(grid("000", "000")));
        assertEquals(1, Islands.numIslands(grid("1", "1", "1")));
    }

    @Test @DisplayName("diagonal cells are NOT connected")
    void diagonals() {
        assertEquals(2, Islands.numIslands(grid("10", "01")));
        assertEquals(1, Islands.numIslands(grid("101", "111")));
    }

    @Test @DisplayName("the input grid is not modified")
    void inputPreserved() {
        char[][] g = grid("11000", "11000", "00100", "00011");
        char[][] copy = new char[g.length][];
        for (int i = 0; i < g.length; i++) copy[i] = g[i].clone();
        Islands.numIslands(g);
        assertEquals(Arrays.deepToString(copy), Arrays.deepToString(g));
    }

    @Test @DisplayName("1000 x 1000 all land: one island, no stack overflow")
    void allLand() {
        char[][] g = new char[1000][1000];
        for (char[] row : g) Arrays.fill(row, '1');
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(1, Islands.numIslands(g)));
    }

    @Test @DisplayName("1000 x 1000 checkerboard: 500,000 islands")
    void checkerboard() {
        char[][] g = new char[1000][1000];
        for (int r = 0; r < 1000; r++) for (int c = 0; c < 1000; c++) g[r][c] = (r + c) % 2 == 0 ? '1' : '0';
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(500_000, Islands.numIslands(g)));
    }

    @Test @DisplayName("a snake-shaped island (long thin path) is counted as one")
    void snake() {
        int n = 999; char[][] g = new char[n][n];
        for (char[] row : g) Arrays.fill(row, '0');
        for (int r = 0; r < n; r += 2) {
            Arrays.fill(g[r], '1');
            if (r + 1 < n) g[r + 1][(r / 2) % 2 == 0 ? n - 1 : 0] = '1';
        }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> assertEquals(1, Islands.numIslands(g)));
    }

    @Test @DisplayName("random small grids match the oracle")
    void randomOracle() {
        Random rnd = new Random(9);
        for (int t = 0; t < 2000; t++) {
            int rows = rnd.nextInt(7) + 1, cols = rnd.nextInt(7) + 1;
            char[][] g = new char[rows][cols];
            for (char[] row : g) for (int c = 0; c < cols; c++) row[c] = rnd.nextInt(100) < 55 ? '1' : '0';
            assertEquals(oracle(g), Islands.numIslands(g), Arrays.deepToString(g));
        }
    }

    // ---------- Number of Islands II ----------

    @Test @DisplayName("IslandCounter: classic sequence on a 3 x 3 grid")
    void counterExample() {
        IslandCounter ic = new IslandCounter(3, 3);
        assertEquals(1, ic.addLand(0, 0));
        assertEquals(1, ic.addLand(0, 1));
        assertEquals(2, ic.addLand(1, 2));
        assertEquals(3, ic.addLand(2, 1));
        assertEquals(1, ic.addLand(1, 1));      // joins (0,1)-(0,0), (1,2) and (2,1) into one island
    }

    @Test @DisplayName("IslandCounter: adding the same cell twice does not change the count")
    void counterDuplicates() {
        IslandCounter ic = new IslandCounter(2, 2);
        assertEquals(1, ic.addLand(0, 0));
        assertEquals(1, ic.addLand(0, 0));
        assertEquals(2, ic.addLand(1, 1));
        assertEquals(2, ic.addLand(1, 1));
    }

    @Test @DisplayName("IslandCounter: 1 x 1 grid")
    void counterSingle() {
        IslandCounter ic = new IslandCounter(1, 1);
        assertEquals(1, ic.addLand(0, 0));
        assertEquals(1, ic.addLand(0, 0));
    }

    @Test @DisplayName("IslandCounter: random additions match a full recount after every step")
    void counterRandom() {
        Random rnd = new Random(21);
        for (int t = 0; t < 200; t++) {
            int rows = rnd.nextInt(6) + 1, cols = rnd.nextInt(6) + 1;
            char[][] g = new char[rows][cols];
            for (char[] row : g) Arrays.fill(row, '0');
            IslandCounter ic = new IslandCounter(rows, cols);
            for (int s = 0; s < 25; s++) {
                int r = rnd.nextInt(rows), c = rnd.nextInt(cols);
                g[r][c] = '1';
                assertEquals(oracle(g), ic.addLand(r, c), "step " + s + " " + Arrays.deepToString(g));
            }
        }
    }

    @Test @DisplayName("IslandCounter: 1,000,000 additions finish fast (a recount per step would take forever)")
    void counterBig() {
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            IslandCounter ic = new IslandCounter(1000, 1000);
            int last = -1;
            for (int r = 0; r < 1000; r++) for (int c = 0; c < 1000; c++) last = ic.addLand(r, c);
            assertEquals(1, last);
        });
    }
}
