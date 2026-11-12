package trees;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

// Binary Tree Level Order Traversal  [Medium | BFS with level size]
//
//     3
//    / \
//   9  20      -> [[3],[9,20],[15,7]]
//     /  \
//    15   7

public class LevelOrderTraversal {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static List<List<Integer>> levelOrder(TreeNode root) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(levelOrder(build(3, 9, 20, null, null, 15, 7))); // [[3], [9, 20], [15, 7]]
    }

    // Builds a tree from level-order values (null = missing child).
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
