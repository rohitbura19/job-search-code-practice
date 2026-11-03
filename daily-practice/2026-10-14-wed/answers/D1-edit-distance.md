# Answers: D1-edit-distance

(Spoilers. Open only after your attempt.)

**Reference**
- State: `dp[i][j]` = edit distance between the first `i` chars of `a` and the first `j` chars of `b`. Base: `dp[i][0] = i`, `dp[0][j] = j`. Transition: if the chars match, `dp[i-1][j-1]`; else `1 + min(replace dp[i-1][j-1], delete dp[i-1][j], insert dp[i][j-1])`.
```java
static int editDistance(String a, String b) {
    if (a.length() < b.length()) return editDistance(b, a);       // keep rows short: O(min) space
    int[] prev = new int[b.length() + 1], cur = new int[b.length() + 1];
    for (int j = 0; j <= b.length(); j++) prev[j] = j;
    for (int i = 1; i <= a.length(); i++) {
        cur[0] = i;
        for (int j = 1; j <= b.length(); j++) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) cur[j] = prev[j - 1];
            else cur[j] = 1 + Math.min(prev[j - 1], Math.min(prev[j], cur[j - 1]));
        }
        int[] t = prev; prev = cur; cur = t;
    }
    return prev[b.length()];
}
```
Verified: 3, 5, and `("", "abc") = 3`. Time O(n·m), space O(min(n, m)). To print operations you need the full table (or Hirschberg's divide and conquer to keep linear space); the traceback from `(n, m)` is: matching chars go diagonal, else follow whichever neighbour equals `dp - 1`. Verified trace for `horse -> ros`: `replace h->r @0, delete r @2, delete e @4`. Bounded distance k: only the diagonal band `|i - j| <= k` can matter, so O(n·k) time (Ukkonen's cutoff), which is how fast fuzzy matching is done. Product use: spell correction, fuzzy search suggestions, deduplicating device or site names. In practice you do not scan the whole dictionary: BK-tree, a trie with a DP row carried down each branch, or n-gram candidate generation first, then edit distance only to rank the candidates.
