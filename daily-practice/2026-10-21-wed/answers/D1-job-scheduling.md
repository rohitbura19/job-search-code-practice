# Answers: D1-job-scheduling

(Spoilers. Open only after your attempt.)

**Reference**
```java
static long maxProfit(int[] start, int[] end, int[] profit) {
    int n = start.length; Integer[] ord = new Integer[n]; for (int i = 0; i < n; i++) ord[i] = i;
    Arrays.sort(ord, Comparator.comparingInt(i -> end[i]));              // by END time
    int[] ends = new int[n]; for (int i = 0; i < n; i++) ends[i] = end[ord[i]];
    long[] dp = new long[n + 1];                                         // dp[i] = best profit using only the first i jobs (in end order)
    for (int i = 1; i <= n; i++) {
        int job = ord[i - 1];
        int p = upperBound(ends, i - 1, start[job]);                     // how many of the first i-1 jobs end at or before this start
        dp[i] = Math.max(dp[i - 1], dp[p] + profit[job]);                // skip this job, or take it on top of the best compatible prefix
    }
    return dp[n];
}
static int upperBound(int[] a, int hi, int key) {                        // first index in [0, hi) with a[idx] > key (or hi)
    int lo = 0; while (lo < hi) { int m = (lo + hi) >>> 1; if (a[m] <= key) lo = m + 1; else hi = m; } return lo;
}
```
Verified: `120`, `150`, `6` for the three examples; `7` for one job, `9` for two touching jobs (`[1,2]`,`[2,3]` paying 4 and 5), `0` for no jobs, and `20` for `[1,3]` and `[3,5]` paying 10 each (touching intervals are compatible, which is why the search uses `<=`); 5,000 random cases (0 to 8 jobs, starts 0 to 11, lengths 1 to 6) matched a 2^n subset brute force; 50,000 random jobs with times up to 10^9 ran in about 60 ms. O(n log n) time (the sort and n binary searches), O(n) space. Idea: process jobs in order of end time; `dp[i]` is the best profit among the first `i` jobs. For job `i` there are two choices: skip it (`dp[i-1]`), or take it, in which case all earlier chosen jobs must END at or before its start; because the jobs are sorted by end time, those compatible jobs are exactly a PREFIX, whose length `p` the binary search finds, so the value is `dp[p] + profit`. The greedy fails because profit matters: with jobs `[0,10] pays 100` and `[0,2] pays 1`, `[3,5] pays 1`, earliest-end picks the small ones and gets 2. Use `upperBound` with `<=`: a lower bound (`<`) would treat a job that ends exactly at this start as incompatible and lose touching intervals. The profit sum can exceed `int` in general (50,000 jobs times 10^4 is only 5·10^8 here, but do not rely on that): use `long`. Times of 10^9 are no problem because only the order matters (no array indexed by time). To return the jobs, store the decision per `i` (`take[i] = dp[p] + profit > dp[i-1]`) and walk back from `n`, jumping to `p` when the job was taken. `m` machines: that becomes a min-cost flow or a greedy with a heap only for the unweighted case; say that it is harder. Versus merge-intervals (early sheets): that one merges overlapping intervals greedily to a smaller set; this one chooses a maximum-weight non-overlapping SUBSET, which needs DP. Same family: Non-overlapping Intervals (unweighted, greedy by end), Maximum Earnings From Taxi, Meeting Rooms II (a heap), and the "choose or skip with a jump to the next compatible index" shape of House Robber. Common mistakes: sorting by start, using the wrong binary-search bound, computing `p` over the whole array instead of the first `i - 1` jobs, and an `int` profit.
