> Code: `src/main/java/practice/d20261024/a1/Palindromes.java`. Test: `PalindromesTest`.

# A1. Valid Palindrome, then Valid Palindrome II (delete at most one) - two pointers from both ends (10 min each)

(a) `isPalindrome(String s)`: only letters and digits count, case is ignored. `"A man, a plan, a canal: Panama" -> true`, `"race a car" -> false`, `" " -> true`.

(b) `validPalindromeII(String s)`: true if deleting AT MOST one character makes `s` a palindrome. `"abca" -> true`, `"abc" -> false`, `"deeee" -> true`.

Constraints: `0 <= n <= 5*10^6`, O(n) time, O(1) extra space (no reversed copy, no recursion).

Say out loud: why trying both deletions at the FIRST mismatch is enough (no later mismatch can need a different deletion); why the work stays linear (the plain scan runs at most twice, once per branch); what changes for Unicode (surrogate pairs: compare code points, not chars).
