# A2. Two pointers (10 min) - `a2/TwoPointers.java`

**1. Two Sum II.** `twoSumSorted(int[] sorted, long target)` on an ascending array returns the 1-based indices `{i, j}`, `i < j`, with `sorted[i-1] + sorted[j-1] == target`, else an empty array. O(1) extra space. `[2,7,11,15], 9 -> {1,2}`; `[3,3], 6 -> {1,2}`; one element -> empty. The sum of two ints can overflow an int.

**2. Container With Most Water.** `maxArea(int[] height)` returns the max over `i < j` of `min(h[i], h[j]) * (j - i)` as a `long`. `[1,8,6,2,5,4,8,3,7] -> 49`; fewer than 2 bars -> 0. Heights up to `Integer.MAX_VALUE`, n up to 3*10^6.
