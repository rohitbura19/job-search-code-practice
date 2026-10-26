package intervals;

// Meeting Rooms II  [Medium | Sort + min-heap of end times]
// Minimum rooms needed. A meeting [s, e] frees its room at e.
//
// [[0,30],[5,10],[15,20]] -> 2
// [[1,4],[4,5]]           -> 1

public class MeetingRoomsII {

    static int minMeetingRooms(int[][] meetings) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(minMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}})); // 2
        System.out.println(minMeetingRooms(new int[][]{{1, 4}, {4, 5}}));             // 1
    }
}
