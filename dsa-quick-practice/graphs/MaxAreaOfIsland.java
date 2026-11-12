package graphs;

// Max Area of Island  [Medium | Grid DFS/BFS]
// Largest group of 4-connected 1s; 0 if none.
//
// [[0,0,1,0],[0,1,1,0],[0,1,0,0],[1,0,0,1]] -> 4
// [[0,0,0]]                                 -> 0

public class MaxAreaOfIsland {

    static int maxAreaOfIsland(int[][] grid) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maxAreaOfIsland(new int[][]{{0, 0, 1, 0}, {0, 1, 1, 0}, {0, 1, 0, 0}, {1, 0, 0, 1}})); // 4
        System.out.println(maxAreaOfIsland(new int[][]{{0, 0, 0}}));                                              // 0
    }
}
