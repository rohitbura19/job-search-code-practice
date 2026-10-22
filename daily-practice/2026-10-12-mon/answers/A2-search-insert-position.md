# Answers: A2-search-insert-position

(Spoilers. Open only after your attempt.)

**Reference**
```java
public int searchInsert(int[] a, int target) {
    int lo = 0, hi = a.length;              // hi is exclusive
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;       // avoids int overflow
        if (a[mid] < target) lo = mid + 1;
        else hi = mid;                      // a[mid] >= target
    }
    return lo;                              // first index with a[i] >= target
}
```
O(log n). Same lower-bound template handles duplicates (first occurrence). For "answer" problems, the predicate is monotonic (feasible(capacity)), and you search the smallest capacity that is feasible.

Extra (verified by `SearchInsertTest`): `firstOccurrence` = `i = searchInsert(a, target)`, then `i < n && a[i] == target ? i : -1`. `shipWithinDays`: `lo = max(weights)`, `hi = sum(weights)` as `long`; `feasible(cap)` counts days greedily (start a new day when `load + w > cap`); `if (feasible(mid)) hi = mid; else lo = mid + 1`.
