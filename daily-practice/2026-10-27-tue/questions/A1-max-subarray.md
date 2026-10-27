> Code: `src/main/java/practice/d20261027/a1/Subarrays.java`. Test: `SubarraysTest`. **15 min.**

# A1. Maximum subarray (sum and product)

1. `maxSubarray(int[] nums)`: the largest sum of a non-empty contiguous subarray. `[-2,1,-3,4,-1,2,1,-5,4]` -> `6` (`[4,-1,2,1]`). `[-3,-1,-2]` -> `-1`.
2. `maxProduct(int[] nums)`: the largest product of a non-empty contiguous subarray. `[2,3,-2,4]` -> `6`; `[-2,0,-1]` -> `0`; `[-2,3,-4]` -> `24`.

Constraints: `1 <= n <= 2*10^6`, the answer fits in an `int`. Target O(n) time, O(1) space.
Say out loud: why you may drop a negative running prefix for the sum, and why the product needs a running minimum too.
