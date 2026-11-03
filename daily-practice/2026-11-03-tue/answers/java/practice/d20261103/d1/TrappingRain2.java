package practice.d20261103.d1;

import java.util.PriorityQueue;

/**
 * Water level at a cell = the lowest wall on the best escape route to the border. Dijkstra-like flood from the border: always pop the
 * LOWEST boundary cell (a min-heap); its unseen neighbours are enclosed by it, so a neighbour lower than it holds (boundary - height)
 * water, and then joins the boundary with height max(boundary, own). Each cell enters the heap once: O(mn log(mn)) time, O(mn) space.
 * Bound: 300*300 cells * 20_000 height = 1.8e9 < 2^31, so int is enough.
 */
public final class TrappingRain2 {
    private TrappingRain2() {}

    public static int trapRainWater(int[][] h) {
        int m = h.length;
        if (m < 3) return 0;
        int n = h[0].length;
        if (n < 3) return 0;
        boolean[][] seen = new boolean[m][n];
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (i == 0 || j == 0 || i == m - 1 || j == n - 1) { seen[i][j] = true; pq.add(new int[]{i, j, h[i][j]}); }
        int water = 0;
        int[] dr = {1, -1, 0, 0}, dc = {0, 0, 1, -1};
        while (!pq.isEmpty()) {
            int[] c = pq.poll();
            for (int d = 0; d < 4; d++) {
                int r = c[0] + dr[d], q = c[1] + dc[d];
                if (r < 0 || q < 0 || r >= m || q >= n || seen[r][q]) continue;
                seen[r][q] = true;
                water += Math.max(0, c[2] - h[r][q]);
                pq.add(new int[]{r, q, Math.max(c[2], h[r][q])});
            }
        }
        return water;
    }
}
