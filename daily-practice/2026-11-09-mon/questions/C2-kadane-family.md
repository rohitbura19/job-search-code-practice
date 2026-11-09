# C2 - Kadane family (20 min) - `c2/KadaneFamily.java`

1. `maxSubarraySum(a)`: best non-empty contiguous sum (long; all-negative arrays return the largest element).
2. `maxProductSubarray(a)`: best non-empty contiguous product; zeros and negatives. `[2,3,-2,4] -> 6`, `[-2,0,-1] -> 0`.
3. `maxCircularSubarraySum(a)`: the subarray may wrap around the end. `[5,-3,5] -> 10`, `[-3,-2,-3] -> -2`. Hint: wrap-around = total - (minimum subarray); the all-negative case needs care.

O(n) time, O(1) space; random tests compare with an O(n^2) brute force.
