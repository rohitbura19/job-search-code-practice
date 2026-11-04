# A2. Binary search basics (10 min)

> Code: `src/main/java/practice/d20261104/a2/BinarySearch.java`. Test: `BinarySearchTest`.

1. **searchInsert** sorted array, target. Return its index or the index where it would go (first index with `a[i] >= target`). Ex: `[1,3,5,6], 5 -> 2`; `2 -> 1`; `7 -> 4`; `[] , 3 -> 0`.
2. **firstLast** sorted with duplicates: `{first, last}` index of target or `{-1,-1}`. Ex: `[5,7,7,8,8,10], 8 -> {3,4}`. Includes `target == Integer.MAX_VALUE` - do not compute `target + 1`.
3. **findMin** sorted distinct values rotated at an unknown pivot (maybe not rotated). Minimum in O(log n). Ex: `[3,4,5,1,2] -> 1`, `[4,5,6,7,0,1,2] -> 0`, `[11,13,15,17] -> 11`.

Constraints: up to 5 * 10^6 elements; values anywhere in `int`.
