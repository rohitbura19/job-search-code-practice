# C2 - Intervals (20 min) - `c2/Intervals.java`

1. `merge(int[][])`: merge overlapping closed intervals (`[1,4],[4,5]` -> `[1,5]`); do not mutate the input.
2. `insert(int[][] sorted, int[] ni)`: sorted non-overlapping input, O(n), no re-sort.
3. `minMeetingRooms(int[][])`: half-open `[start,end)`; `{{0,30},{5,10},{15,20}}` -> 2; `{{0,5},{5,8}}` -> 1.
4. `eraseOverlapIntervals(int[][])`: minimum removals so no two overlap (touching is fine). Values may be `Integer.MIN_VALUE`/`MAX_VALUE` (watch subtraction).

300k random intervals must finish under the timeout. Explain why (4) sorts by END and (1) by START.
