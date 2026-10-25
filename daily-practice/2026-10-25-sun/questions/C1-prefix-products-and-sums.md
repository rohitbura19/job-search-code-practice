> Code: `src/main/java/practice/d20261025/c1/Prefix.java`. Test: `PrefixTest`.

# C1. Prefix tricks: Product of Array Except Self + Subarray Sum Equals K (25 min)

(a) `productExceptSelf(int[] nums)`: `out[i]` is the product of every other element. No division, O(n) time, O(1) extra space besides the output. `[1,2,3,4] -> [24,12,8,6]`; `[-1,1,0,-3,3] -> [0,0,9,0,0]`. Results fit in `int`.

(b) `subarraySum(int[] nums, long k)`: the number of contiguous non-empty subarrays whose sum equals `k`. Values may be negative (so a sliding window is wrong); sums can exceed the `int` range. `[1,1,1], k=2 -> 2`.

Say out loud: why division fails with zeros; the "left product, then right product" invariant; the identity `P[j] - P[i] = k`; why the map is seeded with `{0: 1}`; why the count itself could overflow `int` for 2*10^6 elements and what you would return then.
