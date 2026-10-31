package practice.d20261031.c2;

import java.util.List;

public final class Grids {
    private Grids() {}

    /** Number of 4-directionally connected groups of '1'. Grid can be 1000 x 1000: no recursion. Empty grid -> 0. */
    public static int numIslands(char[][] grid) { throw new UnsupportedOperationException("TODO"); }

    /** Largest island area (count of connected 1s), 0 if none. */
    public static int maxAreaOfIsland(int[][] grid) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Pacific = top row + left column, Atlantic = bottom row + right column. Water flows to a neighbour with height <= current.
     * Return every [row, col] that reaches BOTH oceans, sorted by row then col. Do NOT run a search from every cell.
     */
    public static List<List<Integer>> pacificAtlantic(int[][] heights) { throw new UnsupportedOperationException("TODO"); }
}
