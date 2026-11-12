package arrays;

import java.util.Arrays;

// Rotate Array  [Medium | Three reversals]
// Rotate right by k in place (k may exceed n). O(1) space.
//
// [1,2,3,4,5,6,7], k=3 -> [5,6,7,1,2,3,4]
// [-1,-100,3,99], k=2  -> [3,99,-1,-100]

public class RotateArray {

    static void rotate(int[] nums, int k) {
        // TODO
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5, 6, 7}, b = {-1, -100, 3, 99};
        rotate(a, 3);
        rotate(b, 2);
        System.out.println(Arrays.toString(a)); // [5, 6, 7, 1, 2, 3, 4]
        System.out.println(Arrays.toString(b)); // [3, 99, -1, -100]
    }
}
