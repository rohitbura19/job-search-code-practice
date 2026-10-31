# C1. Binary search tree (20 min) - `c1/Bst.java` (`TreeNode` is given)

1. `isValidBST(root)` - strict BST (no duplicates), bounds from ALL ancestors. Values include `Integer.MIN_VALUE/MAX_VALUE` (a `long` bound or a "previous value" walk handles them). Depth may reach 100 000: no recursion.
2. `kthSmallest(root, k)` - 1-based, iterative in-order, stop early. Follow-up: the tree is modified often and `k` is queried often; how do you get O(h)? (augment nodes with subtree sizes)
3. `lowestCommonAncestor(root, p, q)` - values (both present), a node is its own ancestor, O(h) time, O(1) space.
4. `sortedArrayToBST(int[])` - height-balanced (subtree heights differ by at most 1) from a strictly ascending array.
Examples: `[2,1,3]` valid; `[5,1,4,null,null,3,6]` invalid; `[5,4,6,null,null,3,7]` invalid (3 is in the right subtree of 5); LCA of 2 and 8 in `[6,2,8,0,4,7,9,null,null,3,5]` is 6, of 3 and 5 is 4.
