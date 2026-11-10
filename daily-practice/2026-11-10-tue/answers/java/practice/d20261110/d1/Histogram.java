package practice.d20261110.d1;

/**
 * Monotonic stack of indices with strictly increasing heights. When bar i is lower than the top, the top bar's rectangle is finalised: its height is
 * that bar, its right edge is i (exclusive) and its left edge is the new stack top + 1. A sentinel height 0 at the end flushes the stack. Each index
 * is pushed and popped once: O(n) time, O(n) space (int array, no boxing). Equal heights may stay or pop; popping on <= keeps the stack strict and
 * the widest rectangle is still found by the last equal bar. maximalRectangle: heights[j] = consecutive 1s above and including this row, then run
 * the histogram per row.
 */
public class Histogram {
    public static long largestRectangle(int[] h) {
        int n = h.length;
        int[] stack = new int[n + 1];
        int top = 0;
        long best = 0;
        for (int i = 0; i <= n; i++) {
            int cur = i == n ? 0 : h[i];
            while (top > 0 && h[stack[top - 1]] >= cur) {
                int height = h[stack[--top]];
                int left = top == 0 ? 0 : stack[top - 1] + 1;
                best = Math.max(best, (long) height * (i - left));
            }
            stack[top++] = i;
        }
        return best;
    }

    public static int maximalRectangle(char[][] m) {
        if (m.length == 0 || m[0].length == 0) return 0;
        int[] heights = new int[m[0].length];
        long best = 0;
        for (char[] row : m) {
            for (int j = 0; j < heights.length; j++) heights[j] = row[j] == '1' ? heights[j] + 1 : 0;
            best = Math.max(best, largestRectangle(heights));
        }
        return (int) best;
    }
}
