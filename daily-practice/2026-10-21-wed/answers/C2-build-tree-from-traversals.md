# Answers: C2-build-tree-from-traversals

(Spoilers. Open only after your attempt.)

**Reference**
```java
static final class Node { int val; Node left, right; Node(int v) { val = v; } }
private static int pi; private static Map<Integer, Integer> idx;           // fine for a drill; use a small class with fields (not statics) in real code
static Node build(int[] pre, int[] in) {
    idx = new HashMap<>(); for (int i = 0; i < in.length; i++) idx.put(in[i], i);     // value -> position in inorder: O(1) split
    pi = 0; return go(pre, 0, in.length - 1);
}
private static Node go(int[] pre, int l, int r) {          // [l, r] = the slice of inorder that belongs to this subtree
    if (l > r) return null;
    Node n = new Node(pre[pi++]);                          // the next unread preorder element is this subtree's root
    int m = idx.get(n.val);
    n.left = go(pre, l, m - 1);                            // LEFT first: preorder lists root, whole left subtree, whole right subtree
    n.right = go(pre, m + 1, r);
    return n;
}
// postorder + inorder: pi = post.length - 1;  n = new Node(post[pi--]);  n.right = go2(m + 1, r);  n.left = go2(l, m - 1);    // RIGHT first
```
Verified: the example gives the tree (3 with left 9 and right 20 over 15 and 7), `[-1]` gives a single node, `[1,2,3]` with inorder `[3,2,1]` gives a left chain; the postorder + inorder variant gives the same tree for the same example; 3,000 random trees of 0 to 9 nodes were rebuilt from preorder + inorder AND from postorder + inorder and matched the original structure exactly. A left-skewed chain of 200,000 nodes overflowed the stack (`StackOverflowError` on a 512 KB thread stack), and duplicate values (`pre = [1,1]`, `in = [1,1]`) crashed with `ArrayIndexOutOfBoundsException` because the map keeps only the LAST index of a value. O(n) time (every node is created once, every lookup is O(1)), O(n) space for the map plus O(h) recursion. Idea: the first preorder element is the root; its position `m` in inorder splits the inorder slice into the left subtree (before `m`) and the right subtree (after `m`); recurse on the slices. The shared index `pi` makes the left call consume exactly the left subtree's elements, so the right call starts at the right place; swap the two calls and the tree is wrong. Postorder is the mirror image: root last, so read from the end and build right first. Preorder + postorder is ambiguous whenever a node has exactly one child (the child could be left or right); it is unique for a FULL binary tree (every node has 0 or 2 children). Duplicates make the split ambiguous; ask the interviewer, or carry the extra information (positions) in the input. Skewed input: run on a thread with a big stack or convert to an explicit stack (the iterative preorder + inorder algorithm compares the top of the stack with the next inorder element); a production parser would use the iterative form. Serialising with `#` for null children makes preorder alone enough (the structure is encoded), which is why LeetCode 297 only needs one traversal. Common mistakes: building right before left, passing preorder slice bounds around instead of one moving index, and an O(n) search for the root per call.
