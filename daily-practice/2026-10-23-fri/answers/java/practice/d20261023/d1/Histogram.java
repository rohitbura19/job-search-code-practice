package practice.d20261023.d1;

/**
 * Monotonic stack of indices with non-decreasing heights. When a lower bar arrives, every taller bar on the stack ends:
 * its rectangle spans from the new stack top + 1 to i - 1 (the stack top is the nearest lower bar on the left, the arriving bar the
 * nearest lower-or-equal on the right). A sentinel height 0 at the end flushes the stack. Each index is pushed and popped once:
 * O(n) time, O(n) space (an int[] stack, no boxing).
 */
public final class Histogram {
    private Histogram() {}

    public static long largestRectangle(int[] h) {
        int n = h.length;
        int[] stack = new int[n + 1];
        int sp = 0;
        long best = 0;
        for (int i = 0; i <= n; i++) {
            int cur = i == n ? 0 : h[i];
            while (sp > 0 && h[stack[sp - 1]] >= cur) {
                long height = h[stack[--sp]];
                int left = sp == 0 ? -1 : stack[sp - 1];
                best = Math.max(best, height * (i - left - 1));
            }
            stack[sp++] = i;
        }
        return best;
    }
}
