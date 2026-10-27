> Code: `src/main/java/practice/d20261027/d1/CountSmaller.java`. Test: `CountSmallerTest`. **25 min.**

# D1. Count of smaller numbers after self (+ inversions)

1. `countSmaller(nums)`: `res[i]` = number of `j > i` with `nums[j] < nums[i]`. `[5,2,6,1]` -> `[2,1,1,0]`.
2. `inversions(nums)`: the number of pairs `i < j` with `nums[i] > nums[j]` as a `long`.

Constraints: `n <= 200000`, any `int` values (including `Integer.MIN_VALUE`). The O(n^2) loop times out. Target O(n log n): a Fenwick tree over compressed ranks, or merge sort that counts while merging.
Say out loud: why compress values (huge range, negatives), why scan right to left, and why equal values are not counted.
