> Code: `src/main/java/practice/d20261022/a1/TwoPointers.java`. Test: `TwoPointersTest`.

# A1. Move Zeroes, then Remove Duplicates (keep at most two) - slow/fast pointers (10 min each)

(a) `moveZeroes(int[] nums)`: move every `0` to the end, keep the relative order of the others, in place.
`[0,1,0,3,12] -> [1,3,12,0,0]`, `[0] -> [0]`.

(b) `removeDuplicates(int[] nums)` on a SORTED array: keep each value at most twice, in place, return `k` (the first `k` slots are the answer).
`[1,1,1,2,2,3] -> k=5, [1,1,2,2,3]`; `[0,0,1,1,1,1,2,3,3] -> k=7`.

Constraints: `0 <= n <= 5*10^6`, values fit in `int`, O(1) extra space.

Say out loud: the loop invariant of the writer index; why (b) compares with `nums[w-2]` and not `nums[i-2]`; how you generalise to "at most K"; the minimum number of writes for (a) (swap vs overwrite and zero-fill).
