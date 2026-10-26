package dp;

// Longest Increasing Subsequence  [Medium | DP O(n^2), then tails + binary search O(n log n)]
// Length of the longest strictly increasing subsequence.
//
// [10,9,2,5,3,7,101,18] -> 4
// [7,7,7,7]             -> 1

public class LongestIncreasingSubsequence {

    static int lengthOfLIS(int[] nums) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18})); // 4
        System.out.println(lengthOfLIS(new int[]{7, 7, 7, 7}));                 // 1
    }
}
