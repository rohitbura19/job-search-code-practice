> Code: `src/main/java/practice/d20261026/a2/Trees.java` (`TreeNode` is given). Test: `TreesTest`. **15 min.**

# A2. Invert a binary tree + symmetric tree

**(a) `invert(root)`**: mirror the tree in place (every node's children swapped) and return the root.

**(b) `isSymmetric(root)`**: true if the tree equals its own mirror image around the root.

```
      4                4          symmetric:   1
    /   \            /   \                    / \
   2     7    ->    7     2                  2   2
  / \   / \        / \   / \                / \ / \
 1   3 6   9      9   6 3   1              3  4 4  3
```

Constraints: up to `10^5` nodes, depth up to `10^5` (a chain), values any `int`. A recursive solution overflows the stack here: write both iteratively.

Say out loud: which pairs must mirror each other (and why not `(l.left, r.left)`); why `ArrayDeque` cannot hold the null pairs; the recursive version in one line and what its worst-case stack depth is.
