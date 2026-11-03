# A2. Tree recursion: diameter, balanced, path sum (10 min)

> Code: `src/main/java/practice/d20261103/a2/TreeRecursion.java` (uses the given `TreeNode`). Test: `TreeRecursionTest`.

1. `diameter(root)`: longest path between any two nodes, counted in edges. Example `[1,2,3,4,5]` -> 3 (4-2-1-3).
2. `isBalanced(root)`: every node's subtree heights differ by at most 1. Do it in O(n), not by recomputing heights at every node.
3. `hasPathSum(root, long target)`: is there a ROOT-TO-LEAF path with that sum? `[1,2]` and target 1 -> false (node 1 is not a leaf).

Say aloud: what does each recursion return, and what is the side result? Depth in the tests is up to 5000 (recursion is fine); what would you do for 10^6?
