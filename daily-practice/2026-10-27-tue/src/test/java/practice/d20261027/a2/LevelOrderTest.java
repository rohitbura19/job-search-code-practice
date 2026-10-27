package practice.d20261027.a2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import org.junit.jupiter.api.Test;

class LevelOrderTest {
    /** Builds a tree from LeetCode style level order with nulls, e.g. build(3, 9, 20, null, null, 15, 7). */
    static TreeNode build(Integer... v) {
        if (v.length == 0 || v[0] == null) return null;
        TreeNode root = new TreeNode(v[0]);
        Queue<TreeNode> q = new ArrayDeque<>();
        q.add(root);
        int i = 1;
        while (!q.isEmpty() && i < v.length) {
            TreeNode n = q.poll();
            if (i < v.length && v[i] != null) { n.left = new TreeNode(v[i]); q.add(n.left); }
            i++;
            if (i < v.length && v[i] != null) { n.right = new TreeNode(v[i]); q.add(n.right); }
            i++;
        }
        return root;
    }

    static final TreeNode SAMPLE = build(3, 9, 20, null, null, 15, 7);

    @Test void levelOrderExample() {
        assertEquals(List.of(List.of(3), List.of(9, 20), List.of(15, 7)), LevelOrder.levelOrder(SAMPLE));
    }

    @Test void zigzagExample() {
        assertEquals(List.of(List.of(3), List.of(20, 9), List.of(15, 7)), LevelOrder.zigzag(SAMPLE));
    }

    @Test void rightSideViewSeesPastShorterRightBranch() {
        assertEquals(List.of(1, 3, 4), LevelOrder.rightSideView(build(1, 2, 3, null, 5, null, 4)));
        assertEquals(List.of(1, 2, 5), LevelOrder.rightSideView(build(1, 2, null, 5)));
    }

    @Test void emptyAndSingle() {
        assertEquals(List.of(), LevelOrder.levelOrder(null));
        assertEquals(List.of(), LevelOrder.zigzag(null));
        assertEquals(List.of(), LevelOrder.rightSideView(null));
        assertEquals(List.of(7), LevelOrder.rightSideView(new TreeNode(7)));
        assertEquals(List.of(List.of(7)), LevelOrder.zigzag(new TreeNode(7)));
    }

    @Test void duplicatesAndNegatives() {
        TreeNode t = build(-1, -1, -1, -1, -1);
        assertEquals(List.of(List.of(-1), List.of(-1, -1), List.of(-1, -1)), LevelOrder.levelOrder(t));
    }

    @Test void deepSkewedTreeDoesNotOverflowTheStack() {
        TreeNode root = new TreeNode(0), cur = root;
        for (int i = 1; i < 100_000; i++) { cur.right = new TreeNode(i); cur = cur.right; }
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(100_000, LevelOrder.levelOrder(root).size());
            assertEquals(99_999, LevelOrder.rightSideView(root).get(99_999));
        });
    }
}
