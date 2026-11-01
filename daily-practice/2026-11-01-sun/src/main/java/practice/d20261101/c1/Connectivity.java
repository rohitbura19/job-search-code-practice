package practice.d20261101.c1;

public final class Connectivity {
    private Connectivity() {}

    /** isConnected[i][j] == 1 if city i and j are directly connected (symmetric, diagonal 1). Return the number of provinces (connected groups). */
    public static int findCircleNum(int[][] isConnected) { throw new UnsupportedOperationException("TODO"); }

    /**
     * edges are undirected [u, v] with nodes 1..n, n == edges.length; the graph is a tree plus ONE extra edge.
     * Return the edge that closes the cycle; if several qualify, the one that appears last in the input. Must not recurse (n up to 10^5+).
     */
    public static int[] findRedundantConnection(int[][] edges) { throw new UnsupportedOperationException("TODO"); }

    /** Length of the longest run of consecutive integers (in any order, duplicates allowed) in O(n). Beware Integer.MIN_VALUE / MAX_VALUE. */
    public static int longestConsecutive(int[] nums) { throw new UnsupportedOperationException("TODO"); }
}
