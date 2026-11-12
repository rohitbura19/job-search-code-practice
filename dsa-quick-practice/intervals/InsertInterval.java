package intervals;

import java.util.Arrays;

// Insert Interval  [Medium | Three phases: before, overlap, after]
// Sorted, non-overlapping. Insert and merge in O(n).
//
// [[1,3],[6,9]] + [2,5]                    -> [[1,5],[6,9]]
// [[1,2],[3,5],[6,7],[8,10],[12,16]] + [4,8] -> [[1,2],[3,10],[12,16]]

public class InsertInterval {

    static int[][] insert(int[][] intervals, int[] newInterval) {
        // TODO
        return new int[0][];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(insert(new int[][]{{1, 3}, {6, 9}}, new int[]{2, 5})));
        // [[1, 5], [6, 9]]
        System.out.println(Arrays.deepToString(insert(new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}}, new int[]{4, 8})));
        // [[1, 2], [3, 10], [12, 16]]
    }
}
