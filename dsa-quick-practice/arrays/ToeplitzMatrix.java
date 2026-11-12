package arrays;

// Toeplitz Matrix  [Easy | Compare with top-left neighbour]
// True if every top-left to bottom-right diagonal is constant.
//
// [[1,2,3,4],[5,1,2,3],[9,5,1,2]] -> true
// [[1,2],[2,2]]                   -> false

public class ToeplitzMatrix {

    static boolean isToeplitzMatrix(int[][] m) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isToeplitzMatrix(new int[][]{{1, 2, 3, 4}, {5, 1, 2, 3}, {9, 5, 1, 2}})); // true
        System.out.println(isToeplitzMatrix(new int[][]{{1, 2}, {2, 2}}));                          // false
    }
}
