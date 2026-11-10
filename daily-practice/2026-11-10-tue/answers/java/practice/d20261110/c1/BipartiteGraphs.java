package practice.d20261110.c1;

import java.util.*;

/**
 * Bipartite: BFS 2-colouring per component; an edge between equal colours is an odd cycle. O(V + E). possibleBipartition builds the graph then reuses it.
 * eventualSafeNodes: reverse the edges and run Kahn's algorithm from the terminals - a node becomes safe when all its out-neighbours are safe
 * (out-degree counter hits 0). O(V + E), no recursion, so no stack overflow on a 200k-long chain.
 */
public class BipartiteGraphs {
    public static boolean isBipartite(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n];            // 0 = unseen, 1 / 2 = sides
        int[] queue = new int[n];
        for (int s = 0; s < n; s++) {
            if (color[s] != 0) continue;
            int head = 0, tail = 0;
            queue[tail++] = s;
            color[s] = 1;
            while (head < tail) {
                int u = queue[head++];
                for (int v : graph[u]) {
                    if (color[v] == 0) { color[v] = 3 - color[u]; queue[tail++] = v; }
                    else if (color[v] == color[u]) return false;
                }
            }
        }
        return true;
    }

    public static boolean possibleBipartition(int n, int[][] dislikes) {
        int[] deg = new int[n];
        for (int[] d : dislikes) { deg[d[0] - 1]++; deg[d[1] - 1]++; }
        int[][] g = new int[n][];
        for (int i = 0; i < n; i++) g[i] = new int[deg[i]];
        int[] fill = new int[n];
        for (int[] d : dislikes) {
            int a = d[0] - 1, b = d[1] - 1;
            g[a][fill[a]++] = b;
            g[b][fill[b]++] = a;
        }
        return isBipartite(g);
    }

    public static List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        int[] outLeft = new int[n];
        int[] inDeg = new int[n];
        for (int u = 0; u < n; u++) { outLeft[u] = graph[u].length; for (int v : graph[u]) inDeg[v]++; }
        int[][] rev = new int[n][];
        for (int v = 0; v < n; v++) rev[v] = new int[inDeg[v]];
        int[] fill = new int[n];
        for (int u = 0; u < n; u++) for (int v : graph[u]) rev[v][fill[v]++] = u;
        int[] queue = new int[n];
        int head = 0, tail = 0;
        boolean[] safe = new boolean[n];
        for (int u = 0; u < n; u++) if (outLeft[u] == 0) { queue[tail++] = u; safe[u] = true; }
        while (head < tail) {
            int v = queue[head++];
            for (int u : rev[v]) if (--outLeft[u] == 0) { safe[u] = true; queue[tail++] = u; }
        }
        List<Integer> res = new ArrayList<>();
        for (int u = 0; u < n; u++) if (safe[u]) res.add(u);
        return res;
    }
}
