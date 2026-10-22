package practice.d20261022.c2;

import java.util.Arrays;

/**
 * Both are unbounded knapsack over amounts: O(amount * coins) time, O(amount) space.
 * minCoins: dp[a] = 1 + min(dp[a - c]); "infinity" is amount + 1 (not Integer.MAX_VALUE, which would overflow on +1).
 * countWays: the COIN loop is OUTER, the amount loop inner, so each combination is counted once in a fixed coin order.
 * Swapping the loops counts permutations (1+2 and 2+1 as different), which is a different question.
 */
public final class CoinChange {
    private CoinChange() {}

    public static int minCoins(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int a = 1; a <= amount; a++) {
            for (int c : coins) {
                if (c <= a && dp[a - c] + 1 < dp[a]) dp[a] = dp[a - c] + 1;
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    public static long countWays(int[] coins, int amount) {
        long[] ways = new long[amount + 1];
        ways[0] = 1;
        for (int c : coins) {
            for (int a = c; a <= amount; a++) ways[a] += ways[a - c];
        }
        return ways[amount];
    }
}
