package dp;

// Minimum Path Sum  [Medium | Grid DP, one row of memory]
// Move right or down, top-left to bottom-right. Minimum sum of cells on the path.
//
// [[1,3,1],[1,5,1],[4,2,1]] -> 7
// [[1,2,3],[4,5,6]]         -> 12

public class MinPathSum {

    static int minPathSum(int[][] grid) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(minPathSum(new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}})); // 7
        System.out.println(minPathSum(new int[][]{{1, 2, 3}, {4, 5, 6}}));            // 12
    }
}
