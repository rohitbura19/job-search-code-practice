> Code: `src/main/java/practice/d20261025/a1/Intersect.java`. Test: `IntersectTest`.

# A1. Intersection of Two Arrays II (15 min)

(a) `intersect(int[] a, int[] b)`: the multiset intersection, ascending; every value appears `min(countA, countB)` times. `[1,2,2,1] & [2,2] -> [2,2]`, `[4,9,5] & [9,4,9,8,4] -> [4,9]`.

(b) `intersectSorted(a, b)`: both inputs are sorted ascending; same result with O(1) extra space.

Constraints: `0 <= n, m <= 2*10^6`, values are any `int`, inputs must not be modified.

Say out loud: why counting the SHORTER array bounds the memory; what you do if one array does not fit in memory (stream the big one, count the small one; or external sort then merge); when sort + two pointers beats the hash map.
