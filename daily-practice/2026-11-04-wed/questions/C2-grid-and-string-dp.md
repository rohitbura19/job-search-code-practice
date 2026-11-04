# C2. Grid and string DP (20 min)

> Code: `src/main/java/practice/d20261104/c2/GridStringDp.java`. Test: `GridStringDpTest`.

1. **wordBreak** can `s` (up to 10^4 chars) be segmented into dictionary words (reusable, up to 1000 words of up to 20 chars)? `"leetcode",[leet,code] -> true`; `"catsandog",[cats,dog,sand,and,cat] -> false`. Adversarial case: `"aaaa...ab"` with dictionary `a, aa, ..., a^20` must not blow up.
2. **minPathSum** non-negative grid, move right/down, minimum path sum corner to corner, O(n) extra space. `[[1,3,1],[1,5,1],[4,2,1]] -> 7`.
3. **countPalindromicSubstrings** by position: `"abc" -> 3`, `"aaa" -> 6`. Up to 10^4 chars: O(n^2) is fine, O(n^3) is not.
