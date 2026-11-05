# A1. Array tricks (10 min)

File `a1/ArrayTricks.java`. Test: `ArrayTricksTest`.

1. `productExceptSelf(int[] a)` - `out[i]` is the product of every element except `a[i]`, no division, O(1) extra space besides the result. `int` arithmetic (wraps).
   `[1,2,3,4] -> [24,12,8,6]`; `[-1,1,0,-3,3] -> [0,0,9,0,0]`.
2. `rotate(int[] a, int k)` - rotate right by `k` in place, O(1) space. `[1..7], k=3 -> [5,6,7,1,2,3,4]`. `k` may exceed the length; empty array allowed.
3. `pivotIndex(int[] a)` - leftmost index whose left sum equals its right sum, else -1. `[1,7,3,6,5,6] -> 3`. Sums can exceed `int`.

Constraints: `0 <= n <= 2*10^6`, values fit `int`.
