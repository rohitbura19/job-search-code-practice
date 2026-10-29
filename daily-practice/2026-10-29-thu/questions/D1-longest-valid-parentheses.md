> Code: `src/main/java/practice/d20261029/d1/ValidParens.java`. Test: `ValidParensTest` (brute force on random strings, 10^6 characters). **25 min.**

# D1. Longest valid parentheses

`longest(s)`: length of the longest balanced contiguous substring of `(` and `)`. `"(()"` -> 2, `")()())"` -> 4, `"()(()"` -> 2, `"(()())"` -> 6.
Follow-up `minAddToMakeValid(s)`: fewest insertions to make the whole string balanced: `"())"` -> 1, `"((("` -> 3.

Constraints: n up to 10^6 (no recursion). Known solutions: stack of indices (O(n) space), DP on the end index (`dp[i]`), and two counter passes (left to right, then right to left) in O(1) space. Write the stack version first (5 min), then replace it with the two-pass version.
Say out loud: why one left-to-right pass fails on `"(()"` and why the mirror pass fixes it; what `dp[i-1]` jumping over a matched block means in the DP form.
