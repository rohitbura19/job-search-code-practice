package intervals;

import java.util.Arrays;

// Merge Intervals  [Medium | Sort by start]
// Merge all overlapping intervals. Touching ones ([1,4],[4,5]) merge.
//
// [[1,3],[2,6],[8,10],[15,18]] -> [[1,6],[8,10],[15,18]]

public class MergeIntervals {

    static int[][] merge(int[][] intervals) {
        // TODO
        return new int[0][];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.deepToString(merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}}))); // [[1, 6], [8, 10], [15, 18]]
        System.out.println(Arrays.deepToString(merge(new int[][]{{1, 4}, {4, 5}})));                    // [[1, 5]]
    }
}
