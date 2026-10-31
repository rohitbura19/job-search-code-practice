package practice.d20261031.c1;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * isValidBST: an in-order walk of a BST is strictly increasing, so keep the previous value (a long-free way: a boolean "has prev"
 * plus the int). Iterative with an explicit stack so a 100k-deep chain does not overflow the call stack. O(n) time, O(h) space.
 * The classic bug is comparing only a node with its children; the bounds must come from every ancestor.
 * kthSmallest: the same in-order walk, stop at the k-th pop. O(h + k).
 * lowestCommonAncestor: walk down; while both values are on the same side, go that way; the first split (or hit) is the LCA. O(h), O(1).
 * sortedArrayToBST: the middle element is the root, recurse on halves. O(n), depth O(log n).
 */
public final class Bst {
    private Bst() {}

    public static boolean isValidBST(TreeNode root) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        boolean hasPrev = false;
        int prev = 0;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) { stack.push(cur); cur = cur.left; }
            cur = stack.pop();
            if (hasPrev && cur.val <= prev) return false;
            hasPrev = true;
            prev = cur.val;
            cur = cur.right;
        }
        return true;
    }

    public static int kthSmallest(TreeNode root, int k) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            while (cur != null) { stack.push(cur); cur = cur.left; }
            cur = stack.pop();
            if (--k == 0) return cur.val;
            cur = cur.right;
        }
        throw new IllegalArgumentException("k out of range");
    }

    public static int lowestCommonAncestor(TreeNode root, int p, int q) {
        TreeNode cur = root;
        while (cur != null) {
            if (p < cur.val && q < cur.val) cur = cur.left;
            else if (p > cur.val && q > cur.val) cur = cur.right;
            else return cur.val;
        }
        throw new IllegalArgumentException("empty tree");
    }

    public static TreeNode sortedArrayToBST(int[] a) { return build(a, 0, a.length - 1); }

    private static TreeNode build(int[] a, int lo, int hi) {
        if (lo > hi) return null;
        int mid = (lo + hi) >>> 1;
        return new TreeNode(a[mid], build(a, lo, mid - 1), build(a, mid + 1, hi));
    }
}
