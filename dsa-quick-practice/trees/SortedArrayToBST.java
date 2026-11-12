package trees;

// Convert Sorted Array to BST  [Easy | Pick middle, recurse]
// Height-balanced BST from a strictly ascending array.
//
// [-10,-3,0,5,9] -> root 0; inorder prints -10 -3 0 5 9

public class SortedArrayToBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static TreeNode sortedArrayToBST(int[] nums) {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        TreeNode root = sortedArrayToBST(new int[]{-10, -3, 0, 5, 9});
        System.out.println(root == null ? null : root.val); // 0
        System.out.println(inorder(root).trim());           // -10 -3 0 5 9
        System.out.println(height(root));                   // 3
    }

    static String inorder(TreeNode n) {
        return n == null ? "" : inorder(n.left) + n.val + " " + inorder(n.right);
    }

    static int height(TreeNode n) {
        return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right));
    }
}
