package trees;

import java.util.ArrayDeque;
import java.util.Queue;

// Path Sum  [Easy | DFS with remaining target]
// Is there a ROOT-TO-LEAF path that sums to target? Empty tree -> false.
//
// [5,4,8,11,null,13,4,7,2,null,null,null,1], 22 -> true  (5+4+11+2)
// [1,2,3], 5                                    -> false

public class PathSum {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static boolean hasPathSum(TreeNode root, int target) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(hasPathSum(build(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1), 22)); // true
        System.out.println(hasPathSum(build(1, 2, 3), 5));                                             // false
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
