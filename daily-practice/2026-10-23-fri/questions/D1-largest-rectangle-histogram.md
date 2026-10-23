> Code: `src/main/java/practice/d20261023/d1/Histogram.java`. Test: `HistogramTest`.

# D1. Largest Rectangle in a Histogram - monotonic stack (25 min)

`largestRectangle(int[] heights)`: bars of width 1; the area of the largest rectangle that fits under the bars.
`[2,1,5,6,2,3] -> 10` (the 5 and 6 bars), `[2,4] -> 4`, `[] -> 0`.

Constraints: `n <= 10^6`, `0 <= heights[i] <= 10^9` (area up to `10^15`: use `long`). O(n).

Say out loud: what the stack holds and why it is increasing; for each popped bar, where its rectangle starts and ends; why a sentinel of height 0 flushes the stack; how this solves "maximal rectangle of 1s in a binary matrix" (one histogram per row).
