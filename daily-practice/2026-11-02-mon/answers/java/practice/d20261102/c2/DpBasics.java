package practice.d20261102.c2;

import java.util.Arrays;

/**
 * 1-D DP over the amount/sum, O(amount * coins) and O(sum * n), O(amount) space.
 * coinChangeMin: dp[a] = 1 + min dp[a - c]. coinWays: loop coins OUTSIDE and amounts inside so each combination is
 * counted once (swapping the loops counts ordered sequences). canPartition: 0/1 knapsack, iterate sums downwards
 * so each number is used at most once.
 */
public final class DpBasics {
    private DpBasics() {}

    public static int coinChangeMin(int[] coins, int amount) {
        int inf = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, inf);
        dp[0] = 0;
        for (int a = 1; a <= amount; a++)
            for (int c : coins) if (c <= a && dp[a - c] + 1 < dp[a]) dp[a] = dp[a - c] + 1;
        return dp[amount] >= inf ? -1 : dp[amount];
    }

    public static long coinWays(int[] coins, int amount) {
        long[] dp = new long[amount + 1];
        dp[0] = 1;
        for (int c : coins) for (int a = c; a <= amount; a++) dp[a] += dp[a - c];
        return dp[amount];
    }

    public static boolean canPartition(int[] nums) {
        int sum = 0;
        for (int v : nums) sum += v;
        if ((sum & 1) == 1) return false;
        int half = sum / 2;
        boolean[] can = new boolean[half + 1];
        can[0] = true;
        for (int v : nums) for (int s = half; s >= v; s--) can[s] |= can[s - v];
        return can[half];
    }
}
