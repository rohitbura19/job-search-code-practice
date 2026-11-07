# A2 - Linked lists (10 min) - `a2/LinkedLists.java` (`ListNode` is given)

1. `mergeTwoLists(a, b)` - merge two ascending lists by splicing existing nodes. Ties: take from `a` first. `[1,2,4] + [1,3,4]` -> `[1,1,2,3,4,4]`; `null + null` -> `null`.
2. `removeNthFromEnd(head, n)` - remove the n-th node from the end (1 = last) in ONE pass; `1 <= n <= length`. `[1,2,3,4,5], 2` -> `[1,2,3,5]`; `[1], 1` -> `null`.
3. `middleNode(head)` - the middle node; for an even length the SECOND middle. `[1..5]` -> 3, `[1..6]` -> 4.

Constraints: up to 2,000,000 nodes: no recursion. O(1) extra space.
