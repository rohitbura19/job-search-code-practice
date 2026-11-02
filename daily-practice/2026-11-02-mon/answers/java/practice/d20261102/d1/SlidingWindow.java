package practice.d20261102.d1;

/**
 * Monotonic deque of indices whose values are strictly decreasing from front to back: the front is the window maximum.
 * Drop the front when it leaves the window; drop back entries smaller than or equal to the new value (they can never be a maximum again).
 * Each index enters and leaves once: O(n) time, O(k) space (array-backed deque).
 */
public final class SlidingWindow {
    private SlidingWindow() {}

    public static int[] maxSlidingWindow(int[] a, int k) {
        int n = a.length;
        if (n == 0) return new int[0];
        int[] out = new int[n - k + 1];
        int[] dq = new int[n];
        int head = 0, tail = 0;
        for (int i = 0; i < n; i++) {
            if (head < tail && dq[head] <= i - k) head++;
            while (head < tail && a[dq[tail - 1]] <= a[i]) tail--;
            dq[tail++] = i;
            if (i >= k - 1) out[i - k + 1] = a[dq[head]];
        }
        return out;
    }
}
