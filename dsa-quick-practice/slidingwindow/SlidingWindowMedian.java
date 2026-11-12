package slidingwindow;

import java.util.Arrays;

// Sliding Window Median  [Hard | Two heaps / two TreeSets]
// Median of every window of size k. Even k: mean of the two middles (no int overflow). O(n log k).
//
// [1,3,-1,-3,5,3,6,7], k=3 -> [1.0,-1.0,-1.0,3.0,5.0,6.0]
// [1,2,3,4], k=2           -> [1.5,2.5,3.5]

public class SlidingWindowMedian {

    static double[] medianSlidingWindow(int[] nums, int k) {
        // TODO
        return new double[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(medianSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3))); // [1.0, -1.0, -1.0, 3.0, 5.0, 6.0]
        System.out.println(Arrays.toString(medianSlidingWindow(new int[]{1, 2, 3, 4}, 2)));               // [1.5, 2.5, 3.5]
    }
}
