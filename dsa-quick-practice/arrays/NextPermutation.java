package arrays;

import java.util.Arrays;

// Next Permutation  [Medium | Find pivot, swap, reverse]
// Rearrange in place into the next greater permutation; if it is the last, wrap to ascending.
//
// [1,2,3] -> [1,3,2]
// [3,2,1] -> [1,2,3]
// [1,1,5] -> [1,5,1]

public class NextPermutation {

    static void nextPermutation(int[] nums) {
        // TODO
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3}, b = {3, 2, 1}, c = {1, 1, 5};
        nextPermutation(a);
        nextPermutation(b);
        nextPermutation(c);
        System.out.println(Arrays.toString(a)); // [1, 3, 2]
        System.out.println(Arrays.toString(b)); // [1, 2, 3]
        System.out.println(Arrays.toString(c)); // [1, 5, 1]
    }
}
