package practice.d20261103.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import org.junit.jupiter.api.Test;

class TreeRecursionTest {
    /** Level-order build, null = missing child. */
    static TreeNode tree(Integer... v) {
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

    @Test void diameterExamples() {
        assertEquals(0, TreeRecursion.diameter(null));
        assertEquals(0, TreeRecursion.diameter(tree(1)));
        assertEquals(3, TreeRecursion.diameter(tree(1, 2, 3, 4, 5)));
        assertEquals(1, TreeRecursion.diameter(tree(1, 2)));
        // the longest path (7-5-3-2-4-6-8) does not pass through the root
        assertEquals(6, TreeRecursion.diameter(tree(1, 2, null, 3, 4, 5, null, null, 6, 7, null, null, 8)));
    }

    @Test void balancedExamples() {
        assertTrue(TreeRecursion.isBalanced(null));
        assertTrue(TreeRecursion.isBalanced(tree(3, 9, 20, null, null, 15, 7)));
        assertFalse(TreeRecursion.isBalanced(tree(1, 2, 2, 3, 3, null, null, 4, 4)));
        // balanced at the root but not at a deeper node
        assertFalse(TreeRecursion.isBalanced(tree(1, 2, 3, 4, null, 5, 6, 7, null, null, null, null, null)));
    }

    @Test void pathSumExamples() {
        TreeNode t = tree(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1);
        assertTrue(TreeRecursion.hasPathSum(t, 22));
        assertFalse(TreeRecursion.hasPathSum(t, 5));      // 5 is not a leaf-ending path
        assertFalse(TreeRecursion.hasPathSum(tree(1, 2), 1)); // node 1 has a child, so it is not a leaf
        assertTrue(TreeRecursion.hasPathSum(tree(1, 2), 3));
        assertFalse(TreeRecursion.hasPathSum(null, 0));
        assertTrue(TreeRecursion.hasPathSum(tree(-3), -3));
    }

    @Test void pathSumOverflowNeedsLong() {
        TreeNode t = new TreeNode(Integer.MAX_VALUE, new TreeNode(Integer.MAX_VALUE), null);
        assertTrue(TreeRecursion.hasPathSum(t, 2L * Integer.MAX_VALUE));
        assertFalse(TreeRecursion.hasPathSum(t, -2L));
    }

    static int heightBrute(TreeNode n) { return n == null ? 0 : 1 + Math.max(heightBrute(n.left), heightBrute(n.right)); }

    static int diameterBrute(TreeNode n) {
        if (n == null) return 0;
        return Math.max(heightBrute(n.left) + heightBrute(n.right), Math.max(diameterBrute(n.left), diameterBrute(n.right)));
    }

    static boolean balancedBrute(TreeNode n) {
        return n == null || (Math.abs(heightBrute(n.left) - heightBrute(n.right)) <= 1 && balancedBrute(n.left) && balancedBrute(n.right));
    }

    static TreeNode random(Random rnd, int size) {
        if (size == 0) return null;
        int l = rnd.nextInt(size);
        return new TreeNode(rnd.nextInt(11) - 5, random(rnd, l), random(rnd, size - 1 - l));
    }

    @Test void randomVsBruteForce() {
        Random rnd = new Random(3);
        for (int t = 0; t < 400; t++) {
            TreeNode root = random(rnd, rnd.nextInt(25));
            assertEquals(diameterBrute(root), TreeRecursion.diameter(root));
            assertEquals(balancedBrute(root), TreeRecursion.isBalanced(root));
        }
    }

    @Test void deepSkewedTreeWithinTimeout() {
        TreeNode root = new TreeNode(1);
        TreeNode cur = root;
        for (int i = 0; i < 5000; i++) { cur.left = new TreeNode(1); cur = cur.left; }
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(5000, TreeRecursion.diameter(root));
            assertFalse(TreeRecursion.isBalanced(root));
            assertTrue(TreeRecursion.hasPathSum(root, 5001));
        });
    }
}
