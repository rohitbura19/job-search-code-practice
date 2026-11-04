# D1. Longest Increasing Path in a Matrix (20 min, hard but short)

> Code: `src/main/java/practice/d20261104/d1/LongestIncreasingPath.java`. Test: `LongestIncreasingPathTest`.

Given an `m x n` integer matrix (`1 <= m, n <= 200`, any `int` values), return the number of cells on the longest strictly increasing path. From a cell you may move up, down, left or right (no diagonals, no wrapping).

Ex: `[[9,9,4],[6,6,8],[2,1,1]] -> 4` (1 -> 2 -> 6 -> 9); `[[3,4,5],[3,2,6],[2,2,1]] -> 4`; `[[7,7],[7,7]] -> 1`.

Trap: a 200 x 200 snake has a path of 40 000 cells. Plain recursion + memo is the textbook answer and it overflows the default Java stack; the test includes that case with a 3 s timeout. Target O(mn) time. Think: what kind of graph is this, and how do you process it without recursion?
