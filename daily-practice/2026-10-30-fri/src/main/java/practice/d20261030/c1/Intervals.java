package practice.d20261030.c1;

public final class Intervals {
    private Intervals() {}

    /**
     * Merge overlapping CLOSED intervals [start, end]; intervals that only touch ([1,4] and [4,5]) merge too, [1,2] and [3,4] do not.
     * Input is unsorted, may contain duplicates and values near Integer.MIN_VALUE / MAX_VALUE (do not subtract to compare).
     * Do not modify the argument. Result sorted by start. [[1,3],[8,10],[2,6]] -> [[1,6],[8,10]]. Empty -> empty.
     */
    public static int[][] merge(int[][] intervals) { throw new UnsupportedOperationException("TODO"); }

    /**
     * `sorted` is already sorted by start with no overlaps. Insert `x`, merging whatever it overlaps or touches, in O(n) without sorting.
     * [[1,2],[3,5],[6,7],[8,10]] + [4,8] -> [[1,2],[3,10]].
     */
    public static int[][] insert(int[][] sorted, int[] x) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Minimum number of rooms for meetings [start, end) HALF-open: a meeting ending at 5 and one starting at 5 can share a room.
     * [[0,30],[5,10],[15,20]] -> 2; [[0,5],[5,10]] -> 1; empty -> 0. Must handle 2*10^5 meetings fast.
     */
    public static int minMeetingRooms(int[][] meetings) { throw new UnsupportedOperationException("TODO"); }
}
