package practice.d20261111.c1;

/** The tree may be a 100,000-node chain: recursion overflows the stack, so use an explicit stack / iterative post-order. Values may be negative (maxPathSum). */
public final class TreeDp {
    private TreeDp() {}

    /** House Robber III: you may not take a node and its parent/child together. Maximum total value. Empty tree -> 0. Values are >= 0, the sum fits in long. */
    public static long robIII(TreeNode root) { throw new UnsupportedOperationException("TODO"); }

    /** Maximum path sum of a NON-EMPTY tree; a path is any downward-then-upward chain (may start and end anywhere, at least one node). */
    public static long maxPathSum(TreeNode root) { throw new UnsupportedOperationException("TODO"); }

    /** Count nodes whose value is >= every value on the path from the root to it (the root counts). Empty tree -> 0. */
    public static int goodNodes(TreeNode root) { throw new UnsupportedOperationException("TODO"); }
}
