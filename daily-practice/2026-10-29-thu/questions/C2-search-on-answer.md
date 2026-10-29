> Code: `src/main/java/practice/d20261029/c2/SearchOnAnswer.java`. Test: `SearchOnAnswerTest` (brute-force and DP comparison, overflow, 500,000 elements). **25 min.**

# C2. Binary search on the answer

1. `minEatingSpeed(int[] piles, int h)` (Koko): minimum integer speed so all piles are eaten in `h` hours, one pile at a time, leftovers of an hour are wasted. `[3,6,7,11], h=8` -> 4; `[30,11,23,4,20], h=5` -> 30, `h=6` -> 23.
2. `splitArray(int[] nums, int k)`: split into `k` non-empty contiguous parts minimising the largest part sum. `[7,2,5,10,8], k=2` -> 18.

Constraints: `piles.length <= h`, piles up to 10^9 (hour total needs `long`); `nums` non-negative, sum up to ~2 * 10^9 (the sum does NOT fit in an int, though the answer does for the test sizes: use `long` for the search bounds).
Targets: O(n log range). Say out loud: the monotone predicate in each problem, how you choose `lo` and `hi`, why `mid = lo + (hi - lo) / 2`, and why the loop `while (lo < hi)` with `hi = mid` terminates. The DP alternative is O(k n^2): when would you still use it?
