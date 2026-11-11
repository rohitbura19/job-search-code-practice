package practice.d20261111.c1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/** Bottom-up DP needs children before parents: collect nodes in pre-order with a stack (parent always before its children), then walk that list backwards.
 *  robIII: per node (take, skip): take = val + skip(l) + skip(r); skip = max(take, skip)(l) + max(take, skip)(r).
 *  maxPathSum: down(v) = val + max(0, down(l), down(r)); the best path bending at v is val + max(0, down(l)) + max(0, down(r)).
 *  goodNodes: top-down, carry the maximum seen on the path. All O(n) time, O(n) space, no recursion. */
public final class TreeDp {
    private TreeDp() {}

    private static List<TreeNode> preorder(TreeNode root) {
        List<TreeNode> out = new ArrayList<>();
        if (root == null) return out;
        ArrayDeque<TreeNode> st = new ArrayDeque<>();
        st.push(root);
        while (!st.isEmpty()) {
            TreeNode n = st.pop();
            out.add(n);
            if (n.right != null) st.push(n.right);
            if (n.left != null) st.push(n.left);
        }
        return out;
    }

    public static long robIII(TreeNode root) {
        List<TreeNode> order = preorder(root);
        Map<TreeNode, long[]> res = new IdentityHashMap<>();        // [0] = take this node, [1] = skip this node
        for (int i = order.size() - 1; i >= 0; i--) {
            TreeNode n = order.get(i);
            long[] l = n.left == null ? new long[2] : res.get(n.left);
            long[] r = n.right == null ? new long[2] : res.get(n.right);
            res.put(n, new long[]{n.val + l[1] + r[1], Math.max(l[0], l[1]) + Math.max(r[0], r[1])});
        }
        if (root == null) return 0;
        long[] top = res.get(root);
        return Math.max(top[0], top[1]);
    }

    public static long maxPathSum(TreeNode root) {
        Map<TreeNode, Long> down = new IdentityHashMap<>();
        List<TreeNode> order = preorder(root);
        long best = Long.MIN_VALUE;
        for (int i = order.size() - 1; i >= 0; i--) {
            TreeNode n = order.get(i);
            long l = n.left == null ? 0 : Math.max(0, down.get(n.left));
            long r = n.right == null ? 0 : Math.max(0, down.get(n.right));
            best = Math.max(best, n.val + l + r);
            down.put(n, n.val + Math.max(l, r));
        }
        return best;
    }

    public static int goodNodes(TreeNode root) {
        if (root == null) return 0;
        int count = 0;
        ArrayDeque<TreeNode> nodes = new ArrayDeque<>();
        ArrayDeque<Integer> maxes = new ArrayDeque<>();
        nodes.push(root); maxes.push(root.val);
        while (!nodes.isEmpty()) {
            TreeNode n = nodes.pop();
            int m = maxes.pop();
            if (n.val >= m) count++;
            int next = Math.max(m, n.val);
            if (n.left != null) { nodes.push(n.left); maxes.push(next); }
            if (n.right != null) { nodes.push(n.right); maxes.push(next); }
        }
        return count;
    }
}
