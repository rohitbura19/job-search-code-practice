package practice.d20261103.a2;

/**
 * All three are one post-order pass that returns a height and updates a side result, so each node is visited once: O(n) time, O(h) stack.
 * diameter: at each node the longest path through it is leftHeight + rightHeight (in edges).
 * isBalanced: return -1 as an "unbalanced" sentinel that short-circuits the rest.
 * hasPathSum: only a LEAF ends a path (a node with one child is not a leaf).
 */
public final class TreeRecursion {
    private TreeRecursion() {}

    public static int diameter(TreeNode root) {
        int[] best = {0};
        height(root, best);
        return best[0];
    }

    private static int height(TreeNode n, int[] best) {
        if (n == null) return 0;
        int l = height(n.left, best), r = height(n.right, best);
        best[0] = Math.max(best[0], l + r);
        return 1 + Math.max(l, r);
    }

    public static boolean isBalanced(TreeNode root) { return check(root) >= 0; }

    private static int check(TreeNode n) {
        if (n == null) return 0;
        int l = check(n.left);
        if (l < 0) return -1;
        int r = check(n.right);
        if (r < 0 || Math.abs(l - r) > 1) return -1;
        return 1 + Math.max(l, r);
    }

    public static boolean hasPathSum(TreeNode root, long target) {
        if (root == null) return false;
        long rest = target - root.val;
        if (root.left == null && root.right == null) return rest == 0;
        return hasPathSum(root.left, rest) || hasPathSum(root.right, rest);
    }
}
