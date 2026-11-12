package arrays;

// Maximum Product Subarray  [Medium | Track max and min]
// Largest product of a non-empty contiguous subarray.
//
// [2,3,-2,4]  -> 6
// [-2,0,-1]   -> 0
// [-2,3,-4]   -> 24

public class MaximumProductSubarray {

    static int maxProduct(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maxProduct(new int[]{2, 3, -2, 4})); // 6
        System.out.println(maxProduct(new int[]{-2, 0, -1}));   // 0
        System.out.println(maxProduct(new int[]{-2, 3, -4}));   // 24
    }
}
