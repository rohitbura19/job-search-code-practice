package practice.d20261107.c2;

/**
 * longestPalindrome: expand around each of 2n-1 centres, O(n^2) time, O(1) space (Manacher gets O(n)); keep a strictly longer
 * result only, so the leftmost wins. targetSum: with P = sum of "+" numbers and N the "-" ones, P - N = target and P + N = sum,
 * so P = (sum + target) / 2: count subsets with sum P (needs sum + target even and >= 0). 1-D 0/1 knapsack counting, iterate
 * the capacity downward. O(n * P) time, O(P) space.
 */
public final class StringDp {
    private StringDp() {}

    public static String longestPalindrome(String s) {
        int bestStart = 0, bestLen = 0;
        for (int c = 0; c < 2 * s.length() - 1; c++) {
            int l = c / 2, r = l + c % 2;
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) { l--; r++; }
            int len = r - l - 1;
            if (len > bestLen) { bestLen = len; bestStart = l + 1; }
        }
        return s.substring(bestStart, bestStart + bestLen);
    }

    public static long targetSum(int[] nums, int target) {
        long sum = 0;
        for (int x : nums) sum += x;
        long t = sum + target;
        if (t < 0 || (t & 1) == 1 || Math.abs((long) target) > sum) return 0;
        int p = (int) (t / 2);
        long[] dp = new long[p + 1];
        dp[0] = 1;
        for (int x : nums)
            for (int j = p; j >= x; j--) dp[j] += dp[j - x];
        return dp[p];
    }
}
