# C2. DP over an amount: coin change (min), coin combinations, equal-sum partition (20 min)

> Code: `src/main/java/practice/d20261102/c2/DpBasics.java`. Test: `DpBasicsTest`. **20 min.**

1. `coinChangeMin(coins, amount)`: fewest coins, -1 if impossible. `{1,3,4}`, 6 -> 2 (greedy would give 3).
2. `coinWays(coins, amount)`: number of combinations (order ignored). `{1,2,5}`, 5 -> 4. UK coins `{1,2,5,10,20,50,100,200}`, 200 -> 73682.
3. `canPartition(nums)`: can the positive ints be split into two subsets of equal sum? `{1,5,11,5}` -> true.

Constraints: amount up to 5*10^5 for 1; n <= 200, values <= 100 for 3. Explain why swapping the two loops in 2 counts permutations, and why 3 iterates the sum downwards.
