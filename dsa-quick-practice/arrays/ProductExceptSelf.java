package arrays;

import java.util.Arrays;

// Product of Array Except Self  [Medium | Prefix / suffix products]
// out[i] = product of all other elements. No division, O(n).
//
// [1,2,3,4]      -> [24,12,8,6]
// [-1,1,0,-3,3]  -> [0,0,9,0,0]

public class ProductExceptSelf {

    static int[] productExceptSelf(int[] nums) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(productExceptSelf(new int[]{1, 2, 3, 4})));      // [24, 12, 8, 6]
        System.out.println(Arrays.toString(productExceptSelf(new int[]{-1, 1, 0, -3, 3}))); // [0, 0, 9, 0, 0]
    }
}
