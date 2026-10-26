package trees;

// Construct Binary Tree from Preorder and Inorder  [Medium | Recursion + index map]
// Unique values. Rebuild the tree.
//
// preorder=[3,9,20,15,7], inorder=[9,3,15,20,7]
//
//     3
//    / \
//   9  20
//     /  \
//    15   7

public class BuildTreePreorderInorder {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static TreeNode buildTree(int[] preorder, int[] inorder) {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        TreeNode root = buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        System.out.println(postorder(root).trim()); // 9 15 7 20 3
    }

    static String postorder(TreeNode n) {
        return n == null ? "" : postorder(n.left) + postorder(n.right) + n.val + " ";
    }
}
