> Code: `src/main/java/practice/d20261024/a2/` (`ListNode` is given; implement `ListCycle`). Test: `ListCycleTest`.

# A2. Linked List Cycle, then the entry node of the cycle - Floyd's tortoise and hare (15 min)

(a) `hasCycle(head)`: does following `next` ever fail to reach `null`? (b) `cycleStart(head)`: the first node of the cycle, or `null`.
`1 -> 2 -> 3 -> 4 -> 2 (tail points back to node 2)`: `hasCycle = true`, `cycleStart = node 2`. A single node pointing at itself is a cycle starting at itself.

Constraints: up to `2*10^6` nodes, O(n) time, O(1) extra space (no `HashSet` of visited nodes). Nodes are compared by identity; values may repeat.

Say out loud: why fast (2 steps) and slow (1 step) must meet inside a cycle and cannot jump over each other; the algebra that proves "one pointer from the head and one from the meeting point, moving 1 step each, meet at the entry"; how you would also get the cycle length.
