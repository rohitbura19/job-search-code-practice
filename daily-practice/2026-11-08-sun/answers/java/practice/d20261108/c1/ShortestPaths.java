package practice.d20261108.c1;

import java.util.*;

/**
 * Network delay: Dijkstra from k, answer = max distance. Min effort: Dijkstra where the "distance" of a cell is the
 * minimax edge on the best route (max instead of +; still monotone, so Dijkstra is valid). Cheapest flights: Bellman-Ford
 * limited to k+1 rounds, relaxing from a copy of the previous round (otherwise a round could chain several flights).
 * Dijkstra: O((V+E) log V). Bellman-Ford: O(k*E).
 */
public final class ShortestPaths {
    private ShortestPaths() {}

    public static int networkDelayTime(int[][] times, int n, int k) {
        List<List<int[]>> g = new ArrayList<>();
        for (int i = 0; i <= n; i++) g.add(new ArrayList<>());
        for (int[] e : times) g.get(e[0]).add(new int[]{e[1], e[2]});
        long[] dist = new long[n + 1];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[k] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.add(new long[]{0, k});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[1];
            if (cur[0] > dist[u]) continue;
            for (int[] e : g.get(u)) {
                long nd = cur[0] + e[1];
                if (nd < dist[e[0]]) { dist[e[0]] = nd; pq.add(new long[]{nd, e[0]}); }
            }
        }
        long max = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Long.MAX_VALUE) return -1;
            max = Math.max(max, dist[i]);
        }
        return (int) max;
    }

    public static int minimumEffortPath(int[][] h) {
        int rows = h.length, cols = h[0].length;
        int[][] best = new int[rows][cols];
        for (int[] r : best) Arrays.fill(r, Integer.MAX_VALUE);
        best[0][0] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[]{0, 0, 0});
        int[] dr = {1, -1, 0, 0}, dc = {0, 0, 1, -1};
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int r = cur[1], c = cur[2];
            if (cur[0] > best[r][c]) continue;
            if (r == rows - 1 && c == cols - 1) return cur[0];
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d], nc = c + dc[d];
                if (nr < 0 || nc < 0 || nr >= rows || nc >= cols) continue;
                int effort = Math.max(cur[0], Math.abs(h[nr][nc] - h[r][c]));
                if (effort < best[nr][nc]) { best[nr][nc] = effort; pq.add(new int[]{effort, nr, nc}); }
            }
        }
        return 0;
    }

    public static int cheapestFlightWithinKStops(int n, int[][] flights, int src, int dst, int k) {
        long INF = Long.MAX_VALUE / 4;
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        for (int round = 0; round <= k; round++) {
            long[] next = dist.clone();
            for (int[] f : flights) if (dist[f[0]] < INF) next[f[1]] = Math.min(next[f[1]], dist[f[0]] + f[2]);
            dist = next;
        }
        return dist[dst] >= INF ? -1 : (int) dist[dst];
    }
}
