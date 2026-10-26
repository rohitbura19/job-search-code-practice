package trees;

// Diameter of Binary Tree  [Easy | DFS, return height, record best]
// Length in edges of the longest path between any two nodes.
//
//       1
//      / \
//     2   3      -> 3   (4-2-1-3)
//    / \
//   4   5

public class DiameterOfBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) { this.val = val; this.left = left; this.right = right; }
    }

    static int diameterOfBinaryTree(TreeNode root) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(4), new TreeNode(5)),
                new TreeNode(3));
        System.out.println(diameterOfBinaryTree(root)); // 3
    }
}
