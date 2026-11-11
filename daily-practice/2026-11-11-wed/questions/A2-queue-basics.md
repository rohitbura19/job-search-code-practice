# A2 - Queue and two-pointer basics (10 min) - `a2/QueueBasics.java`

1. `MovingAverage(size)` with `double next(int val)`: average of the last `size` values (fewer at the start). Values near `Integer.MAX_VALUE`: no overflow. O(1) per call.
2. `RecentCounter` with `int ping(int t)`: t strictly increases; return the number of pings in the closed window `[t-3000, t]`. Amortised O(1); 2,000,000 calls must be fast.
3. `backspaceCompare(String s, String t)`: `#` is a backspace (nothing to delete -> no effect). Equal after typing? `"ab#c"`, `"ad#c"` -> true; `"a#c"`, `"b"` -> false. O(1) extra space (scan from the back, no builder).
