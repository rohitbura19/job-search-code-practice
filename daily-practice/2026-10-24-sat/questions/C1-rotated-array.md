> Code: `src/main/java/practice/d20261024/c1/Rotated.java`. Test: `RotatedTest`.

# C1. Search in a Rotated Sorted Array, then Find Minimum with duplicates - binary search on a half-sorted array (25 min)

(a) `search(nums, target)`: `nums` was sorted ascending with DISTINCT values, then rotated at an unknown pivot. Return the index or `-1`. `[4,5,6,7,0,1,2], 0 -> 4`; `[4,5,6,7,0,1,2], 3 -> -1`.
(b) `findMin(nums)`: the same rotation but values MAY repeat. `[2,2,2,0,1] -> 0`; `[3,3,1,3] -> 1`.

Constraints: `1 <= n <= 5*10^6`, (a) O(log n); (b) O(log n) on average, O(n) worst case.

Say out loud: how you decide which half is sorted and why one always is; why `nums[mid] == nums[hi]` in (b) forbids discarding half (`[3,1,3,3,3]` vs `[3,3,3,1,3]`) and why `hi--` is still safe; why (b) compares with `hi` and not with `lo`; `lo + (hi - lo) / 2` and the overflow it avoids.
