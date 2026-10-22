> Code: `src/main/java/practice/d20261013/a1/DiameterOfBinaryTree.java` (`TreeNode` is given). Test: `DiameterOfBinaryTreeTest`.

### A1. Diameter of Binary Tree (Tree recursion, return one thing, record another)
Given the root of a binary tree, return the length (in edges) of the longest path between any two nodes. The path may or may not pass through the root.
```
      1
     / \
    2   3        -> 3   (path 4-2-1-3 or 5-2-1-3)
   / \
  4   5
```
Interviewer follow-up: do it iteratively (post-order with an explicit stack). Now every edge has a weight, or the nodes have values and you want the maximum path sum. What changes for an n-ary tree? What is the recursion depth for a skewed tree of 10^6 nodes?
