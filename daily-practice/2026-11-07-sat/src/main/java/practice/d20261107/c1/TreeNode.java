package practice.d20261107.c1;

/** Binary tree node (data class, given). */
public class TreeNode {
    public int val;
    public TreeNode left, right;

    public TreeNode(int val) { this.val = val; }

    public TreeNode(int val, TreeNode left, TreeNode right) { this.val = val; this.left = left; this.right = right; }

    public static boolean sameTree(TreeNode a, TreeNode b) {
        if (a == null || b == null) return a == b;
        return a.val == b.val && sameTree(a.left, b.left) && sameTree(a.right, b.right);
    }
}
