package practice.d20261026.a2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Both are iterative: the tests build 100,000-deep chains and a recursive version overflows the stack.
 * invert: explicit stack, swap the children of each popped node. O(n) time, O(h) space.
 * isSymmetric: a queue of node PAIRS that must mirror each other: (l.left, r.right) and (l.right, r.left). A LinkedList is
 * used because ArrayDeque rejects null elements. O(n) time, O(w) space.
 */
public final class Trees {
    private Trees() {}

    public static TreeNode invert(TreeNode root) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        if (root != null) stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode n = stack.pop();
            TreeNode t = n.left; n.left = n.right; n.right = t;
            if (n.left != null) stack.push(n.left);
            if (n.right != null) stack.push(n.right);
        }
        return root;
    }

    public static boolean isSymmetric(TreeNode root) {
        if (root == null) return true;
        Deque<TreeNode> q = new LinkedList<>();
        q.add(root.left);
        q.add(root.right);
        while (!q.isEmpty()) {
            TreeNode l = q.poll(), r = q.poll();
            if (l == null && r == null) continue;
            if (l == null || r == null || l.val != r.val) return false;
            q.add(l.left);  q.add(r.right);
            q.add(l.right); q.add(r.left);
        }
        return true;
    }
}
