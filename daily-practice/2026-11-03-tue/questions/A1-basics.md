# A1. Majority, Pascal row, brackets (10 min)

> Code: `src/main/java/practice/d20261103/a1/Basics.java`. Test: `BasicsTest`.

1. `majorityElement(int[] nums)`: the element that appears more than n/2 times (it exists). `[3,2,3]` -> 3; `[2,2,1,1,1,2,2]` -> 2. O(n) time, O(1) space.
2. `pascalRow(int r)`: row r (0-based) as `List<Integer>`; `r=3` -> `[1,3,3,1]`. 0 <= r <= 33. Use O(r) extra space (one list updated in place).
3. `isValid(String s)`: only `()[]{}`; true iff every bracket closes in the right order. `"([)]"` -> false, `""` -> true.

Constraints: n up to 2 million (1) and 1 million characters (3); no recursion.
