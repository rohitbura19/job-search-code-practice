package arrays;

import java.util.Arrays;

// Set Matrix Zeroes  [Medium | First row/column as markers]
// If a cell is 0, set its row and column to 0. In place, O(1) extra space.
//
// [[1,1,1],[1,0,1],[1,1,1]]         -> [[1,0,1],[0,0,0],[1,0,1]]
// [[0,1,2,0],[3,4,5,2],[1,3,1,5]]   -> [[0,0,0,0],[0,4,5,0],[0,3,1,0]]

public class SetMatrixZeroes {

    static void setZeroes(int[][] m) {
        // TODO
    }

    public static void main(String[] args) {
        int[][] a = {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}};
        int[][] b = {{0, 1, 2, 0}, {3, 4, 5, 2}, {1, 3, 1, 5}};
        setZeroes(a);
        setZeroes(b);
        System.out.println(Arrays.deepToString(a)); // [[1, 0, 1], [0, 0, 0], [1, 0, 1]]
        System.out.println(Arrays.deepToString(b)); // [[0, 0, 0, 0], [0, 4, 5, 0], [0, 3, 1, 0]]
    }
}
