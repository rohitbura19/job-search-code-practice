package trees;

import java.util.ArrayDeque;
import java.util.Queue;

// House Robber III  [Medium | Tree DP: (rob, skip) per node]
// Can't take a node together with its parent or child. Max total.
//
// [3,2,3,null,3,null,1] -> 7
// [3,4,5,1,3,null,1]    -> 9

public class HouseRobberIII {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static int rob(TreeNode root) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(rob(build(3, 2, 3, null, 3, null, 1))); // 7
        System.out.println(rob(build(3, 4, 5, 1, 3, null, 1)));    // 9
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
