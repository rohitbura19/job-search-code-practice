package arrays;

// Missing Number  [Easy | XOR or sum]
// n distinct numbers from 0..n, one missing. O(n) time, O(1) space.
//
// [3,0,1]              -> 2
// [9,6,4,2,3,5,7,0,1]  -> 8

public class MissingNumber {

    static int missingNumber(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(missingNumber(new int[]{3, 0, 1}));                   // 2
        System.out.println(missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1})); // 8
    }
}
