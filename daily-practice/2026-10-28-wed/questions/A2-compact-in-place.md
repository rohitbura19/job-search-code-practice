> Code: `src/main/java/practice/d20261028/a2/Compact.java`. Test: `CompactTest`. **15 min.**

# A2. In-place compaction with a read and a write pointer

1. `moveZeroes`: `[0,1,0,3,12]` -> `[1,3,12,0,0]`, stable, in place.
2. `removeElement(a, val)`: `[0,1,2,2,3,0,4,2]`, `val=2` -> `k=5`, first five slots `[0,1,3,0,4]`.
3. `removeDuplicatesAtMostTwice` (sorted array): `[1,1,1,2,2,3]` -> `k=5`, `[1,1,2,2,3]`.

Constraints: `0 <= n <= 5*10^6`, one pass, O(1) extra space.
Say out loud: the invariant of the write pointer ("a[0..w) is the answer for the prefix read so far") and why comparing with `a[w-2]`, not `a[r-2]`, is the right test in item 3. How would you generalise to "at most k times"?
