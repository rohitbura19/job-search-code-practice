package graphs;

// Longest Increasing Path in a Matrix  [Hard | DFS + memo, or topological layers]
// Move up/down/left/right to a strictly larger value. Cells on the longest path.
//
// [[9,9,4],[6,6,8],[2,1,1]] -> 4   (1,2,6,9)
// [[3,4,5],[3,2,6],[2,2,1]] -> 4   (3,4,5,6)

public class LongestIncreasingPath {

    static int longestIncreasingPath(int[][] matrix) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(longestIncreasingPath(new int[][]{{9, 9, 4}, {6, 6, 8}, {2, 1, 1}})); // 4
        System.out.println(longestIncreasingPath(new int[][]{{3, 4, 5}, {3, 2, 6}, {2, 2, 1}})); // 4
    }
}
