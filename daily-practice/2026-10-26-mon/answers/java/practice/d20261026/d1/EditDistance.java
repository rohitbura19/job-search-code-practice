package practice.d20261026.d1;

import java.util.Arrays;

/**
 * minDistance: dp[i][j] = distance between a[0..i) and b[0..j): equal chars -> dp[i-1][j-1]; else 1 + min(substitute dp[i-1][j-1],
 * delete dp[i-1][j], insert dp[i][j-1]). Only the previous row is needed: O(n*m) time, O(min(n, m)) space (swap so b is shorter).
 * distanceAtMost: a path from (0,0) to (n,m) with cost <= k never leaves the diagonal band |i - j| <= k, so compute only
 * 2k+1 cells per row and cap every value at k+1 ("too far"). O(n*k) time, O(m) space. Early out: |n - m| > k -> -1.
 */
public final class EditDistance {
    private EditDistance() {}

    public static int minDistance(String a, String b) {
        if (b.length() > a.length()) { String t = a; a = b; b = t; }      // b is the shorter one: row of length |b| + 1
        int m = b.length();
        int[] prev = new int[m + 1], cur = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            cur[0] = i;
            for (int j = 1; j <= m; j++) {
                cur[j] = a.charAt(i - 1) == b.charAt(j - 1) ? prev[j - 1] : 1 + Math.min(prev[j - 1], Math.min(prev[j], cur[j - 1]));
            }
            int[] t = prev; prev = cur; cur = t;
        }
        return prev[m];
    }

    public static int distanceAtMost(String a, String b, int k) {
        int n = a.length(), m = b.length();
        if (Math.abs(n - m) > k) return -1;
        final int inf = k + 1;
        int[] prev = new int[m + 1], cur = new int[m + 1];
        Arrays.fill(cur, inf);
        for (int j = 0; j <= m; j++) prev[j] = Math.min(j, inf);
        for (int i = 1; i <= n; i++) {
            int lo = Math.max(1, i - k), hi = Math.min(m, i + k);
            cur[0] = Math.min(i, inf);
            if (lo > 1) cur[lo - 1] = inf;                                   // the cell left of the band must read as "too far"
            for (int j = lo; j <= hi; j++) {
                int v = a.charAt(i - 1) == b.charAt(j - 1) ? prev[j - 1] : 1 + Math.min(prev[j - 1], Math.min(prev[j], cur[j - 1]));
                cur[j] = Math.min(v, inf);
            }
            int[] t = prev; prev = cur; cur = t;
        }
        return prev[m] <= k ? prev[m] : -1;
    }
}
