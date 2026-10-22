package practice.d20261022.d1;

/**
 * Monotonic deque of INDICES whose values decrease from front to back: the front is always the window maximum.
 * For each i: pop the back while nums[back] <= nums[i] (they can never be a maximum again), push i,
 * pop the front when it left the window (front <= i - k), record nums[front] once i >= k - 1.
 * Each index enters and leaves once: O(n) time, O(k) space. Brute force is O(n * k).
 * The deque is an int array used as a ring-free stack/queue (head and tail only move forward): no boxing.
 */
public final class SlidingWindowMax {
    private SlidingWindowMax() {}

    public static int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return new int[0];
        if (k < 1 || k > n) throw new IllegalArgumentException("k=" + k);
        int[] dq = new int[n];
        int head = 0, tail = 0;
        int[] out = new int[n - k + 1];
        for (int i = 0; i < n; i++) {
            while (tail > head && nums[dq[tail - 1]] <= nums[i]) tail--;
            dq[tail++] = i;
            if (dq[head] <= i - k) head++;
            if (i >= k - 1) out[i - k + 1] = nums[dq[head]];
        }
        return out;
    }
}
