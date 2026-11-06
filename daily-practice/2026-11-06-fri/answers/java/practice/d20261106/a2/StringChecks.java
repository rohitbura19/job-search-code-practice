package practice.d20261106.a2;

/**
 * isSubsequence: one pointer into s, advance on each match in t. O(|t|) / O(1).
 * validPalindromeII: two pointers; on the first mismatch try skipping the left or the right char and check the rest. O(n) / O(1).
 * countAndSay: build term by term, run-length encoding the previous term. O(total length) / O(length).
 */
public class StringChecks {
    public static boolean isSubsequence(String s, String t) {
        int i = 0;
        for (int j = 0; j < t.length() && i < s.length(); j++) if (s.charAt(i) == t.charAt(j)) i++;
        return i == s.length();
    }

    public static boolean validPalindromeII(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) return isPal(s, l + 1, r) || isPal(s, l, r - 1);
            l++;
            r--;
        }
        return true;
    }

    private static boolean isPal(String s, int l, int r) {
        while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false;
        return true;
    }

    public static String countAndSay(int n) {
        String cur = "1";
        for (int k = 2; k <= n; k++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cur.length(); ) {
                int j = i;
                while (j < cur.length() && cur.charAt(j) == cur.charAt(i)) j++;
                sb.append(j - i).append(cur.charAt(i));
                i = j;
            }
            cur = sb.toString();
        }
        return cur;
    }
}
