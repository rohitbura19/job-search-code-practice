package practice.d20261030.d1;

/**
 * dp[i][j] = s[0..i) matches p[0..j). If p[j-1] is '*': either zero copies (dp[i][j-2]) or one more copy of the element p[j-2]
 * (i > 0, p[j-2] matches s[i-1], and dp[i-1][j]). Otherwise dp[i-1][j-1] && p[j-1] matches s[i-1]. Only the previous row is needed.
 * O(|s| * |p|) time, O(|p|) space.
 */
public final class Regex {
    private Regex() {}

    public static boolean isMatch(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[] prev = new boolean[n + 1], cur = new boolean[n + 1];
        prev[0] = true;
        for (int j = 2; j <= n; j++) prev[j] = p.charAt(j - 1) == '*' && prev[j - 2];
        for (int i = 1; i <= m; i++) {
            cur[0] = false;
            for (int j = 1; j <= n; j++) {
                char pc = p.charAt(j - 1);
                if (pc == '*') {
                    char el = p.charAt(j - 2);
                    cur[j] = cur[j - 2] || ((el == '.' || el == s.charAt(i - 1)) && prev[j]);
                } else {
                    cur[j] = prev[j - 1] && (pc == '.' || pc == s.charAt(i - 1));
                }
            }
            boolean[] t = prev; prev = cur; cur = t;
        }
        return prev[n];
    }
}
