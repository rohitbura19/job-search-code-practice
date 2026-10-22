> Code: `d1/SlidingWindowMax.java`. Test: `SlidingWindowMaxTest`.

### D1. Sliding Window Maximum (Monotonic deque)
Array `nums`, window size `k`. Return the max of each window.
`[1,3,-1,-3,5,3,6,7], k=3 -> [3,3,5,5,6,7]`. Must beat O(n·k).

Interviewer follow-up: why does a heap give O(n log n) and why is the deque O(n)? What if `k` changes per query? Describe the same idea for a "rolling max latency over the last 60 s" metric.
