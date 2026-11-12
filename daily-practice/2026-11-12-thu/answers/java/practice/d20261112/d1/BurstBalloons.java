package practice.d20261112.d1;

/**
 * Interval DP on the LAST balloon burst in (l, r): dp[l][r] = max over k of dp[l][k] + dp[k][r] + a[l]*a[k]*a[r],
 * where a is nums padded with 1 on both sides. Choosing the last (not first) balloon makes the two sides independent.
 * O(n^3) time, O(n^2) space.
 */
public class BurstBalloons {
    public static int maxCoins(int[] nums) {
        int n = nums.length;
        int[] a = new int[n + 2];
        a[0] = a[n + 1] = 1;
        System.arraycopy(nums, 0, a, 1, n);
        int[][] dp = new int[n + 2][n + 2];
        for (int len = 2; len <= n + 1; len++) {
            for (int l = 0; l + len <= n + 1; l++) {
                int r = l + len;
                int best = 0;
                for (int k = l + 1; k < r; k++) best = Math.max(best, dp[l][k] + dp[k][r] + a[l] * a[k] * a[r]);
                dp[l][r] = best;
            }
        }
        return dp[0][n + 1];
    }
}
