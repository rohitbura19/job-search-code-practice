# C1. Sliding window (20 min)

> Code: `src/main/java/practice/d20261104/c1/SlidingWindow.java`. Test: `SlidingWindowTest`.

1. **longestUnique** longest substring without repeated characters (any `char`, length up to 10^6). `"abcabcbb" -> 3`, `"pwwkew" -> 3`, `"abba" -> 2`, `"" -> 0`.
2. **longestRepeatingReplacement** `'A'-'Z'` string, change at most `k` characters to any letter, longest run of one letter. `("ABAB",2) -> 4`, `("AABABBA",1) -> 4`.
3. **shortestSubarrayAtLeast** `(long target, int[] positive numbers)`: length of the shortest contiguous subarray with sum `>= target`, else `0`. `(7,[2,3,1,2,4,3]) -> 2`. Sums exceed `int`.

All three in O(n). Say the window invariant and when it is repaired.
