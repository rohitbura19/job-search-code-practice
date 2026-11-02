# A2. Monotonic stack: temperatures, circular next greater, k-duplicate removal (10 min)

> Code: `src/main/java/practice/d20261102/a2/MonotonicStack.java`. Test: `MonotonicStackTest`. **10 min.**

1. `dailyTemperatures([73,74,75,71,69,72,76,73])` = `[1,1,4,2,1,1,0,0]`: days to wait for a strictly warmer day, 0 if never.
2. `nextGreaterCircular([1,2,3,4,3])` = `[2,3,4,-1,4]`: next strictly greater value, scanning to the right and wrapping around; -1 if none.
3. `removeDuplicates("deeedbbcccbdaa", 3)` = `"aa"`: repeatedly delete k equal adjacent characters. After a deletion the neighbours may merge into a new run (`abbba`, k=3 -> `aa`).

Constraints: n up to 10^6, O(n). Values may be negative or `Integer.MIN_VALUE`/`MAX_VALUE`; k >= 1.
