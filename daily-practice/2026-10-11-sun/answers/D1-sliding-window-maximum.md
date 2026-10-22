# Answers: D1-sliding-window-maximum

(Spoilers. Open only after your attempt.)

**Reference**
- Keep a deque of indices whose values are in decreasing order. Pop from the back while the new value is >= back value. Pop from the front if it is out of the window. Front is the max.
- Each index is pushed and popped once -> O(n) time, O(k) space.
```java
public int[] maxSlidingWindow(int[] nums, int k) {
    int n = nums.length; int[] res = new int[n - k + 1];
    Deque<Integer> dq = new ArrayDeque<>();
    for (int i = 0; i < n; i++) {
        while (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
        while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i]) dq.pollLast();
        dq.offerLast(i);
        if (i >= k - 1) res[i - k + 1] = nums[dq.peekFirst()];
    }
    return res;
}
```
