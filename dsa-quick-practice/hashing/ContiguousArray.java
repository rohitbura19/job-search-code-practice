package hashing;

// Contiguous Array  [Medium | Prefix sum (0 -> -1) + first index map]
// Longest subarray of a 0/1 array with equal 0s and 1s.
//
// [0,1]     -> 2
// [0,1,0]   -> 2
// [0,0,1,0,0,0,1,1] -> 6

public class ContiguousArray {

    static int findMaxLength(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(findMaxLength(new int[]{0, 1}));                   // 2
        System.out.println(findMaxLength(new int[]{0, 1, 0}));                // 2
        System.out.println(findMaxLength(new int[]{0, 0, 1, 0, 0, 0, 1, 1})); // 6
    }
}
