package trees;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

// Binary Tree Zigzag Level Order  [Medium | BFS, alternate direction]
//
//     3
//    / \
//   9  20      -> [[3],[20,9],[15,7]]
//     /  \
//    15   7

public class ZigzagLevelOrder {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(zigzagLevelOrder(build(3, 9, 20, null, null, 15, 7))); // [[3], [20, 9], [15, 7]]
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
