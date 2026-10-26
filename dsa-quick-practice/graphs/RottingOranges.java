package graphs;

// Rotting Oranges  [Medium | Multi-source BFS]
// 0 empty, 1 fresh, 2 rotten. Each minute, fresh oranges next to rotten ones rot.
// Minutes until none is fresh, or -1 if impossible.
//
// [[2,1,1],[1,1,0],[0,1,1]] -> 4
// [[2,1,1],[0,1,1],[1,0,1]] -> -1

public class RottingOranges {

    static int orangesRotting(int[][] grid) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(orangesRotting(new int[][]{{2, 1, 1}, {1, 1, 0}, {0, 1, 1}})); // 4
        System.out.println(orangesRotting(new int[][]{{2, 1, 1}, {0, 1, 1}, {1, 0, 1}})); // -1
    }
}
