package practice.d20261101.c1;

import java.util.HashSet;
import java.util.Set;

/**
 * Union-Find with path halving and union by size: near O(1) amortised per operation (inverse Ackermann).
 * findCircleNum: union every connected pair, count roots. O(n^2 alpha) for the matrix.
 * findRedundantConnection: the first edge whose endpoints already share a root closes the cycle; scanning in order returns the last
 *   candidate because a tree plus one edge has exactly one cycle, and the edge that closes it while scanning is the last of the cycle's edges.
 * longestConsecutive: put all in a set; only start counting at x when x-1 is absent, so each run is walked once. O(n). Uses long to survive the int limits.
 */
public final class Connectivity {
    private Connectivity() {}

    private static final class Dsu {
        final int[] parent, size;
        int components;
        Dsu(int n) {
            parent = new int[n]; size = new int[n]; components = n;
            for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
        }
        int find(int x) {
            while (parent[x] != x) { parent[x] = parent[parent[x]]; x = parent[x]; }
            return x;
        }
        boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;
            if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
            parent[rb] = ra; size[ra] += size[rb]; components--;
            return true;
        }
    }

    public static int findCircleNum(int[][] m) {
        Dsu d = new Dsu(m.length);
        for (int i = 0; i < m.length; i++)
            for (int j = i + 1; j < m.length; j++)
                if (m[i][j] == 1) d.union(i, j);
        return d.components;
    }

    public static int[] findRedundantConnection(int[][] edges) {
        Dsu d = new Dsu(edges.length + 1);
        int[] last = null;
        for (int[] e : edges) if (!d.union(e[0], e[1])) last = e;
        return last;
    }

    public static int longestConsecutive(int[] nums) {
        Set<Long> set = new HashSet<>();
        for (int x : nums) set.add((long) x);
        int best = 0;
        for (long x : set) {
            if (set.contains(x - 1)) continue;
            int len = 1;
            while (set.contains(x + len)) len++;
            best = Math.max(best, len);
        }
        return best;
    }
}
