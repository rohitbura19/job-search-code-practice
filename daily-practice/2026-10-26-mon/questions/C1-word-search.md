> Code: `src/main/java/practice/d20261026/c1/WordSearch.java`. Test: `WordSearchTest`. **25 min.**

# C1. Word search (backtracking) + counting paths

**(a) `exist(board, word)`**: can `word` be spelled by a path of horizontally or vertically adjacent cells, using each cell at most once?

```
board = ABCE        "ABCCED" -> true
        SFCS        "SEE"    -> true
        ADEE        "ABCB"   -> false  (the same B cannot be used twice)
```

**(b) `countPaths(board, word)`**: the number of distinct paths (sequences of cells) that spell `word`. Same rules; a path and its reverse count separately. `board = [aa][aa]`, `word = "aaa"` -> 8.

Constraints: board up to `200 x 200`, word length `1..40`; for the stress case the word is longer than the longest possible path, or contains a letter the board does not have. The board must look unchanged after the call.

Say out loud: the worst-case bound (`R*C*3^L`) and why `3`, not `4`; marking visited in place vs a `boolean[][]` (and why restoring in a `finally` matters); two prunings (letter counts, searching the reversed word) and which input each helps.
