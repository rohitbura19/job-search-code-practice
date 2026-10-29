package practice.d20261029.c1;

import java.util.List;

public final class Matrices {
    private Matrices() {}

    /**
     * Elements in clockwise spiral order, starting top-left. Any m x n (not only square), including 1 x n, m x 1, empty.
     * [[1,2,3],[4,5,6],[7,8,9]] -> [1,2,3,6,9,8,7,4,5]; [[1,2,3,4],[5,6,7,8],[9,10,11,12]] -> [1,2,3,4,8,12,11,10,9,5,6,7].
     */
    public static List<Integer> spiral(int[][] a) { throw new UnsupportedOperationException("TODO"); }

    /** Rotate a square n x n matrix 90 degrees clockwise IN PLACE (no second matrix). [[1,2],[3,4]] -> [[3,1],[4,2]]. */
    public static void rotate(int[][] a) { throw new UnsupportedOperationException("TODO"); }

    /**
     * If a cell is 0 set its whole row and column to 0, IN PLACE, using O(1) extra space (no row/column boolean arrays: reuse the
     * first row and column as markers). [[1,1,1],[1,0,1],[1,1,1]] -> [[1,0,1],[0,0,0],[1,0,1]]. Zeros created by the fill must not cascade.
     */
    public static void setZeroes(int[][] a) { throw new UnsupportedOperationException("TODO"); }
}
