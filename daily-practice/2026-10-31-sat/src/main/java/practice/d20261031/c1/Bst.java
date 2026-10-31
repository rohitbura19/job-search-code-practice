package practice.d20261031.c1;

public final class Bst {
    private Bst() {}

    /** Strict BST: left subtree < node < right subtree, for the WHOLE subtree, not just the children. Values may be Integer.MIN/MAX. Depth may be 100_000. */
    public static boolean isValidBST(TreeNode root) { throw new UnsupportedOperationException("TODO"); }

    /** k-th smallest (1-based) value in a valid BST, iteratively. k is always in range. */
    public static int kthSmallest(TreeNode root, int k) { throw new UnsupportedOperationException("TODO"); }

    /** Lowest common ancestor VALUE of two values that exist in the BST (a node is its own ancestor). Iterative, O(h), O(1) space. */
    public static int lowestCommonAncestor(TreeNode root, int p, int q) { throw new UnsupportedOperationException("TODO"); }

    /** Height-balanced BST from a strictly ascending array (subtree heights differ by at most 1). Empty array -> null. */
    public static TreeNode sortedArrayToBST(int[] sorted) { throw new UnsupportedOperationException("TODO"); }
}
