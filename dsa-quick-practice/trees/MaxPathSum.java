package trees;

import java.util.ArrayDeque;
import java.util.Queue;

// Binary Tree Maximum Path Sum  [Hard | Return best downward gain, record best through node]
// Path = any node-to-node chain, at least one node. Values may be negative.
//
// [1,2,3]                      -> 6
// [-10,9,20,null,null,15,7]    -> 42

public class MaxPathSum {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static int maxPathSum(TreeNode root) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(maxPathSum(build(1, 2, 3)));                        // 6
        System.out.println(maxPathSum(build(-10, 9, 20, null, null, 15, 7)));  // 42
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
