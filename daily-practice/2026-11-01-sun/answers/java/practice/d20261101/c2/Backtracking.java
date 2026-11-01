package practice.d20261101.c2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * subsetsWithDup: sort, then at each depth skip a value equal to the previous one at the SAME depth (i > start && a[i] == a[i-1]).
 *   O(n * 2^n) time, O(n) recursion.
 * generateParenthesis: add '(' while open < n, ')' while close < open; every prefix stays valid, so no dead ends. O(Catalan(n) * n).
 * totalNQueens: place row by row; bitmasks for columns and the two diagonals give O(1) conflict checks. Roughly O(n!) pruned.
 */
public final class Backtracking {
    private Backtracking() {}

    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        int[] a = nums.clone();
        Arrays.sort(a);
        List<List<Integer>> out = new ArrayList<>();
        subsets(a, 0, new ArrayList<>(), out);
        return out;
    }

    private static void subsets(int[] a, int start, List<Integer> cur, List<List<Integer>> out) {
        out.add(new ArrayList<>(cur));
        for (int i = start; i < a.length; i++) {
            if (i > start && a[i] == a[i - 1]) continue;
            cur.add(a[i]);
            subsets(a, i + 1, cur, out);
            cur.remove(cur.size() - 1);
        }
    }

    public static List<String> generateParenthesis(int n) {
        List<String> out = new ArrayList<>();
        gen(new char[2 * n], 0, 0, 0, n, out);
        return out;
    }

    private static void gen(char[] buf, int pos, int open, int close, int n, List<String> out) {
        if (pos == buf.length) { out.add(new String(buf)); return; }
        if (open < n) { buf[pos] = '('; gen(buf, pos + 1, open + 1, close, n, out); }
        if (close < open) { buf[pos] = ')'; gen(buf, pos + 1, open, close + 1, n, out); }
    }

    public static int totalNQueens(int n) {
        return queens(n, 0, 0, 0, 0);
    }

    private static int queens(int n, int row, int cols, int d1, int d2) {
        if (row == n) return 1;
        int count = 0;
        int free = ~(cols | d1 | d2) & ((1 << n) - 1);
        while (free != 0) {
            int bit = free & -free;
            free -= bit;
            count += queens(n, row + 1, cols | bit, (d1 | bit) << 1, (d2 | bit) >> 1);
        }
        return count;
    }
}
