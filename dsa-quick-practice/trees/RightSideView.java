package trees;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

// Binary Tree Right Side View  [Medium | BFS, last of each level]
//
//     1
//    / \
//   2   3      -> [1,3,4]
//    \   \
//     5   4

public class RightSideView {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static List<Integer> rightSideView(TreeNode root) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(rightSideView(build(1, 2, 3, null, 5, null, 4))); // [1, 3, 4]
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
