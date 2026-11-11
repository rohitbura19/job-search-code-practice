# C2 - Floyd and frequency (20 min) - `c2/FloydAndFrequency.java`

1. `findDuplicate(int[] nums)`: n+1 values in `[1,n]`, one value repeats (possibly many times). Return it without modifying the array, O(1) space, O(n) time. `[1,3,4,2,2]` -> 2.
2. `cycleStart(ListNode head)`: the node where the cycle begins or null. O(1) space. (`ListNode` is given.)
3. `topKFrequent(int[] nums, int k)`: the k most frequent values, any order, O(n) (bucket by frequency). `[1,1,1,2,2,3]`, k=2 -> `{1,2}`.

Constraints: n up to 2,000,000; values of topKFrequent are any int.
