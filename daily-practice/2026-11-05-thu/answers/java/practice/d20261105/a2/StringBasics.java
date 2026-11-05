package practice.d20261105.a2;

/**
 * strStr: try each start, compare; O(n*m) worst case, O(1) space (KMP gives O(n+m)).
 * reverseWords: scan from the end, copy each word; O(n).
 * zigzag: walk the string once, bouncing the row index between 0 and rows-1, append to per-row builders. O(n).
 */
public final class StringBasics {
    private StringBasics() {}

    public static int strStr(String hay, String needle) {
        int n = hay.length(), m = needle.length();
        for (int i = 0; i + m <= n; i++) {
            int j = 0;
            while (j < m && hay.charAt(i + j) == needle.charAt(j)) j++;
            if (j == m) return i;
        }
        return -1;
    }

    public static String reverseWords(String s) {
        StringBuilder sb = new StringBuilder();
        int i = s.length() - 1;
        while (i >= 0) {
            while (i >= 0 && s.charAt(i) == ' ') i--;
            if (i < 0) break;
            int end = i;
            while (i >= 0 && s.charAt(i) != ' ') i--;
            if (sb.length() > 0) sb.append(' ');
            sb.append(s, i + 1, end + 1);
        }
        return sb.toString();
    }

    public static String zigzag(String s, int rows) {
        if (rows == 1 || rows >= s.length()) return s;
        StringBuilder[] r = new StringBuilder[rows];
        for (int i = 0; i < rows; i++) r[i] = new StringBuilder();
        int row = 0, dir = 1;
        for (int i = 0; i < s.length(); i++) {
            r[row].append(s.charAt(i));
            if (row == 0) dir = 1;
            else if (row == rows - 1) dir = -1;
            row += dir;
        }
        StringBuilder out = new StringBuilder(s.length());
        for (StringBuilder b : r) out.append(b);
        return out.toString();
    }
}
