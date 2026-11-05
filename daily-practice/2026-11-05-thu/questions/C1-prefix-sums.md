# C1. Prefix sums and hash maps (20 min)

File `c1/PrefixSums.java`. Test: `PrefixSumsTest`.

1. `subarraySumEqualsK(int[] a, long k)` - count contiguous subarrays with sum `k` (negatives allowed, sums exceed `int`). `[1,1,1], k=2 -> 2`; `[1,-1,0], k=0 -> 3`.
2. `longestBalancedBinary(int[] a)` - longest contiguous subarray of 0/1 with equal numbers of 0s and 1s. `[0,0,1,0,0,0,1,1] -> 6`.
3. `subarraysDivisibleByK(int[] a, int k)` - count subarrays with sum divisible by `k`. `[4,5,0,-2,-3,1], k=5 -> 7`. Mind negative remainders.

Constraints: `n <= 10^6`, values fit `int`, `1 <= k <= 2^20`. O(n) expected.
