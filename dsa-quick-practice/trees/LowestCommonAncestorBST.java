package trees;

import java.util.ArrayDeque;
import java.util.Queue;

// Lowest Common Ancestor of a BST  [Medium | Walk down using BST order, O(1) space]
// Return the LCA value; a node is its own ancestor.
//
// [6,2,8,0,4,7,9,null,null,3,5], p=2, q=8 -> 6
// same tree, p=2, q=4                      -> 2

public class LowestCommonAncestorBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static int lowestCommonAncestor(TreeNode root, int p, int q) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        TreeNode root = build(6, 2, 8, 0, 4, 7, 9, null, null, 3, 5);
        System.out.println(lowestCommonAncestor(root, 2, 8)); // 6
        System.out.println(lowestCommonAncestor(root, 2, 4)); // 2
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
