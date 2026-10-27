> Code: `src/main/java/practice/d20261027/a2/LevelOrder.java` (`TreeNode` is given). Test: `LevelOrderTest`. **15 min.**

# A2. Binary tree level order, zigzag, right-side view

Given the root of a binary tree (up to 100000 nodes, possibly a 100000-deep chain):
1. `levelOrder`: values level by level, left to right. `[3,9,20,null,null,15,7]` -> `[[3],[9,20],[15,7]]`.
2. `zigzag`: level 0 left-to-right, level 1 right-to-left, alternating -> `[[3],[20,9],[15,7]]`.
3. `rightSideView`: the last node of each level -> `[3,20,7]`. Note a node on the left can be visible when the right branch is shorter: `[1,2,3,null,5,null,4]` -> `[1,3,4]`.

Iterative BFS only (a recursive solution can overflow on the chain). Say out loud: why the level size is frozen before the inner loop.
