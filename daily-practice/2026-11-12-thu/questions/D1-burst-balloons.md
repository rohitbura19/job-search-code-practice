# D1 - Burst Balloons (hard, 20 min) - `d1/BurstBalloons.java`

Balloons carry numbers `nums[0..n-1]`. Bursting balloon `i` earns `nums[i-1] * nums[i] * nums[i+1]` coins (a neighbour outside the array counts as 1); after the burst its neighbours become adjacent. Return the maximum coins for bursting them all.

`{3,1,5,8}` -> 167 (burst 1, 5, 3, 8). `{1,5}` -> 10. `{}` -> 0.
Constraints: `n <= 300`, `0 <= nums[i] <= 100`. Aim for O(n^3). The test compares with an exhaustive search on random arrays up to 7 elements.
