package practice.d20261111.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class TreeDpTest {
    /** Level-order builder; null = missing child. */
    static TreeNode tree(Integer... v) {
        if (v.length == 0 || v[0] == null) return null;
        TreeNode root = new TreeNode(v[0]);
        Deque<TreeNode> q = new ArrayDeque<>(List.of(root));
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

    @Test void robExamples() {
        assertEquals(7, TreeDp.robIII(tree(3, 2, 3, null, 3, null, 1)));
        assertEquals(9, TreeDp.robIII(tree(3, 4, 5, 1, 3, null, 1)));
        assertEquals(0, TreeDp.robIII(null));
        assertEquals(5, TreeDp.robIII(tree(5)));
        assertEquals(10, TreeDp.robIII(tree(1, 10)));
    }

    @Test void maxPathExamples() {
        assertEquals(6, TreeDp.maxPathSum(tree(1, 2, 3)));
        assertEquals(42, TreeDp.maxPathSum(tree(-10, 9, 20, null, null, 15, 7)));
        assertEquals(-3, TreeDp.maxPathSum(tree(-3)));
        assertEquals(-1, TreeDp.maxPathSum(tree(-3, -1, -2)));
        assertEquals(2, TreeDp.maxPathSum(tree(2, -1)));
    }

    @Test void goodNodesExamples() {
        assertEquals(4, TreeDp.goodNodes(tree(3, 1, 4, 3, null, 1, 5)));
        assertEquals(3, TreeDp.goodNodes(tree(3, 3, null, 4, 2)));
        assertEquals(1, TreeDp.goodNodes(tree(1)));
        assertEquals(0, TreeDp.goodNodes(null));
        assertEquals(1, TreeDp.goodNodes(tree(5, 1, 2)));
    }

    // ---- recursive brute force on small random trees
    static TreeNode random(Random r, int n, int lo, int hi) {
        TreeNode root = new TreeNode(lo + r.nextInt(hi - lo + 1));
        List<TreeNode> slots = new ArrayList<>(List.of(root));
        for (int i = 1; i < n; i++) {
            TreeNode p = slots.get(r.nextInt(slots.size()));
            TreeNode c = new TreeNode(lo + r.nextInt(hi - lo + 1));
            if (p.left == null && (p.right != null || r.nextBoolean())) p.left = c; else if (p.right == null) p.right = c; else { i--; continue; }
            slots.add(c);
        }
        return root;
    }

    static long[] robRec(TreeNode n) {
        if (n == null) return new long[2];
        long[] l = robRec(n.left), r = robRec(n.right);
        return new long[]{n.val + l[1] + r[1], Math.max(l[0], l[1]) + Math.max(r[0], r[1])};
    }

    static long best;
    static long pathRec(TreeNode n) {
        if (n == null) return 0;
        long l = Math.max(0, pathRec(n.left)), r = Math.max(0, pathRec(n.right));
        best = Math.max(best, n.val + l + r);
        return n.val + Math.max(l, r);
    }

    static int goodRec(TreeNode n, int max) {
        if (n == null) return 0;
        return (n.val >= max ? 1 : 0) + goodRec(n.left, Math.max(max, n.val)) + goodRec(n.right, Math.max(max, n.val));
    }

    @Test void randomVsRecursive() {
        Random r = new Random(11);
        for (int t = 0; t < 1500; t++) {
            TreeNode root = random(r, 1 + r.nextInt(14), -8, 8);
            best = Long.MIN_VALUE; pathRec(root);
            assertEquals(best, TreeDp.maxPathSum(root));
            assertEquals(goodRec(root, Integer.MIN_VALUE), TreeDp.goodNodes(root));
            TreeNode pos = random(r, 1 + r.nextInt(14), 0, 9);
            long[] x = robRec(pos);
            assertEquals(Math.max(x[0], x[1]), TreeDp.robIII(pos));
        }
    }

    @Test void deepChainDoesNotOverflowStack() {
        TreeNode root = new TreeNode(1), cur = root;
        for (int i = 2; i <= 200_000; i++) { cur.left = new TreeNode(i % 7); cur = cur.left; }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertTrue(TreeDp.robIII(root) > 0);
            assertTrue(TreeDp.maxPathSum(root) > 0);
            assertTrue(TreeDp.goodNodes(root) >= 1);
        });
    }

    @Test void hugeValuesUseLong() {
        TreeNode root = tree(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(3L * Integer.MAX_VALUE, TreeDp.maxPathSum(root));
        assertEquals(2L * Integer.MAX_VALUE, TreeDp.robIII(root));
    }
}
