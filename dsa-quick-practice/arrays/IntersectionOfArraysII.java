package arrays;

import java.util.Arrays;

// Intersection of Two Arrays II  [Easy | Hashing or sort + two pointers]
// Return the common elements, each as many times as it appears in both, ascending.
//
// [1,2,2,1] & [2,2]       -> [2,2]
// [4,9,5] & [9,4,9,8,4]   -> [4,9]

public class IntersectionOfArraysII {

    static int[] intersect(int[] a, int[] b) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(intersect(new int[]{1, 2, 2, 1}, new int[]{2, 2})));       // [2, 2]
        System.out.println(Arrays.toString(intersect(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4}))); // [4, 9]
    }
}
