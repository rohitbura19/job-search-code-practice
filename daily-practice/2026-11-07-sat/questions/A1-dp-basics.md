# A1 - DP warm-ups (10 min) - `a1/DpBasics.java`

1. `long climbStairs(int n)` - number of distinct ways to climb n steps taking 1 or 2 at a time. `0 <= n <= 90`; `climbStairs(0) = 1`. Examples: 2 -> 2, 3 -> 3, 5 -> 8.
2. `int minCostClimbingStairs(int[] cost)` - paying `cost[i]` lets you leave step i and move 1 or 2 steps; you may start on step 0 or 1 and must reach index `cost.length`. `[10,15,20]` -> 15; `[1,100,1,1,1,100,1,1,100,1]` -> 6. Constraints: `2 <= n <= 5_000_000`, `0 <= cost[i] <= 999`. O(1) extra space.
3. `int tribonacci(int n)` - `T0=0, T1=1, T2=1, Tn = Tn-1 + Tn-2 + Tn-3`, `0 <= n <= 37` (fits in int). `4 -> 4`, `25 -> 1389537`.
