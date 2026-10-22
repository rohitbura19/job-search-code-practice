# Answers: A1-diameter-of-binary-tree

(Spoilers. Open only after your attempt.)

**Reference**
```java
private int best = 0;
public int diameterOfBinaryTree(TreeNode root) { height(root); return best; }

private int height(TreeNode n) {           // returns height, records diameter as a side effect
    if (n == null) return 0;
    int l = height(n.left), r = height(n.right);
    best = Math.max(best, l + r);          // path that bends at n
    return 1 + Math.max(l, r);             // path that continues up to the parent
}
```
O(n) time, O(h) stack. The key insight: the value you RETURN to the parent (a straight path) differs from the value you RECORD (a path that bends at this node). Same pattern solves max path sum (clamp negative child contributions to 0) and longest univalue path. For n-ary trees keep the two largest child heights. A skewed tree of 10^6 nodes overflows the default stack: convert to iterative post-order or run in a thread with a bigger stack.
