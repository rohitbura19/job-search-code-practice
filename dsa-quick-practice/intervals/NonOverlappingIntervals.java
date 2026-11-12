package intervals;

// Non-overlapping Intervals  [Medium | Greedy by end time]
// Minimum removals so the rest don't overlap (touching ends are fine).
//
// [[1,2],[2,3],[3,4],[1,3]] -> 1
// [[1,2],[1,2],[1,2]]       -> 2
// [[1,2],[2,3]]             -> 0

public class NonOverlappingIntervals {

    static int eraseOverlapIntervals(int[][] intervals) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(eraseOverlapIntervals(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 3}})); // 1
        System.out.println(eraseOverlapIntervals(new int[][]{{1, 2}, {1, 2}, {1, 2}}));         // 2
        System.out.println(eraseOverlapIntervals(new int[][]{{1, 2}, {2, 3}}));                 // 0
    }
}
