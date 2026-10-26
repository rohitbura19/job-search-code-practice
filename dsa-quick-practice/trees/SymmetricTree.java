package trees;

// Symmetric Tree  [Easy | DFS on two nodes]
// True if the tree is a mirror of itself.
//
//       1
//      / \
//     2   2      -> true
//    / \ / \
//   3  4 4  3

public class SymmetricTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) { this.val = val; this.left = left; this.right = right; }
    }

    static boolean isSymmetric(TreeNode root) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        TreeNode yes = new TreeNode(1,
                new TreeNode(2, new TreeNode(3), new TreeNode(4)),
                new TreeNode(2, new TreeNode(4), new TreeNode(3)));
        TreeNode no = new TreeNode(1,
                new TreeNode(2, null, new TreeNode(3)),
                new TreeNode(2, null, new TreeNode(3)));
        System.out.println(isSymmetric(yes)); // true
        System.out.println(isSymmetric(no));  // false
    }
}
