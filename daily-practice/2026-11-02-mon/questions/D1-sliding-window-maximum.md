# D1. Sliding Window Maximum (hard, short, 20 min)

> Code: `src/main/java/practice/d20261102/d1/SlidingWindow.java`. Test: `SlidingWindowTest` (brute-force comparison, 10^6 elements within 2 s). **20 min.**

`maxSlidingWindow(a, k)`: for every window of k consecutive elements return its maximum. `[1,3,-1,-3,5,3,6,7]`, k=3 -> `[3,3,5,5,6,7]`.

Must be O(n): a heap gives O(n log n) with lazy deletion; a deque of indices gives O(n). Explain the deque invariant and why equal values are popped from the back. Extension on paper: minimum and maximum together (window range), and the same problem over a stream where you cannot index back.
