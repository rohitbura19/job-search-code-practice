> Code: `src/main/java/practice/d20261024/d1/MinWindow.java`. Test: `MinWindowTest`.

# D1. Minimum Window Substring - sliding window with a deficit counter (25 min)

`minWindow(s, t)`: the shortest substring of `s` that contains every character of `t` WITH multiplicity (`t = "AABC"` needs two `A`). Leftmost if several tie; `""` if there is none (or `t` is empty). ASCII only.
`"ADOBECODEBANC", "ABC" -> "BANC"`; `"a", "aa" -> ""`.

Constraints: `|s|` up to `5*10^6`, O(|s| + |t|) time, O(1) space for the table.

Say out loud: the invariant ("the window `[l, r]` has `missing == 0` iff it covers `t`") and why `l` and `r` only move forward (each is O(n) in total); what the sign of `need[c]` means for a character that is NOT in `t`; why `missing` counts characters with multiplicity and not distinct letters; how it changes for Unicode (a `HashMap<Integer,Integer>` of code points) and for "all anagram positions" (fixed window).
