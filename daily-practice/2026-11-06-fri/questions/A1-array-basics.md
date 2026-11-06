# A1 - Array basics (easy, 10 min)
File: `a1/ArrayBasics.java`

1. **maxProfit(prices)** - one buy, one later sell. `[7,1,5,3,6,4]` -> 5; `[7,6,4,3,1]` -> 0. Empty/single -> 0. Values up to `Integer.MAX_VALUE` (difference fits an int because prices are >= 0).
2. **containsNearbyDuplicate(nums, k)** - equal values at distinct indices at most k apart. `[1,2,3,1], 3` -> true; `[1,2,3,1], 2` -> false. n up to 5*10^5.
3. **mergeSorted(a, m, b, n)** - `a` has room for m+n; merge `b` into `a` in place, O(1) extra space. `[1,3,5,0,0,0],3,[2,4,6],3` -> `[1,2,3,4,5,6]`.
