package arrays;

import java.util.Arrays;

// Plus One  [Easy | Carry]
// Digits of a number, most significant first. Return digits + 1.
//
// [1,2,3] -> [1,2,4]
// [9,9]   -> [1,0,0]

public class PlusOne {

    static int[] plusOne(int[] digits) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(plusOne(new int[]{1, 2, 3}))); // [1, 2, 4]
        System.out.println(Arrays.toString(plusOne(new int[]{9, 9})));    // [1, 0, 0]
    }
}
