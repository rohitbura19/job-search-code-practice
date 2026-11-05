# D1. Median of Two Sorted Arrays (hard, 20 min)

File `d1/MedianOfTwoSortedArrays.java`. Test: `MedianOfTwoSortedArraysTest`.

`double findMedian(int[] a, int[] b)` - both sorted ascending, not both empty. Return the median of the union in O(log(min(m, n))) without merging.
`[1,3] + [2] -> 2.0`; `[1,2] + [3,4] -> 2.5`; `[Integer.MIN_VALUE] + [Integer.MAX_VALUE] -> -0.5`.

Constraints: `m, n <= 5*10^6`; values anywhere in `int` (no overflow when averaging). The test calls it 20 000 times on arrays of millions: a linear merge fails the timeout.
