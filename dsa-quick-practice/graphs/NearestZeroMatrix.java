package graphs;

import java.util.Arrays;

// 01 Matrix  [Medium | Multi-source BFS from all zeros]
// Distance of every cell to the nearest 0.
//
// [[0,0,0],[0,1,0],[1,1,1]] -> [[0,0,0],[0,1,0],[1,2,1]]

public class NearestZeroMatrix {

    static int[][] updateMatrix(int[][] mat) {
        // TODO
        return new int[0][];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(updateMatrix(new int[][]{{0, 0, 0}, {0, 1, 0}, {1, 1, 1}})));
        // [[0, 0, 0], [0, 1, 0], [1, 2, 1]]
    }
}
