package trees;

// Invert Binary Tree  [Easy | DFS or BFS]
// Mirror the tree (swap every node's children). Bonus: do it iteratively.
//
//       4              4
//     /   \          /   \
//    2     7   ->   7     2
//   / \   / \      / \   / \
//  1   3 6   9    9   6 3   1

public class InvertBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) { this.val = val; this.left = left; this.right = right; }
    }

    static TreeNode invertTree(TreeNode root) {
        // TODO
        return root;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(4,
                new TreeNode(2, new TreeNode(1), new TreeNode(3)),
                new TreeNode(7, new TreeNode(6), new TreeNode(9)));
        System.out.println(inorder(invertTree(root)).trim()); // 9 7 6 4 3 2 1
    }

    static String inorder(TreeNode n) {
        return n == null ? "" : inorder(n.left) + n.val + " " + inorder(n.right);
    }
}
