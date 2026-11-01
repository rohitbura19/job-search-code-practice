package practice.d20261101.a2;

/**
 * isValidSudoku: one pass, a 9-bit mask per row / column / box; a set bit means the digit was seen. O(81), O(1).
 * plusOne: add from the right; stop at the first digit below 9; if all were 9 the result is 1 followed by zeros. O(n).
 * longestCommonPrefix: shrink the first string's prefix against every other string (or compare column by column). O(total chars).
 */
public final class GridAndDigits {
    private GridAndDigits() {}

    public static boolean isValidSudoku(char[][] b) {
        int[] rows = new int[9], cols = new int[9], boxes = new int[9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (b[r][c] == '.') continue;
                int bit = 1 << (b[r][c] - '1'), box = r / 3 * 3 + c / 3;
                if (((rows[r] | cols[c] | boxes[box]) & bit) != 0) return false;
                rows[r] |= bit; cols[c] |= bit; boxes[box] |= bit;
            }
        }
        return true;
    }

    public static int[] plusOne(int[] digits) {
        int[] d = digits.clone();
        for (int i = d.length - 1; i >= 0; i--) {
            if (d[i] < 9) { d[i]++; return d; }
            d[i] = 0;
        }
        int[] out = new int[d.length + 1];
        out[0] = 1;
        return out;
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        String p = strs[0];
        for (int i = 1; i < strs.length && !p.isEmpty(); i++) {
            int k = 0, m = Math.min(p.length(), strs[i].length());
            while (k < m && p.charAt(k) == strs[i].charAt(k)) k++;
            p = p.substring(0, k);
        }
        return p;
    }
}
