package arrays;

import java.util.List;

// Spiral Matrix  [Medium | Shrinking boundaries]
// Elements in clockwise spiral order from the top-left. Any m x n.
//
// [[1,2,3],[4,5,6],[7,8,9]]          -> [1,2,3,6,9,8,7,4,5]
// [[1,2,3,4],[5,6,7,8],[9,10,11,12]] -> [1,2,3,4,8,12,11,10,9,5,6,7]

public class SpiralMatrix {

    static List<Integer> spiralOrder(int[][] m) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));             // [1, 2, 3, 6, 9, 8, 7, 4, 5]
        System.out.println(spiralOrder(new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}})); // [1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7]
    }
}
