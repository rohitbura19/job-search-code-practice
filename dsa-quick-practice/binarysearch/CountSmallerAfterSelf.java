package binarysearch;

import java.util.Arrays;

// Count of Smaller Numbers After Self  [Hard | Merge sort on indices, or Fenwick tree]
// result[i] = number of j > i with nums[j] < nums[i]. O(n log n).
//
// [5,2,6,1] -> [2,1,1,0]
// [-1,-1]   -> [0,0]

public class CountSmallerAfterSelf {

    static int[] countSmaller(int[] nums) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(countSmaller(new int[]{5, 2, 6, 1}))); // [2, 1, 1, 0]
        System.out.println(Arrays.toString(countSmaller(new int[]{-1, -1})));     // [0, 0]
    }
}
