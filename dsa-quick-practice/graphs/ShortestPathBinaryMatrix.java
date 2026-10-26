package graphs;

// Shortest Path in Binary Matrix  [Medium | BFS, 8 directions]
// n x n grid, 0 open, 1 blocked. Length in cells of the shortest path
// from top-left to bottom-right, or -1.
//
// [[0,1],[1,0]]             -> 2
// [[0,0,0],[1,1,0],[1,1,0]] -> 4

public class ShortestPathBinaryMatrix {

    static int shortestPathBinaryMatrix(int[][] grid) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(shortestPathBinaryMatrix(new int[][]{{0, 1}, {1, 0}}));                   // 2
        System.out.println(shortestPathBinaryMatrix(new int[][]{{0, 0, 0}, {1, 1, 0}, {1, 1, 0}})); // 4
    }
}
