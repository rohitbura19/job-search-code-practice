package practice.d20261112.c1;

import java.util.List;

public class UnionFindProblems {
    /** isConnected[i][j] == 1 means cities i and j are directly connected (symmetric, diagonal is 1). Number of provinces (connected groups). */
    public static int findCircleNum(int[][] isConnected) { throw new UnsupportedOperationException("TODO"); }

    /** Edges of a tree on nodes 1..n plus one extra edge, nodes numbered 1..n, n = edges.length. Return the LAST edge in the input that closes a cycle. */
    public static int[] findRedundantConnection(int[][] edges) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Each account is [name, email1, email2, ...]. Two accounts belong to one person if they share an email (names may repeat for different people).
     * Return one row per person: [name, sorted distinct emails...]. Rows ordered by name, then by first email.
     */
    public static List<List<String>> accountsMerge(List<List<String>> accounts) { throw new UnsupportedOperationException("TODO"); }
}
