# Q12. First Occurrence of 1 in a Sorted 0/1 Array

**Duration:** 15 minutes · **Language:** Java · **Source:** IDFC Technical Round 1, per LeetCode ("interviewer wanted a single-iteration solution")

## Problem Statement

You are given an array that contains only `0`s followed by `1`s, so it is sorted. Return the **index of the first `1`**, or `-1` if there are no 1s.

The solution must be a **single binary search loop** that runs in O(log n), even when the array is all 1s. The candidate in the LeetCode report found a 1 and then scanned left from it. That degrades to O(n) when the array is all 1s, and it cost them the round.

```java
int firstOne(int[] a)
```

## Examples

```
[0,0,0,1,1]  -> 3
[1,1,1,1]    -> 0
[0,0]        -> -1
[]           -> -1
[1]          -> 0
[0]          -> -1
[0,1]        -> 1
```

## Points to explain

- The lower-bound pattern: on `a[mid] == 1`, record `ans = mid` and set `hi = mid - 1`. On a 0, set `lo = mid + 1`.
- `mid = lo + (hi - lo) / 2` avoids overflow. Know why `(lo + hi) / 2` overflows for arrays of about 2^31.
- The loop invariant, and why the loop ends.

## Follow-ups

1. **Count the 1s:** `n - firstOne` (careful with `-1`).
2. **Unknown length or an infinite stream:** `interface Bits { int get(long i); }` returns `-1` past the end. Use **exponential search**: double `hi` until you find a 1 or the end, then binary search. O(log p), where p is the answer's position.
3. **Matrix:** each row is sorted 0s then 1s. Find the row with the most 1s in O(rows + cols) with a staircase walk.
4. Make it generic: `firstTrue(lo, hi, IntPredicate p)`. The same code solves "first bad version" and "minimum capacity to ship".
