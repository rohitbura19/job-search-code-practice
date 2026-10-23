> Code: `src/main/java/practice/d20261023/c2/Increasing.java`. Test: `IncreasingTest`.

# C2. Longest Increasing Subsequence in O(n log n), then Russian Doll Envelopes (25 min)

(a) `lengthOfLIS(int[] nums)`: length of the longest STRICTLY increasing subsequence. `[10,9,2,5,3,7,101,18] -> 4`, `[7,7,7,7] -> 1`.

(b) `maxEnvelopes(int[][] envelopes)`: `{w,h}` fits in another only if both `w` and `h` are strictly smaller. Longest chain. `[[5,4],[6,4],[6,7],[2,3]] -> 3`. Do not reorder the caller's array.

Constraints: `n <= 200_000` for (a), `100_000` for (b).

Say out loud: what `tails[i]` means and why it stays sorted; lower bound vs upper bound (strict vs non-strict); why sorting equal widths by height DESCENDING makes (b) a plain LIS; how to also return the subsequence (parent pointers) and how to count the number of LIS (segment tree / BIT).
