# C2. Subsequence DP: LIS, LCS, circular house robber (20 min)

> Code: `src/main/java/practice/d20261103/c2/Subsequences.java`. Test: `SubsequencesTest`.

1. `lis(int[])`: longest strictly increasing subsequence, O(n log n), n up to 200_000.
2. `lcs(String a, String b)`: longest common subsequence length, O(|a||b|) time, O(min) space, lengths up to 3000.
3. `robCircular(int[])`: houses in a circle, no two neighbours, non-negative values; max sum. `[2,3,2]` -> 3, `[1,2,3,1]` -> 4.

Write the recurrence for each first (one line). Then: how do you recover the LIS itself, not just its length?
