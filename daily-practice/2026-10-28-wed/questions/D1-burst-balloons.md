> Code: `src/main/java/practice/d20261028/d1/BurstBalloons.java`. Test: `BurstBalloonsTest` (brute-force comparison for n <= 7, n = 300 within 2 s). **25 min.**

# D1. Burst Balloons (interval DP)

`nums` are balloon values. Bursting balloon `i` earns `nums[left] * nums[i] * nums[right]` where left/right are the nearest balloons still present (a missing one counts as `1`). Return the maximum coins for bursting everything.
`[3,1,5,8]` -> `167` (burst 1, 5, 3, 8: `3*1*5 + 3*5*8 + 1*3*8 + 1*8*1`). `[1,5]` -> `10`. `[]` -> `0`.

Constraints: `0 <= n <= 300`, `0 <= nums[i] <= 100`. Target O(n^3) time.
Say out loud: why you choose the LAST balloon to burst in an interval (the subproblems become independent), what `dp[l][r]` means (open interval, borders stay), and why choosing the FIRST burst gives dependent subproblems.
