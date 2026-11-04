package practice.d20261104.c2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * wordBreak: dp[i] = prefix of length i is breakable; try only word lengths up to the longest dictionary word: O(n * L) lookups.
 * minPathSum: dp over a single row, dp[j] = grid[i][j] + min(dp[j] (from above), dp[j-1] (from left)): O(mn) time, O(n) space.
 * countPalindromicSubstrings: expand around the 2n-1 centres: O(n^2) time, O(1) space (Manacher would be O(n)).
 */
public final class GridStringDp {
    private GridStringDp() {}

    public static boolean wordBreak(String s, List<String> dict) {
        Set<String> words = new HashSet<>(dict);
        int maxLen = 0;
        for (String w : words) maxLen = Math.max(maxLen, w.length());
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); i++)
            for (int len = 1; len <= Math.min(i, maxLen) && !dp[i]; len++)
                if (dp[i - len] && words.contains(s.substring(i - len, i))) dp[i] = true;
        return dp[s.length()];
    }

    public static int minPathSum(int[][] grid) {
        int n = grid[0].length;
        int[] dp = new int[n];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) dp[j] = grid[0][0];
                else if (i == 0) dp[j] = dp[j - 1] + grid[i][j];
                else if (j == 0) dp[j] = dp[j] + grid[i][j];
                else dp[j] = Math.min(dp[j], dp[j - 1]) + grid[i][j];
            }
        return dp[n - 1];
    }

    public static int countPalindromicSubstrings(String s) {
        int count = 0;
        for (int c = 0; c < 2 * s.length() - 1; c++) {
            int l = c / 2, r = l + c % 2;
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { count++; l--; r++; }
        }
        return count;
    }
}
