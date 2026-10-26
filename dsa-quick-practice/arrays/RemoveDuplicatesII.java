package arrays;

import java.util.Arrays;

// Remove Duplicates from Sorted Array II  [Medium | Two pointers]
// Sorted array: keep each value at most twice, in place. Return k (first k slots are the answer).
//
// [1,1,1,2,2,3]         -> 5, [1,1,2,2,3]
// [0,0,1,1,1,1,2,3,3]   -> 7

public class RemoveDuplicatesII {

    static int removeDuplicates(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        int[] a = {1, 1, 1, 2, 2, 3};
        int k = removeDuplicates(a);
        System.out.println(k + " " + Arrays.toString(Arrays.copyOf(a, k))); // 5 [1, 1, 2, 2, 3]
        System.out.println(removeDuplicates(new int[]{0, 0, 1, 1, 1, 1, 2, 3, 3})); // 7
    }
}
