# C1 - Trees: rebuild and serialize (25 min) - `c1/TreeCodec.java` (`TreeNode` is given)

1. `buildTree(int[] preorder, int[] inorder)` - rebuild the binary tree (distinct values). Example: preorder `[3,9,20,15,7]`, inorder `[9,3,15,20,7]` -> `3 / (9, 20 / (15, 7))`. Empty arrays -> `null`. Expected O(n); the tree may be a 100,000-node chain, so explain (or avoid) recursion depth.
2. `serialize(root)` / `deserialize(text)` - level-order text, values comma separated, `#` for a missing child, trailing `#` removed, empty tree -> `""`. `1` with children `2` and `3`, and `3` with children `4`, `5` -> `"1,2,3,#,#,4,5"`. Values are any `int` (negative too). Both directions iterative.
