# Answers: D1-trapping-rain-water

(Spoilers. Open only after your attempt.)

**Reference**
- Water above bar i = `min(maxLeft(i), maxRight(i)) - height[i]`. Brute force is O(n²). Prefix/suffix max arrays give O(n) time and O(n) space.
- Two pointers drop the arrays: the lower side is the limiting side, so process it.
```java
public int trap(int[] h) {
    int l = 0, r = h.length - 1, leftMax = 0, rightMax = 0, water = 0;
    while (l < r) {
        if (h[l] < h[r]) {                         // left is the limiting wall
            leftMax = Math.max(leftMax, h[l]);
            water += leftMax - h[l];
            l++;
        } else {
            rightMax = Math.max(rightMax, h[r]);
            water += rightMax - h[r];
            r--;
        }
    }
    return water;
}
```
O(n) time, O(1) space. Why correct: if `h[l] < h[r]`, then some wall on the right is at least `h[r]` > `h[l]`, so the true limit for `l` is `leftMax` and the right side cannot be smaller. Monotonic stack variant: keep decreasing indices; on a taller bar pop the bottom, add `(min(left, cur) - bottom) * width` layer by layer. 2D (Trapping Rain Water II): min-heap BFS from the border, always expand the lowest wall, O(RC log RC). Stream: the answer at any time needs the full history of the left maxima; keep a monotonic stack of (height, count) which is amortised O(1) per reading.
