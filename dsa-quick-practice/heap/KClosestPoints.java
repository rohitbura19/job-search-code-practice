package heap;

import java.util.Arrays;

// K Closest Points to Origin  [Medium | Max-heap of size k]
// Use squared distance as long. Return sorted by distance.
//
// [[1,3],[-2,2]], k=1        -> [[-2,2]]
// [[3,3],[5,-1],[-2,4]], k=2 -> [[3,3],[-2,4]]

public class KClosestPoints {

    static int[][] kClosest(int[][] points, int k) {
        // TODO
        return new int[0][];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(kClosest(new int[][]{{1, 3}, {-2, 2}}, 1)));          // [[-2, 2]]
        System.out.println(Arrays.deepToString(kClosest(new int[][]{{3, 3}, {5, -1}, {-2, 4}}, 2))); // [[3, 3], [-2, 4]]
    }
}
