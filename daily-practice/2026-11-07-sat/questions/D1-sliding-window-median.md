# D1 - Sliding Window Median (hard, 20 min) - `d1/SlidingWindowMedian.java`

`double[] medianSlidingWindow(int[] nums, int k)` returns the median of every window of size k (`1 <= k <= n <= 200,000`). Odd k: the middle value. Even k: the mean of the two middle values - values may be `Integer.MIN_VALUE`/`MAX_VALUE`, so do not add two ints. Result length `n - k + 1`.

Example: `[1,3,-1,-3,5,3,6,7], k=3` -> `[1,-1,-1,3,5,6]`. `[1,2,3,4], k=2` -> `[1.5,2.5,3.5]`.

Target O(n log k). Watch out: many duplicate values (remove exactly one element), and `Integer` comparison with `==`.
