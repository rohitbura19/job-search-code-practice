package trees;

import java.util.ArrayDeque;
import java.util.Queue;

// Kth Smallest Element in a BST  [Medium | Iterative inorder]
//
// [3,1,4,null,2], k=1         -> 1
// [5,3,6,2,4,null,null,1], k=3 -> 3

public class KthSmallestBST {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static int kthSmallest(TreeNode root, int k) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(kthSmallest(build(3, 1, 4, null, 2), 1));               // 1
        System.out.println(kthSmallest(build(5, 3, 6, 2, 4, null, null, 1), 3));   // 3
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
