# A1. Array tricks (10 min)

> Code: `src/main/java/practice/d20261104/a1/ArrayTricks.java`. Test: `ArrayTricksTest`.

1. **missingNumber** `int[] nums` holds `n` distinct numbers from `0..n`. Return the missing one in O(n) time and O(1) space. Ex: `[3,0,1] -> 2`, `[0,1] -> 2`, `[] -> 0`. Constraint: `n <= 10^6`.
2. **findDuplicates** each value is in `1..n` and appears once or twice. Return those that appear twice, ascending, using O(1) extra space besides the result, and leave the input unchanged. Ex: `[4,3,2,7,8,2,3,1] -> [2,3]`, `[1] -> []`.
3. **firstUniqChar** lowercase `a-z` string, up to 10^6 chars: index of the first character that occurs once, else `-1`. Ex: `"leetcode" -> 0`, `"loveleetcode" -> 2`, `"aabb" -> -1`.
