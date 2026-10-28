package practice.d20261028.c2;

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * networkDelay: Dijkstra with a binary heap and a CSR (array) adjacency; skip stale heap entries. O((V + E) log V).
 * cheapestWithStops: Dijkstra is wrong here (cheapest path may use too many edges), so run Bellman-Ford for k + 1 rounds,
 * relaxing from a COPY of the previous round so that each round adds exactly one flight. O(k * E), O(V).
 */
public final class ShortestPaths {
    private ShortestPaths() {}

    public static int networkDelay(int[][] times, int n, int k) {
        int[] head = new int[n + 2];
        for (int[] t : times) head[t[0] + 1]++;
        for (int i = 1; i < head.length; i++) head[i] += head[i - 1];
        int[] to = new int[times.length], w = new int[times.length], fill = Arrays.copyOf(head, head.length);
        for (int[] t : times) { int p = fill[t[0]]++; to[p] = t[1]; w[p] = t[2]; }
        long[] dist = new long[n + 1];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[k] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        pq.add(new long[] {0, k});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[1];
            if (cur[0] > dist[u]) continue;
            for (int e = head[u]; e < head[u + 1]; e++) {
                long nd = cur[0] + w[e];
                if (nd < dist[to[e]]) { dist[to[e]] = nd; pq.add(new long[] {nd, to[e]}); }
            }
        }
        long max = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Long.MAX_VALUE) return -1;
            max = Math.max(max, dist[i]);
        }
        return (int) max;
    }

    public static int cheapestWithStops(int n, int[][] flights, int src, int dst, int k) {
        final long INF = Long.MAX_VALUE / 2;
        long[] best = new long[n];
        Arrays.fill(best, INF);
        best[src] = 0;
        for (int round = 0; round <= k; round++) {
            long[] next = best.clone();
            for (int[] f : flights) {
                if (best[f[0]] < INF && best[f[0]] + f[2] < next[f[1]]) next[f[1]] = best[f[0]] + f[2];
            }
            best = next;
        }
        return best[dst] >= INF ? -1 : (int) best[dst];
    }
}
