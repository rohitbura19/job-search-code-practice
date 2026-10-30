# D1. Regular expression matching with '.' and '*' (20 min)

`isMatch(s, p)` in `d1/Regex.java`: does `p` match the WHOLE of `s`? `.` matches any one character, `*` means zero or more of the preceding element.
Examples: `("aa","a")` false, `("aa","a*")` true, `("ab",".*")` true, `("aab","c*a*b")` true, `("mississippi","mis*is*p*.")` false.

Constraints: |s|, |p| up to 2000; the pattern is valid (no leading `*`, no `**`). A plain recursion is exponential on `("a" x 30, "a*" x 15 + "b")`; the test checks that.
The test compares with `java.util.regex` on random inputs. State the recurrence for `dp[i][j]` first, then keep only one row.
