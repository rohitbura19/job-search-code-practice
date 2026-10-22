# Answers: C2-coin-change

(Spoilers. Open only after your attempt.)

**Reference**
```java
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);            // "infinity"
    dp[0] = 0;
    for (int a = 1; a <= amount; a++)
        for (int c : coins)
            if (c <= a) dp[a] = Math.min(dp[a], dp[a - c] + 1);
    return dp[amount] > amount ? -1 : dp[amount];
}
```
O(amount · coins) time, O(amount) space. Greedy gives 4+1+1 = 3 coins for 6 with `[1,3,4]`, optimum is 3+3 = 2. Number of ways: coins in the OUTER loop, amounts in the inner loop (`dp[a] += dp[a-c]`) so that combinations are not counted as permutations. Reconstruct: keep `choice[a]` = the coin used, walk back from `amount`.
