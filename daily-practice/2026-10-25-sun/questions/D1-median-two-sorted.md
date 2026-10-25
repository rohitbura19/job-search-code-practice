> Code: `src/main/java/practice/d20261025/d1/Median.java`. Test: `MedianTest`.

# D1. Median of Two Sorted Arrays (25 min, hard but short)

`findMedian(int[] a, int[] b)`: both sorted ascending (either may be empty, not both: `IllegalArgumentException`). Return the median of the union in `O(log(min(n, m)))`. Even total -> the mean of the two middle values, computed without `int` overflow (`[MIN_VALUE, MAX_VALUE] -> -0.5`).

`[1,3] & [2] -> 2.0`; `[1,2] & [3,4] -> 2.5`.

Say out loud: the partition invariant (left part size, `maxLeft <= minRight` on both sides); why you binary-search the SHORTER array; how sentinels remove the edge cases; why merging with two pointers is O(n + m) and when that is fine.
