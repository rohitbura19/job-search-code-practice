package hashing;

import java.util.Arrays;

// Two Sum  [Easy | HashMap of value -> index]
// Exactly one pair i < j with nums[i] + nums[j] == target. Return {i, j}. O(n).
//
// [2,7,11,15], 9 -> [0,1]
// [3,2,4], 6     -> [1,2]

public class TwoSum {

    static int[] twoSum(int[] nums, int target) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9))); // [0, 1]
        System.out.println(Arrays.toString(twoSum(new int[]{3, 2, 4}, 6)));      // [1, 2]
    }
}
