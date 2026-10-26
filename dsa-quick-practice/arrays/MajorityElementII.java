package arrays;

import java.util.List;

// Majority Element II  [Medium | Boyer-Moore with 2 candidates]
// Return all values that appear more than n/3 times, ascending. O(1) extra space.
//
// [3,2,3] -> [3]
// [1,2]   -> [1,2]
// [1,2,3] -> []

public class MajorityElementII {

    static List<Integer> majorityElement(int[] nums) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(majorityElement(new int[]{3, 2, 3})); // [3]
        System.out.println(majorityElement(new int[]{1, 2}));    // [1, 2]
        System.out.println(majorityElement(new int[]{1, 2, 3})); // []
    }
}
