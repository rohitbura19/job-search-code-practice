# A1. Array basics (10 min) - `a1/ArrayBasics.java`

**1. Contains Nearby Duplicate.** `containsNearbyDuplicate(int[] nums, int k)`: true if there are `i != j` with `nums[i] == nums[j]` and `|i - j| <= k`.
Examples: `[1,2,3,1], k=3 -> true`; `[1,0,1,1], k=1 -> true`; `[1,2,3,1,2,3], k=2 -> false`. Constraints: n up to 2*10^6, `k` up to `Integer.MAX_VALUE`, values any int.

**2. Best Time to Buy and Sell Stock (one transaction).** `maxProfitOnce(int[] prices)` returns the max `prices[j] - prices[i]`, `i < j`, or 0. `[7,1,5,3,6,4] -> 5`, `[7,6,4,3,1] -> 0`. Prices are non-negative ints; the profit fits an int.

**3. Best Time to Buy and Sell Stock II (unlimited transactions, hold at most one share).** `maxProfitMany(int[] prices)` returns a `long`. `[7,1,5,3,6,4] -> 7`. The total can exceed `Integer.MAX_VALUE`.
Follow-up (say it): why does summing positive deltas equal the best set of transactions?
