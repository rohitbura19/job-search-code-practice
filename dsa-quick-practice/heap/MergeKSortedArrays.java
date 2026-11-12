package heap;

import java.util.Arrays;

// Merge K Sorted Arrays  [Medium | Min-heap of (value, array, index)]
// O(N log k).
//
// [[1,4,5],[1,3,4],[2,6]] -> [1,1,2,3,4,4,5,6]
// [[],[1]]                -> [1]

public class MergeKSortedArrays {

    static int[] mergeKSorted(int[][] arrays) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(mergeKSorted(new int[][]{{1, 4, 5}, {1, 3, 4}, {2, 6}}))); // [1, 1, 2, 3, 4, 4, 5, 6]
        System.out.println(Arrays.toString(mergeKSorted(new int[][]{{}, {1}})));                      // [1]
    }
}
