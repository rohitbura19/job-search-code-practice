package arrays;

// Maximum Subarray  [Medium | Kadane]
// Largest sum of a non-empty contiguous subarray.
//
// [-2,1,-3,4,-1,2,1,-5,4] -> 6   ([4,-1,2,1])
// [-3,-1,-2]              -> -1

public class MaximumSubarray {

    static int maxSubArray(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4})); // 6
        System.out.println(maxSubArray(new int[]{-3, -1, -2}));                    // -1
    }
}
