package arrays;

// Maximum Sum Circular Subarray  [Medium | Kadane max and min]
// The subarray may wrap around the end. Non-empty.
//
// [1,-2,3,-2]  -> 3
// [5,-3,5]     -> 10
// [-3,-2,-3]   -> -2

public class MaxCircularSubarray {

    static int maxSubarraySumCircular(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maxSubarraySumCircular(new int[]{1, -2, 3, -2})); // 3
        System.out.println(maxSubarraySumCircular(new int[]{5, -3, 5}));     // 10
        System.out.println(maxSubarraySumCircular(new int[]{-3, -2, -3}));   // -2
    }
}
