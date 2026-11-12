package trees;

import java.util.ArrayDeque;
import java.util.Queue;

// Validate Binary Search Tree  [Medium | Bounds or inorder]
// Strict: whole left subtree < node < whole right subtree.
//
// [2,1,3]               -> true
// [5,1,4,null,null,3,6] -> false  (3 is in 5's right subtree)

public class ValidateBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static boolean isValidBST(TreeNode root) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isValidBST(build(2, 1, 3)));                   // true
        System.out.println(isValidBST(build(5, 1, 4, null, null, 3, 6))); // false
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
