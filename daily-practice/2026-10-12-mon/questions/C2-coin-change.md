> Code: `c2/CoinChange.java` (`coinChange`, plus the follow-ups `countWays` and `coinsUsed`). Test: `CoinChangeTest`.

### C2. Coin Change (1D DP)
`coins[]` and an `amount`. Return the fewest coins to make the amount, or -1. `[1,2,5], 11 -> 3`; `[2], 3 -> -1`.

Interviewer follow-up: why does greedy fail (`[1,3,4], 6`)? Count the number of ways instead (Coin Change II) — what changes in the loop order? Return the actual coins.
