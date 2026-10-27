> Code: `src/main/java/practice/d20261027/c2/Permutations.java`. Test: `PermutationsTest`. **25 min.**

# C2. Permutations II and Next Permutation

1. `permuteUnique(nums)`: all distinct permutations of an array that may contain duplicates, in lexicographic order. `[1,1,2]` -> `[[1,1,2],[1,2,1],[2,1,1]]`. Empty input -> `[[]]`. `n <= 8`.
2. `nextPermutation(nums)`: rearrange in place into the next greater permutation; if none, into ascending order. `[1,2,3]` -> `[1,3,2]`; `[3,2,1]` -> `[1,2,3]`; `[1,1,5]` -> `[1,5,1]`. O(n) time, O(1) space, up to 10^6 elements.

Say out loud: the exact duplicate-skipping rule (`a[i]==a[i-1] && !used[i-1]`) and why it is correct; the 3 steps of next permutation (rightmost ascent, swap with the rightmost larger, reverse the suffix) and why the suffix was descending.
