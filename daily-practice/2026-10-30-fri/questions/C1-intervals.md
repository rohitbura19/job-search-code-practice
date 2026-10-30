# C1. Intervals: merge, insert, meeting rooms (20 min)

Implement in `c1/Intervals.java`.

1. `merge`: merge overlapping closed intervals. `[1,4]` and `[4,5]` touch and merge; `[1,2]` and `[3,4]` do not.
2. `insert`: the list is already sorted and disjoint; insert one interval, O(n), no sort.
3. `minMeetingRooms`: half-open `[start, end)`; a meeting that ends at 5 and one that starts at 5 share a room. Up to 2 * 10^5 meetings.

Constraints: values anywhere in the int range, so `a - b` comparators are wrong; do not modify the input array of `merge`.
Say out loud: why do the two sorted arrays (starts, ends) work for rooms without tracking which meeting is which?
