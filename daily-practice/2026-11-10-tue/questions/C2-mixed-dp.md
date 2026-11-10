# C2 - Grid and state DP (20 min) - `c2/MixedDp.java`

1. `maximalSquare(char[][] m)`: area of the largest all-'1' square (0 if none); empty matrix allowed. O(rc) time, O(c) space. `["10100","10111","11111","10010"]` -> 4.
2. `maxProfitWithCooldown(int[] prices)`: any number of transactions, one share at a time, after selling you cannot buy the next day. `[1,2,3,0,2]` -> 3 (buy 1, sell 2, cooldown, buy 0, sell 2). O(n) time, O(1) space.
3. `numSquares(int n)`: fewest perfect squares summing to n. `12` -> 3 (4+4+4); `13` -> 2. n up to 100,000.
