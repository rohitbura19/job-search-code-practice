# Reference answers

Java files mirror `src/main/java` (copy over the stubs to run the tests). Given-complete classes (`Enrollment`, `TreeNode`, `ListNode`, `Comment`, `CommentSort`, `H1Snippets`) are not repeated. Explanations are in each file's header. Spring: `answers/e-spring-notifications/` holds the completed gap files (copy over `e-spring-notifications/src/main/java/practice/e`).

| Item | Time | Space |
|---|---|---|
| A1 thirdMax, one-flip ones, flowerbed | O(n) each | O(1) |
| A2 MovingAverage, RecentCounter, backspaceCompare | O(1) per call, amortised O(1), O(n + m) | O(size), O(window), O(1) |
| B1 streams | O(n) / O(s log s) for honourRoll | O(n) |
| C1 robIII, maxPathSum, goodNodes | O(n) | O(n), iterative |
| C2 findDuplicate, cycleStart, topKFrequent | O(n) | O(1), O(1), O(n) |
| D1 skyline | O(n log n) | O(n) |
| G CommentService | add/delete O(depth); vote O(1); topLevel O(t log t); replies O(c log c); countVisible O(visible) | O(comments + votes) |
| H2 ObjectPool | O(1) per op | O(capacity) |

Key points: `thirdMax` uses `long` sentinels. Floyd restarts one pointer at the head, both step by one to the entrance. Rob III keeps (take, skip) per node; every tree DP here walks the reversed pre-order, no recursion. Skyline events are (x, -h) for starts and (x, h) for ends so the sort order encodes the tie rules. Comment visibility = `liveInSubtree > 0`, maintained along the ancestor chain. H1 values were produced by running the snippets (JDK 21); the rules are in the README review.
