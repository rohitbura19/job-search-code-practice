package practice.d20261110.c1;

import java.util.List;

public class BipartiteGraphs {
    /** Undirected graph as adjacency lists (graph[u] lists neighbours, symmetric, may be disconnected, no self-loops). Can nodes be 2-coloured? Iterative (n up to 200,000). */
    public static boolean isBipartite(int[][] graph) { throw new UnsupportedOperationException("TODO"); }

    /** People 1..n, dislikes[i] = {a, b}: a and b cannot be in the same group. Can everyone be split into two groups? Duplicate pairs possible. */
    public static boolean possibleBipartition(int n, int[][] dislikes) { throw new UnsupportedOperationException("TODO"); }

    /** Directed graph. A node is safe if every path from it ends at a terminal node (no outgoing edges) - i.e. it is not on or before a cycle. Return safe nodes ascending. Iterative. */
    public static List<Integer> eventualSafeNodes(int[][] graph) { throw new UnsupportedOperationException("TODO"); }
}
