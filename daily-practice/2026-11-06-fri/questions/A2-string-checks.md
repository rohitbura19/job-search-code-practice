# A2 - String checks (easy, 10 min)
File: `a2/StringChecks.java`

1. **isSubsequence(s, t)** - `"abc"` in `"ahbgdc"` -> true; `"axc"` -> false. Empty `s` is always true.
2. **validPalindromeII(s)** - palindrome after deleting at most one char. `"abca"` -> true; `"abc"` -> false. Length up to 2*10^6, must be O(n).
3. **countAndSay(n)**, 1 <= n <= 30 - `1, 11, 21, 1211, 111221, 312211, ...` (read the previous term aloud as runs).
