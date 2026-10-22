> Code: `c1/MergeIntervals.java` (`merge`, then the follow-up `minMeetingRooms`). Test: `MergeIntervalsTest`.

### C1. Merge Intervals (Sorting)
Merge all overlapping intervals. `[[1,3],[2,6],[8,10],[15,18]] -> [[1,6],[8,10],[15,18]]`. Touching intervals `[1,4],[4,5]` merge.

Interviewer follow-up: intervals arrive as a stream (insert one at a time) — what structure? Return the minimum number of meeting rooms instead.

(Kit contract for meeting rooms: a meeting `[s, e]` frees its room at `e`, so `[1,4]` and `[4,5]` share one room.)
