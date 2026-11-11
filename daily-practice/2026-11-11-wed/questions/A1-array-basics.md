# A1 - Array basics (10 min) - `a1/ArrayBasics.java`

1. `thirdMax(int[] a)`: the third DISTINCT maximum; with fewer than 3 distinct values return the maximum. `[3,2,1]` -> 1, `[1,2]` -> 2, `[2,2,3,1]` -> 1. O(n), O(1) space.
2. `longestOnesWithOneFlip(int[] bits)`: longest run of 1s after flipping at most one 0. `[1,0,1,1,0]` -> 4. Single pass.
3. `canPlaceFlowers(int[] bed, int n)`: 0 empty, 1 planted, no two planted plots adjacent; can n MORE be planted? `[1,0,0,0,1]`, n=1 -> true; n=2 -> false. Do not modify the input.

Constraints: n up to 3,000,000; values of thirdMax are any int (`Integer.MIN_VALUE` is legal); n >= 0.
