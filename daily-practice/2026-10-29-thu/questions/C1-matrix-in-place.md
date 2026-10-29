> Code: `src/main/java/practice/d20261029/c1/Matrices.java`. Test: `MatricesTest` (random checks, 3000 x 3000 matrix within 4 s). **25 min, plain editor.**

# C1. Matrix manipulation: spiral, rotate, set zeroes

1. `spiral(int[][] a)`: elements in clockwise spiral order for any m x n matrix (1 x n, m x 1 and empty included). `[[1,2,3],[4,5,6],[7,8,9]]` -> `[1,2,3,6,9,8,7,4,5]`.
2. `rotate(int[][] a)`: rotate an n x n matrix 90 degrees clockwise IN PLACE. `[[1,2],[3,4]]` -> `[[3,1],[4,2]]`.
3. `setZeroes(int[][] a)`: if a cell is 0, set its whole row and column to 0, in place, O(1) extra space (reuse the first row and column as markers).

Constraints: up to 3000 x 3000 values. Say out loud: why the spiral needs the two "still rows/columns left?" checks (draw a 1 x n and a 3 x 1 case), why transpose + reverse-rows equals a rotation (what is the coordinate mapping of each step), why the first row/column must be handled last in `setZeroes`, and what the O(m + n) boolean-array version costs you and when it is the better choice.
