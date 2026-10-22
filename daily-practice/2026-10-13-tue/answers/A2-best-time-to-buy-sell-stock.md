# Answers: A2-best-time-to-buy-sell-stock

(Spoilers. Open only after your attempt.)

**Reference**
```java
public int maxProfit(int[] prices) {
    int minSoFar = Integer.MAX_VALUE, best = 0;
    for (int p : prices) {
        minSoFar = Math.min(minSoFar, p);      // cheapest buy so far
        best = Math.max(best, p - minSoFar);   // sell today
    }
    return best;
}
```
O(n) time, O(1) space, and that is exactly the streaming answer: two numbers. Unlimited transactions: sum of every positive day-to-day delta. At most k: DP `dp[t][day]` (or state machine hold/sold per transaction). Cooldown: three-state machine (hold, sold, rest). This is Kadane in disguise (max subarray of the delta array).
