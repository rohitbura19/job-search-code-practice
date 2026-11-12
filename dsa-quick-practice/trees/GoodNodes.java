package trees;

import java.util.ArrayDeque;
import java.util.Queue;

// Count Good Nodes in Binary Tree  [Medium | DFS carrying path max]
// A node is good if no value on the root-to-node path is greater than it.
//
// [3,1,4,3,null,1,5] -> 4
// [3,3,null,4,2]     -> 3

public class GoodNodes {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static int goodNodes(TreeNode root) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(goodNodes(build(3, 1, 4, 3, null, 1, 5))); // 4
        System.out.println(goodNodes(build(3, 3, null, 4, 2)));       // 3
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
