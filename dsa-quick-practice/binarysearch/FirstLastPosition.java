package binarysearch;

import java.util.Arrays;

// Find First and Last Position of Element  [Medium | Two lower-bound searches]
// Sorted with duplicates. {first, last} or {-1, -1}. O(log n).
//
// [5,7,7,8,8,10], 8 -> [3,4]
// [5,7,7,8,8,10], 6 -> [-1,-1]

public class FirstLastPosition {

    static int[] searchRange(int[] nums, int target) {
        // TODO
        return new int[]{-1, -1};
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 8))); // [3, 4]
        System.out.println(Arrays.toString(searchRange(new int[]{5, 7, 7, 8, 8, 10}, 6))); // [-1, -1]
    }
}
