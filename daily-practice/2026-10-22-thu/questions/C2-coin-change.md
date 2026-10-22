> Code: `c2/CoinChange.java`. Test: `CoinChangeTest` (brute-force comparison). **25 min.**

# C2. Coin Change (fewest coins) and Coin Change II (number of combinations)

(a) `minCoins(coins, amount)`: fewest coins (unlimited supply) summing to `amount`; `0` for amount 0; `-1` when impossible.
`[1,2,5], 11 -> 3`; `[2], 3 -> -1`; `[1,3,4], 6 -> 2` (greedy gives 3).

(b) `countWays(coins, amount)`: number of different COMBINATIONS (order does not matter) that sum to `amount`; `1` for amount 0.
`[1,2,5], 5 -> 4`; `[2], 3 -> 0`.

Constraints: `0 <= amount <= 5*10^5`, `0 <= coins.length <= 12`, coins can repeat in the input and can be larger than `amount` (up to `Integer.MAX_VALUE`); the answer to (b) fits in a `long`.

Interviewer follow-up: why does greedy fail (counter-example) and for which coin systems does it work; why does the loop ORDER decide combinations vs permutations in (b); why is "infinity" `amount + 1` and not `Integer.MAX_VALUE`; reconstruct the coins used; space optimisation and its limit; each coin usable once (0/1 knapsack: which loop direction); the amount is 10^12 with 3 coins (think: periodicity, Frobenius number); relation to the subset-sum and knapsack family.
