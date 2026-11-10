# D1 - Largest Rectangle in Histogram (20 min) - `d1/Histogram.java`

`largestRectangle(int[] heights)`: bars of width 1, heights 0..10^9, n up to 10^6. Return the area (long!) of the largest rectangle formed by consecutive bars. `[2,1,5,6,2,3]` -> 10; `[2,4]` -> 4; `[]` -> 0.
O(n) with a monotonic stack; an O(n^2) scan times out. State: what is on the stack, when is a bar finalised, what are its boundaries?

Bonus `maximalRectangle(char[][] m)`: largest rectangle of '1's in a binary matrix, reusing the histogram row by row. O(rows * cols).
