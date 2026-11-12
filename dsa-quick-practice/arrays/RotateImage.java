package arrays;

import java.util.Arrays;

// Rotate Image  [Medium | Transpose + reverse rows]
// Rotate an n x n matrix 90 degrees clockwise IN PLACE.
//
// [[1,2,3],[4,5,6],[7,8,9]] -> [[7,4,1],[8,5,2],[9,6,3]]

public class RotateImage {

    static void rotate(int[][] m) {
        // TODO
    }

    public static void main(String[] args) {
        int[][] m = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        rotate(m);
        System.out.println(Arrays.deepToString(m)); // [[7, 4, 1], [8, 5, 2], [9, 6, 3]]
    }
}
