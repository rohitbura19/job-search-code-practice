package arrays;

import java.util.Arrays;

// Move Zeroes  [Easy | Two pointers]
// Move all 0s to the end in place, keeping the order of the other values.
//
// [0,1,0,3,12] -> [1,3,12,0,0]

public class MoveZeroes {

    static void moveZeroes(int[] nums) {
        // TODO
    }

    public static void main(String[] args) {
        int[] a = {0, 1, 0, 3, 12};
        moveZeroes(a);
        System.out.println(Arrays.toString(a)); // [1, 3, 12, 0, 0]
    }
}
