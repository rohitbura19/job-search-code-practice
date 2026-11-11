# C1 - Tree DP without recursion (25 min) - `c1/TreeDp.java`

`TreeNode` is given. The tree can be a 200,000-node chain: recursion overflows the stack, so use an explicit stack / iterative post-order.

1. `robIII(root)` - House Robber III: pick nodes so that no parent and child are both chosen, maximise the sum (values >= 0, result is a `long`). `[3,2,3,null,3,null,1]` -> 7; `[3,4,5,1,3,null,1]` -> 9.
2. `maxPathSum(root)` - maximum sum of any path (at least one node; a path goes up then down through one top node). Values may be negative. `[-10,9,20,null,null,15,7]` -> 42; `[-3]` -> -3.
3. `goodNodes(root)` - count nodes whose value is >= every value on the path root -> node. `[3,1,4,3,null,1,5]` -> 4.
