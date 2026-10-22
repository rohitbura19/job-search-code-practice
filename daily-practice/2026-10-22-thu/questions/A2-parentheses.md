> Code: `a2/Parentheses.java`. Test: `ParenthesesTest`.

# A2. Valid Parentheses, then Minimum Insertions to Make Valid (stack vs counter) (10 min each)

(a) `isValid(String s)`: `s` contains only `()[]{}`. Valid when every bracket is closed by the same kind in the right order.
`"()[]{}" -> true`, `"([)]" -> false`, `"" -> true`, `"(" -> false`.

(b) `minAddToMakeValid(String s)`: `s` contains only `(` and `)`. Minimum number of characters to INSERT to make it valid.
`"())" -> 1`, `")(" -> 2`, `"(((" -> 3`.

Constraints: `0 <= n <= 2*10^6` (the deeply nested test would overflow a recursive solution).

Say out loud: why (b) needs only a counter; why a stack of EXPECTED closers is simpler than a stack of openers; the odd-length shortcut; how you would return the repaired string; the longest valid substring variant (stack of indices or two scans).
