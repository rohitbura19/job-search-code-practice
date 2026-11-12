# A2 - Matrix basics (10 min) - `a2/MatrixBasics.java`

1. `spiralOrder(int[][] m)`: clockwise from the top-left. `{{1,2,3},{4,5,6},{7,8,9}}` -> `[1,2,3,6,9,8,7,4,5]`. Handle 1 x n, n x 1, empty.
2. `rotate(int[][] m)`: n x n, 90 degrees clockwise, IN PLACE, O(1) extra. `{{1,2,3},{4,5,6},{7,8,9}}` -> `{{7,4,1},{8,5,2},{9,6,3}}`.
3. `isToeplitz(int[][] m)`: every top-left to bottom-right diagonal constant.

Constraints: up to 700 x 900; a naive loop that re-visits cells fails the timeout.
