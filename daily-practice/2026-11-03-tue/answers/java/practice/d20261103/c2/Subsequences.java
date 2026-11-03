package practice.d20261103.c2;

import java.util.Arrays;

/**
 * LIS: tails[k] = smallest possible tail of an increasing subsequence of length k+1; tails stays sorted, so binary search the first
 *   tail >= x (strict increase) and overwrite it. O(n log n), O(n).
 * LCS: dp[i][j] = dp[i-1][j-1]+1 if a[i-1]==b[j-1] else max(dp[i-1][j], dp[i][j-1]); two rolling rows. O(nm) time, O(min) space.
 * Circular rob: house 0 and house n-1 are neighbours, so the best is the max of "skip the last" and "skip the first" linear problems.
 */
public final class Subsequences {
    private Subsequences() {}

    public static int lis(int[] nums) {
        int[] tails = new int[nums.length];
        int size = 0;
        for (int x : nums) {
            int lo = 0, hi = size;
            while (lo < hi) {
                int mid = (lo + hi) >>> 1;
                if (tails[mid] < x) lo = mid + 1; else hi = mid;
            }
            tails[lo] = x;
            if (lo == size) size++;
        }
        return size;
    }

    public static int lcs(String a, String b) {
        if (b.length() > a.length()) { String t = a; a = b; b = t; }
        int[] prev = new int[b.length() + 1], cur = new int[b.length() + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++)
                cur[j] = a.charAt(i - 1) == b.charAt(j - 1) ? prev[j - 1] + 1 : Math.max(prev[j], cur[j - 1]);
            int[] t = prev; prev = cur; cur = t;
            Arrays.fill(cur, 0);
        }
        return prev[b.length()];
    }

    public static int robCircular(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        return Math.max(rob(nums, 0, n - 2), rob(nums, 1, n - 1));
    }

    private static int rob(int[] a, int from, int to) {
        int take = 0, skip = 0;
        for (int i = from; i <= to; i++) {
            int nt = skip + a[i];
            skip = Math.max(skip, take);
            take = nt;
        }
        return Math.max(take, skip);
    }
}
