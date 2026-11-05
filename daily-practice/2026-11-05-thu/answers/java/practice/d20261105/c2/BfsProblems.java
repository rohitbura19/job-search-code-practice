package practice.d20261105.c2;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * BFS finds shortest paths in unweighted graphs. Multi-source BFS: push ALL sources at distance 0 and run once (O(V+E)), instead of one BFS per source.
 * orangesRotting: queue every rotten cell, process level by level, count minutes, track fresh left.
 * nearestZero: queue all zeros, spread to unvisited neighbours with dist+1.
 * openLock: states are the 10^4 codes; neighbours = 8 per state; dead ends are pre-marked visited. O(10^4 * 8).
 * All three use arrays/ints for visited, no recursion.
 */
public final class BfsProblems {
    private BfsProblems() {}

    private static final int[] DR = {1, -1, 0, 0}, DC = {0, 0, 1, -1};

    public static int orangesRotting(int[][] grid) {
        int m = grid.length, n = m == 0 ? 0 : grid[0].length, fresh = 0;
        int[][] g = new int[m][];
        Queue<int[]> q = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            g[i] = grid[i].clone();
            for (int j = 0; j < n; j++) {
                if (g[i][j] == 2) q.add(new int[] {i, j});
                else if (g[i][j] == 1) fresh++;
            }
        }
        int minutes = 0;
        while (!q.isEmpty() && fresh > 0) {
            for (int size = q.size(); size > 0; size--) {
                int[] c = q.poll();
                for (int d = 0; d < 4; d++) {
                    int r = c[0] + DR[d], cc = c[1] + DC[d];
                    if (r >= 0 && cc >= 0 && r < m && cc < n && g[r][cc] == 1) { g[r][cc] = 2; fresh--; q.add(new int[] {r, cc}); }
                }
            }
            minutes++;
        }
        return fresh == 0 ? minutes : -1;
    }

    public static int[][] nearestZero(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[][] dist = new int[m][n];
        Queue<int[]> q = new ArrayDeque<>();
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) q.add(new int[] {i, j});
                else dist[i][j] = -1;
            }
        while (!q.isEmpty()) {
            int[] c = q.poll();
            for (int d = 0; d < 4; d++) {
                int r = c[0] + DR[d], cc = c[1] + DC[d];
                if (r >= 0 && cc >= 0 && r < m && cc < n && dist[r][cc] == -1) { dist[r][cc] = dist[c[0]][c[1]] + 1; q.add(new int[] {r, cc}); }
            }
        }
        return dist;
    }

    public static int openLock(String[] deadends, String target) {
        Set<String> dead = new HashSet<>(Arrays.asList(deadends));
        if (dead.contains("0000") || dead.contains(target)) return -1;
        if (target.equals("0000")) return 0;
        Set<String> seen = new HashSet<>();
        seen.add("0000");
        Queue<String> q = new ArrayDeque<>();
        q.add("0000");
        int steps = 0;
        while (!q.isEmpty()) {
            steps++;
            for (int size = q.size(); size > 0; size--) {
                char[] cur = q.poll().toCharArray();
                for (int w = 0; w < 4; w++) {
                    char old = cur[w];
                    for (int delta : new int[] {1, 9}) {
                        cur[w] = (char) ('0' + (old - '0' + delta) % 10);
                        String next = new String(cur);
                        if (next.equals(target)) return steps;
                        if (!dead.contains(next) && seen.add(next)) q.add(next);
                    }
                    cur[w] = old;
                }
            }
        }
        return -1;
    }
}
