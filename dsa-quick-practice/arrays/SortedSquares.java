package arrays;

import java.util.Arrays;

// Squares of a Sorted Array  [Easy | Two pointers from both ends]
// Sorted input (may have negatives). Return squares sorted. O(n).
//
// [-4,-1,0,3,10] -> [0,1,9,16,100]
// [-7,-3,2,3,11] -> [4,9,9,49,121]

public class SortedSquares {

    static int[] sortedSquares(int[] nums) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(sortedSquares(new int[]{-4, -1, 0, 3, 10}))); // [0, 1, 9, 16, 100]
        System.out.println(Arrays.toString(sortedSquares(new int[]{-7, -3, 2, 3, 11}))); // [4, 9, 9, 49, 121]
    }
}
