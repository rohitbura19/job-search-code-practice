# C1 - Design small data structures (medium, 20 min)
Files: `c1/FenwickArray.java`, `c1/RandomizedSet.java`, `c1/TimeMap.java`

1. **FenwickArray** - `update(i, v)` sets a value, `sumRange(l, r)` inclusive; both O(log n); n up to 5*10^5; sums exceed int (use long). Example: `[1,3,5]` -> sum(0,2)=9; update(1,2) -> 8.
2. **RandomizedSet** - insert/remove/getRandom all O(1) average, uniform getRandom. The `Random` is injected (call `nextInt(size)` once). Hint: list + map value -> index, remove by swapping with the last element.
3. **TimeMap** - `set(key, value, ts)` with non-decreasing ts per key; `get(key, ts)` returns the value with the greatest ts' <= ts, or `""`. get must be O(log k).
