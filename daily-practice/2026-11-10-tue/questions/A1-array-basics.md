# A1 - Array basics (10 min) - `a1/ArrayBasics.java`

1. `sortedSquares(int[] a)`: `a` sorted ascending (negatives allowed); return the squares sorted ascending in O(n). `[-4,-1,0,3,10]` -> `[0,1,9,16,100]`.
2. `intersect(int[] a, int[] b)`: multiset intersection, each value min(countA, countB) times, any order. `[1,2,2,1]`, `[2,2]` -> `[2,2]`. `[4,9,5]`, `[9,4,9,8,4]` -> `[4,9]`.
3. `rotateRight(int[] a, int k)`: in place, O(1) space; k may be 0, larger than n or negative (rotate left). `[1..7]`, k=3 -> `[5,6,7,1,2,3,4]`.

Constraints: n up to 2,000,000; |values| <= 46,339 for squares; any int for intersect/rotate (`k = Integer.MIN_VALUE` is legal).
