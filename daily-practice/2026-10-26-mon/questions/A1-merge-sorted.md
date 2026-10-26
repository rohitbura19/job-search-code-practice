> Code: `src/main/java/practice/d20261026/a1/MergeSorted.java`. Test: `MergeSortedTest`. **15 min.**

# A1. Merge sorted array in place + sort colors (three-way partition)

**(a) `merge(int[] a, int m, int[] b, int n)`**: `a` has length `m + n`, its first `m` entries and all of `b` are sorted ascending. Merge `b` into `a` in place (no extra array).

```
a = [1,2,3,_,_,_] m=3   b = [2,5,6] n=3   ->  a = [1,2,2,3,5,6]
```

**(b) `sortColors(int[] nums)`**: `nums` holds only 0, 1 and 2. Sort it in place in ONE pass (no counting pass, no library sort). Then generalise: `threeWayPartition(a, pivot)` rearranges `a` into `[ < pivot | == pivot | > pivot ]` in one pass and returns `{lt, gt}` (the part boundaries).

```
[2,0,2,1,1,0] -> [0,0,1,1,2,2]        threeWayPartition([5,1,5,9,3], 5) -> e.g. [1,3,5,5,9] with {2, 4}
```

Constraints: `0 <= m, n <= 2*10^6`, values are any `int` (including `MIN_VALUE`/`MAX_VALUE`; do not subtract values). O(1) extra space.

Say out loud: why filling from the front fails and from the back works; the invariant of the three zones in (b), and why after swapping with the `>` side the index does not advance but after swapping with the `<` side it does; where this partition is used (quicksort with many duplicates).
