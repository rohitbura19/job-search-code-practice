package practice.d20261024.c2;

/**
 * Multi-source BFS: put ALL sources in the queue at distance 0 and expand level by level; the level count is the answer, and a
 * visited check on enqueue keeps it O(cells). (a) rotten oranges are the sources; -1 if a fresh one is never reached. (b) plain
 * BFS with 8 neighbours from (0,0); the distance counts cells. An int[] queue of cell indices avoids boxing on 10^6 cells.
 */
public final class Grids {
    private Grids() {}

    public static int minutesToRot(int[][] grid) {
        int rows = grid.length, cols = rows == 0 ? 0 : grid[0].length;
        int[] q = new int[rows * cols];
        int head = 0, tail = 0, fresh = 0;
        int[][] g = new int[rows][];
        for (int r = 0; r < rows; r++) {
            g[r] = grid[r].clone();
            for (int c = 0; c < cols; c++) {
                if (g[r][c] == 2) q[tail++] = r * cols + c;
                else if (g[r][c] == 1) fresh++;
            }
        }
        int minutes = 0;
        int[] dr = {1, -1, 0, 0}, dc = {0, 0, 1, -1};
        while (head < tail && fresh > 0) {
            minutes++;
            for (int end = tail; head < end; ) {
                int cell = q[head++], r = cell / cols, c = cell % cols;
                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d], nc = c + dc[d];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && g[nr][nc] == 1) {
                        g[nr][nc] = 2;
                        fresh--;
                        q[tail++] = nr * cols + nc;
                    }
                }
            }
        }
        return fresh == 0 ? minutes : -1;
    }

    public static int shortestPath(int[][] grid) {
        int n = grid.length;
        if (n == 0 || grid[0][0] == 1 || grid[n - 1][n - 1] == 1) return -1;
        boolean[] seen = new boolean[n * n];
        int[] q = new int[n * n];
        int head = 0, tail = 0, dist = 1;
        q[tail++] = 0;
        seen[0] = true;
        while (head < tail) {
            for (int end = tail; head < end; ) {
                int cell = q[head++], r = cell / n, c = cell % n;
                if (r == n - 1 && c == n - 1) return dist;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr, nc = c + dc;
                        if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] == 0 && !seen[nr * n + nc]) {
                            seen[nr * n + nc] = true;
                            q[tail++] = nr * n + nc;
                        }
                    }
                }
            }
            dist++;
        }
        return -1;
    }
}
