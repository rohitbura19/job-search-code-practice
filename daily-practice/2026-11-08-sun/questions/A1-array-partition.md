# A1 - In-place array partitioning (10 min) - `a1/ArrayPartition.java`

1. `sortColors(int[] a)`: values only 0, 1, 2. Sort in place, one pass, O(1) space. `[2,0,2,1,1,0]` -> `[0,0,1,1,2,2]`.
2. `moveZeroes(int[] a)`: move zeroes to the end keeping the order of the others; return the count of non-zero values. `[0,1,0,3,12]` -> `[1,3,12,0,0]`, returns 3.
3. `removeDuplicatesAtMostTwice(int[] a)`: sorted input, keep each value at most twice, in place; return the new length. `[1,1,1,2,2,3]` -> length 5, prefix `[1,1,2,2,3]`.

Constraints: 0 <= n <= 2*10^6 (time limit 2 s); values are any `int` for 2 and 3.
