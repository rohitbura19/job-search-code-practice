package arrays;

import java.util.Arrays;

// Two Sum II - Sorted Input  [Medium | Two pointers]
// Sorted ascending. Return 1-based {i, j} with sum == target. O(1) extra space.
//
// [2,7,11,15], 9 -> [1,2]
// [2,3,4], 6     -> [1,3]

public class TwoSumSorted {

    static int[] twoSum(int[] numbers, int target) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9))); // [1, 2]
        System.out.println(Arrays.toString(twoSum(new int[]{2, 3, 4}, 6)));      // [1, 3]
    }
}
