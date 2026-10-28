package practice.d20261028.d1;

/**
 * Interval DP on the LAST balloon to burst. dp[l][r] = best for the open interval (l, r) whose borders l and r stay until the end.
 * If m is the last one burst inside (l, r), the neighbours at that time are exactly l and r, and the two sides are independent:
 * dp[l][r] = max over m of dp[l][m] + v[l]*v[m]*v[r] + dp[m][r]. Thinking of the FIRST burst instead makes sides dependent.
 * O(n^3) time, O(n^2) space. 300^3 / 6 ~ 4.5M steps.
 */
public final class BurstBalloons {
    private BurstBalloons() {}

    public static int maxCoins(int[] nums) {
        int n = nums.length;
        int[] v = new int[n + 2];
        v[0] = v[n + 1] = 1;
        System.arraycopy(nums, 0, v, 1, n);
        int[][] dp = new int[n + 2][n + 2];
        for (int len = 2; len <= n + 1; len++) {
            for (int l = 0; l + len <= n + 1; l++) {
                int r = l + len, best = 0;
                for (int m = l + 1; m < r; m++) best = Math.max(best, dp[l][m] + v[l] * v[m] * v[r] + dp[m][r]);
                dp[l][r] = best;
            }
        }
        return dp[0][n + 1];
    }
}
