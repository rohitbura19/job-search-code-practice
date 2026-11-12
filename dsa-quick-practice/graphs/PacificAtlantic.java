package graphs;

import java.util.List;

// Pacific Atlantic Water Flow  [Medium | Reverse BFS from each ocean]
// Pacific = top + left edge, Atlantic = bottom + right edge. Water flows to neighbours with height <= current.
// Return cells that reach BOTH, sorted by row then col.
//
// [[1,2,2,3,5],[3,2,3,4,4],[2,4,5,3,1],[6,7,1,4,5],[5,1,1,2,4]]
//   -> [[0,4],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]

public class PacificAtlantic {

    static List<List<Integer>> pacificAtlantic(int[][] heights) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        int[][] h = {{1, 2, 2, 3, 5}, {3, 2, 3, 4, 4}, {2, 4, 5, 3, 1}, {6, 7, 1, 4, 5}, {5, 1, 1, 2, 4}};
        System.out.println(pacificAtlantic(h)); // [[0, 4], [1, 3], [1, 4], [2, 2], [3, 0], [3, 1], [4, 0]]
    }
}
