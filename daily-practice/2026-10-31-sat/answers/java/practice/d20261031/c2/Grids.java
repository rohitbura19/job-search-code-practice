package practice.d20261031.c2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * numIslands / maxAreaOfIsland: scan; on an unvisited land cell flood-fill with an explicit stack (marking visited in place,
 * restoring is not needed here because the grid is the caller's scratch input; copy first if it must stay intact).
 * O(R*C) time, O(R*C) worst-case stack.
 * pacificAtlantic: reverse the flow. Start from each ocean's border cells and climb to neighbours with height >= current; the
 * answer is the intersection of the two reachable sets. Two floods instead of R*C floods: O(R*C) instead of O((R*C)^2).
 */
public final class Grids {
    private Grids() {}

    private static final int[] DR = {1, -1, 0, 0}, DC = {0, 0, 1, -1};

    public static int numIslands(char[][] grid) {
        int count = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[r].length; c++)
                if (grid[r][c] == '1') { count++; sink(grid, r, c); }
        return count;
    }

    private static void sink(char[][] g, int r0, int c0) {
        Deque<int[]> st = new ArrayDeque<>();
        g[r0][c0] = '0';
        st.push(new int[] {r0, c0});
        while (!st.isEmpty()) {
            int[] p = st.pop();
            for (int d = 0; d < 4; d++) {
                int r = p[0] + DR[d], c = p[1] + DC[d];
                if (r >= 0 && c >= 0 && r < g.length && c < g[r].length && g[r][c] == '1') { g[r][c] = '0'; st.push(new int[] {r, c}); }
            }
        }
    }

    public static int maxAreaOfIsland(int[][] grid) {
        int best = 0;
        for (int r = 0; r < grid.length; r++)
            for (int c = 0; c < grid[r].length; c++)
                if (grid[r][c] == 1) {
                    int area = 0;
                    Deque<int[]> st = new ArrayDeque<>();
                    grid[r][c] = 0;
                    st.push(new int[] {r, c});
                    while (!st.isEmpty()) {
                        int[] p = st.pop();
                        area++;
                        for (int d = 0; d < 4; d++) {
                            int nr = p[0] + DR[d], nc = p[1] + DC[d];
                            if (nr >= 0 && nc >= 0 && nr < grid.length && nc < grid[nr].length && grid[nr][nc] == 1) { grid[nr][nc] = 0; st.push(new int[] {nr, nc}); }
                        }
                    }
                    best = Math.max(best, area);
                }
        return best;
    }

    public static List<List<Integer>> pacificAtlantic(int[][] h) {
        List<List<Integer>> out = new ArrayList<>();
        if (h.length == 0 || h[0].length == 0) return out;
        int R = h.length, C = h[0].length;
        boolean[][] pac = new boolean[R][C], atl = new boolean[R][C];
        for (int r = 0; r < R; r++) { climb(h, pac, r, 0); climb(h, atl, r, C - 1); }
        for (int c = 0; c < C; c++) { climb(h, pac, 0, c); climb(h, atl, R - 1, c); }
        for (int r = 0; r < R; r++) for (int c = 0; c < C; c++) if (pac[r][c] && atl[r][c]) out.add(List.of(r, c));
        return out;
    }

    private static void climb(int[][] h, boolean[][] seen, int r0, int c0) {
        if (seen[r0][c0]) return;
        Deque<int[]> st = new ArrayDeque<>();
        seen[r0][c0] = true;
        st.push(new int[] {r0, c0});
        while (!st.isEmpty()) {
            int[] p = st.pop();
            for (int d = 0; d < 4; d++) {
                int r = p[0] + DR[d], c = p[1] + DC[d];
                if (r >= 0 && c >= 0 && r < h.length && c < h[0].length && !seen[r][c] && h[r][c] >= h[p[0]][p[1]]) { seen[r][c] = true; st.push(new int[] {r, c}); }
            }
        }
    }
}
