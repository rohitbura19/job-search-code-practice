package practice.d20261110.c2;

import java.util.Arrays;

/**
 * maximalSquare: dp[j] = 1 + min(left, up, diag) when the cell is '1' (side of the square ending at that corner); keep one row plus the previous diagonal. O(rc), O(c).
 * maxProfitWithCooldown: three states per day - hold, sold (just sold today), rest (free, no stock). hold = max(hold, rest - p); sold = hold + p;
 * rest = max(rest, prevSold). Answer max(sold, rest). O(n), O(1).
 * numSquares: dp[i] = 1 + min over squares s <= i of dp[i - s]. O(n sqrt n), O(n).
 */
public class MixedDp {
    public static int maximalSquare(char[][] m) {
        if (m.length == 0 || m[0].length == 0) return 0;
        int cols = m[0].length, best = 0;
        int[] dp = new int[cols + 1];
        for (char[] row : m) {
            int diag = 0;
            for (int j = 1; j <= cols; j++) {
                int up = dp[j];
                dp[j] = row[j - 1] == '1' ? 1 + Math.min(diag, Math.min(up, dp[j - 1])) : 0;
                diag = up;
                best = Math.max(best, dp[j]);
            }
        }
        return best * best;
    }

    public static int maxProfitWithCooldown(int[] prices) {
        if (prices.length == 0) return 0;
        int hold = Integer.MIN_VALUE / 2, sold = 0, rest = 0;
        for (int p : prices) {
            int prevSold = sold;
            sold = hold + p;
            hold = Math.max(hold, rest - p);
            rest = Math.max(rest, prevSold);
        }
        return Math.max(sold, rest);
    }

    public static int numSquares(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i <= n; i++)
            for (int s = 1; s * s <= i; s++) dp[i] = Math.min(dp[i], dp[i - s * s] + 1);
        return dp[n];
    }
}
