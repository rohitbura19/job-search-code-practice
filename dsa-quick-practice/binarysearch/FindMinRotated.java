package binarysearch;

// Find Minimum in Rotated Sorted Array  [Medium | Compare mid with right]
// Distinct values, rotated (or not). O(log n).
//
// [3,4,5,1,2]      -> 1
// [4,5,6,7,0,1,2]  -> 0
// [11,13,15,17]    -> 11

public class FindMinRotated {

    static int findMin(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(findMin(new int[]{3, 4, 5, 1, 2}));       // 1
        System.out.println(findMin(new int[]{4, 5, 6, 7, 0, 1, 2})); // 0
        System.out.println(findMin(new int[]{11, 13, 15, 17}));      // 11
    }
}
