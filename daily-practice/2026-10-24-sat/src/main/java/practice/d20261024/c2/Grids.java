package practice.d20261024.c2;

public final class Grids {
    private Grids() {}

    /**
     * 0 empty, 1 fresh, 2 rotten. Each minute a fresh orange 4-adjacent to a rotten one rots. Minutes until no fresh orange is
     * left, 0 if there was none, -1 if some can never rot. Do not mutate the input. O(rows * cols).
     */
    public static int minutesToRot(int[][] grid) {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * n x n grid of 0 (open) / 1 (blocked). Length (number of cells) of the shortest path from (0,0) to (n-1,n-1) moving in 8
     * directions through open cells, or -1. n = 0 gives -1.
     */
    public static int shortestPath(int[][] grid) {
        throw new UnsupportedOperationException("TODO");
    }
}
