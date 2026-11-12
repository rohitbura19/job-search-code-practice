package arrays;

// Max Consecutive Ones II  [Medium | Sliding window]
// Longest run of 1s if you may flip at most one 0. Single pass.
//
// [1,0,1,1,0]   -> 4
// [1,0,1,1,0,1] -> 4

public class MaxConsecutiveOnesII {

    static int findMaxConsecutiveOnes(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(findMaxConsecutiveOnes(new int[]{1, 0, 1, 1, 0}));    // 4
        System.out.println(findMaxConsecutiveOnes(new int[]{1, 0, 1, 1, 0, 1})); // 4
    }
}
