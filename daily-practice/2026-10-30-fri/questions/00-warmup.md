# Warm-up (10 min, blank page, no IDE)

Spaced repetition from the last two weeks. State the invariant or recurrence in one sentence each; mark "needed hints" in the score card.

1. Sweep after sorting (10-23 histogram, 10-27 3Sum): what does sorting buy you, and what is the loop invariant after each step?
2. Cycle / middle of a list (10-24 list cycle): where do slow and fast meet, and why is the middle `slow` when `fast.next.next == null`?
3. Binary search on the answer (10-29 Koko): what must be true of the predicate, and what is the loop invariant on `[lo, hi]`?
4. Idempotent retry (10-26 resumable upload, 10-29 confirm): what makes a repeated request safe without a second side effect? Today's G needs it for `commit`.
5. DP over two strings (10-26 edit distance): what do `dp[i][j]` mean and which cells does a transition read? D1 today has the same shape.
6. Fencing / stale owner (10-27 lock lease): what stops a paused old owner from writing after a new owner took over? G has the same problem after a rebalance.
