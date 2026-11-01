# A1. Happy number, summary ranges, isomorphic strings (10 min)

`a1/RangesAndHappy.java`

1. `isHappy(n)`, n >= 1: replace n by the sum of squares of its digits until it is 1 (happy) or loops. `19 -> true`, `2 -> false`. O(1) extra space.
2. `summaryRanges(int[] sorted unique)`: `[0,1,2,4,5,7] -> ["0->2","4->5","7"]`. Constraints: length 0..10^6, values may be `Integer.MIN_VALUE` / `Integer.MAX_VALUE` (the difference of neighbours overflows `int`).
3. `isIsomorphic(s, t)`: a bijection between characters. `egg/add -> true`, `badc/baba -> false`, `ab/aa -> false`.
