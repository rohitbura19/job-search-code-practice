package slidingwindow;

import java.util.Arrays;

// Sliding Window Maximum  [Hard | Monotonic deque]
// Max of every window of size k. Must be O(n).
//
// [1,3,-1,-3,5,3,6,7], k=3 -> [3,3,5,5,6,7]

public class SlidingWindowMaximum {

    static int[] maxSlidingWindow(int[] nums, int k) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3))); // [3, 3, 5, 5, 6, 7]
    }
}
