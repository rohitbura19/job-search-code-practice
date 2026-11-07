package practice.d20261107.c1;

public final class TreeCodec {
    private TreeCodec() {}

    /**
     * Rebuild the tree from its preorder and inorder traversals (all values distinct, same length). O(n): index the inorder
     * array in a map. Depth can reach n, so avoid unbounded recursion on the skewed test (use an explicit stack) -
     * or reason about it and say what you would do in production.
     */
    public static TreeNode buildTree(int[] preorder, int[] inorder) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Level-order text: values comma separated, "#" for a missing child, trailing "#" removed, empty tree -> "".
     * Example: 1 with children 2,3 and 3 with children 4,5 -> "1,2,3,#,#,4,5". Must not recurse (depth up to 100k).
     */
    public static String serialize(TreeNode root) { throw new UnsupportedOperationException("TODO"); }

    /** Inverse of serialize; accepts exactly the format above (including no trailing "#"). Must not recurse. */
    public static TreeNode deserialize(String data) { throw new UnsupportedOperationException("TODO"); }
}
