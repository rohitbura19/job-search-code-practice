package trees;

import java.util.ArrayDeque;
import java.util.Queue;

// Balanced Binary Tree  [Easy | Height with -1 as "unbalanced"]
// For every node, subtree heights differ by at most 1. O(n).
//
// [3,9,20,null,null,15,7]   -> true
// [1,2,2,3,3,null,null,4,4] -> false

public class BalancedBinaryTree {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static boolean isBalanced(TreeNode root) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isBalanced(build(3, 9, 20, null, null, 15, 7)));     // true
        System.out.println(isBalanced(build(1, 2, 2, 3, 3, null, null, 4, 4))); // false
    }

    static TreeNode build(Integer... v) {
        if (v.length == 0 || v[0] == null) return null;
        TreeNode root = new TreeNode(v[0]);
        Queue<TreeNode> q = new ArrayDeque<>();
        q.add(root);
        for (int i = 1; i < v.length; ) {
            TreeNode n = q.poll();
            if (i < v.length && v[i] != null) q.add(n.left = new TreeNode(v[i]));
            i++;
            if (i < v.length && v[i] != null) q.add(n.right = new TreeNode(v[i]));
            i++;
        }
        return root;
    }
}
