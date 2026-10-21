> Code: `a2/PascalTriangle.java`. Test: `PascalTriangleTest`.

### A2. Pascal's Triangle (row by row, then one row in O(k) space)
(a) Return the first `numRows` rows of Pascal's triangle. `5 -> [[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]`. (b) Return only row `k` (0-indexed) using O(k) extra space. `4 -> [1,4,6,4,1]`, `0 -> [1]`.

Interviewer follow-up: why must the in-place update for (b) run right to left? Give the closed form for one cell and a way to generate a row in O(k) time with a single loop (`C(k, c+1) = C(k, c) * (k - c) / (c + 1)`: why is the division exact?). For which `k` does the middle value stop fitting in an `int`, and in a `long`? Where does this show up (binomial coefficients, counting grid paths, expansion of `(a + b)^k`)? Compute `C(n, k) mod p` for large `n`.
