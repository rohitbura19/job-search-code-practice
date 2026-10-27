package practice.d20261027.a2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * BFS with a queue; freeze the level size before the inner loop so one pass handles exactly one level.
 * Zigzag reverses every odd level (or uses addFirst on a Deque). Right view = last element of each level.
 * All O(n) time, O(w) space (w = widest level). Iterative, so a 100000-deep skewed tree cannot overflow the stack.
 */
public final class LevelOrder {
    private LevelOrder() {}

    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> out = new ArrayList<>();
        if (root == null) return out;
        Deque<TreeNode> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> level = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                TreeNode n = q.poll();
                level.add(n.val);
                if (n.left != null) q.add(n.left);
                if (n.right != null) q.add(n.right);
            }
            out.add(level);
        }
        return out;
    }

    public static List<List<Integer>> zigzag(TreeNode root) {
        List<List<Integer>> out = levelOrder(root);
        for (int i = 1; i < out.size(); i += 2) Collections.reverse(out.get(i));
        return out;
    }

    public static List<Integer> rightSideView(TreeNode root) {
        List<Integer> out = new ArrayList<>();
        for (List<Integer> level : levelOrder(root)) out.add(level.get(level.size() - 1));
        return out;
    }
}
