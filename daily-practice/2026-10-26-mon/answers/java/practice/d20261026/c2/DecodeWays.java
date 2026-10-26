package practice.d20261026.c2;

import java.util.ArrayList;
import java.util.List;

/**
 * numDecodings: dp[i] = ways to decode the suffix/prefix of length i. dp[i] = (s[i-1] != '0' ? dp[i-1] : 0)
 * + (s[i-2..i) in 10..26 ? dp[i-2] : 0). Two rolling variables -> O(n) time, O(1) space. Counts reach Fibonacci growth
 * (n ones = F(n+1)), so use long: length 90 still fits, length 93+ overflows (say so in the interview).
 * decodeAll: backtracking, one-digit letter first: A..I (one digit) sort before J..Z (two digits), so generation order IS
 * lexicographic order. Output size is the count, so O(count * n).
 */
public final class DecodeWays {
    private DecodeWays() {}

    public static long numDecodings(String s) {
        long prev2 = 1, prev1 = 1;                     // dp[0] = 1 (empty), dp[i-1]
        for (int i = 1; i <= s.length(); i++) {
            long cur = 0;
            if (s.charAt(i - 1) != '0') cur += prev1;
            if (i >= 2) {
                int two = (s.charAt(i - 2) - '0') * 10 + (s.charAt(i - 1) - '0');
                if (two >= 10 && two <= 26) cur += prev2;
            }
            prev2 = prev1;
            prev1 = cur;
        }
        return prev1;
    }

    public static List<String> decodeAll(String s) {
        List<String> out = new ArrayList<>();
        go(s, 0, new StringBuilder(), out);
        return out;
    }

    private static void go(String s, int i, StringBuilder sb, List<String> out) {
        if (i == s.length()) { out.add(sb.toString()); return; }
        if (s.charAt(i) == '0') return;
        sb.append((char) ('A' + s.charAt(i) - '1'));
        go(s, i + 1, sb, out);
        sb.setLength(sb.length() - 1);
        if (i + 1 < s.length()) {
            int two = (s.charAt(i) - '0') * 10 + (s.charAt(i + 1) - '0');
            if (two <= 26) {
                sb.append((char) ('A' + two - 1));
                go(s, i + 2, sb, out);
                sb.setLength(sb.length() - 1);
            }
        }
    }
}
