> Code: `d1/SlidingWindowMax.java`. Test: `SlidingWindowMaxTest` (brute force + a 10^6 array with window 5*10^5). **25 min.**

# D1. Sliding Window Maximum (monotonic deque)

Return the maximum of every window of `k` consecutive elements. `nums = [1,3,-1,-3,5,3,6,7], k = 3 -> [3,3,5,5,6,7]`; `[1], 1 -> [1]`.
Empty `nums` gives an empty array; `k < 1` or `k > n` for non-empty input throws `IllegalArgumentException`.

Constraints: `n <= 10^6`, any `int` values (including `MIN_VALUE`), duplicates, O(n) expected.

Interviewer follow-up: why store INDICES in the deque and not values; why pop the back on `<=` and not `<` (does it matter for correctness? for deque size?); amortised proof of O(n); other solutions (heap with lazy deletion O(n log n), sparse table, block prefix/suffix max with O(n) and no deque: describe it); sliding window MINIMUM and "longest subarray with max - min <= limit" (two deques); streaming input with unbounded `k`; where this appears (rate limiting by peak, moving-peak monitoring of sensor data).
