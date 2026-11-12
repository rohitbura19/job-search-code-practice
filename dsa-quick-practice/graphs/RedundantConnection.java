package graphs;

import java.util.Arrays;

// Redundant Connection  [Medium | Union-Find]
// A tree on nodes 1..n plus one extra edge. Return the edge that closes the cycle (last one in input).
//
// [[1,2],[1,3],[2,3]]                -> [2,3]
// [[1,2],[2,3],[3,4],[1,4],[1,5]]    -> [1,4]

public class RedundantConnection {

    static int[] findRedundantConnection(int[][] edges) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}})));                 // [2, 3]
        System.out.println(Arrays.toString(findRedundantConnection(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}}))); // [1, 4]
    }
}
