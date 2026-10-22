package practice.d20261013.a1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DiameterOfBinaryTreeTest {

    /** Level-order builder, LeetCode style: null = no node. */
    static TreeNode build(Integer... v) {
        if (v.length == 0 || v[0] == null) return null;
        TreeNode root = new TreeNode(v[0]);
        Queue<TreeNode> q = new ArrayDeque<>(); q.add(root);
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

    static void both(int expected, TreeNode root) {
        assertEquals(expected, DiameterOfBinaryTree.diameterOfBinaryTree(root), "recursive");
        assertEquals(expected, DiameterOfBinaryTree.diameterIterative(root), "iterative");
    }

    @Test @DisplayName("sheet example: 4-2-1-3 has 3 edges")
    void example() { both(3, build(1, 2, 3, 4, 5)); }

    @Test @DisplayName("empty tree and single node -> 0")
    void tiny() { both(0, null); both(0, build(7)); }

    @Test @DisplayName("two nodes -> 1")
    void two() { both(1, build(1, 2)); }

    @Test @DisplayName("the longest path does NOT pass through the root")
    void notThroughRoot() {
        // root has a short right side; the long bend is inside the left subtree: 6-4-2-5-7 (4 edges)
        TreeNode left = build(2, 4, 5, 6, null, null, 7);
        TreeNode root = new TreeNode(1, left, new TreeNode(3));
        both(4, root);
    }

    @Test @DisplayName("a straight chain of 5 nodes -> 4")
    void chain() { both(4, build(1, 2, null, 3, null, 4, null, 5)); }

    static TreeNode random(Random rnd, int n) {
        TreeNode[] nodes = new TreeNode[n];
        nodes[0] = new TreeNode(rnd.nextInt(21) - 10);
        for (int i = 1; i < n; i++) {
            nodes[i] = new TreeNode(rnd.nextInt(21) - 10);
            while (true) {
                TreeNode p = nodes[rnd.nextInt(i)];
                if (rnd.nextBoolean()) { if (p.left == null) { p.left = nodes[i]; break; } }
                else { if (p.right == null) { p.right = nodes[i]; break; } }
            }
        }
        return nodes[0];
    }
    static int h(TreeNode n) { return n == null ? 0 : 1 + Math.max(h(n.left), h(n.right)); }
    static int oracle(TreeNode n) {   // O(n^2): the diameter is the best bend over every node
        if (n == null) return 0;
        return Math.max(h(n.left) + h(n.right), Math.max(oracle(n.left), oracle(n.right)));
    }

    @Test @DisplayName("random trees agree with an O(n^2) oracle")
    void randomTrees() {
        Random rnd = new Random(13);
        for (int t = 0; t < 200; t++) {
            TreeNode root = random(rnd, 1 + rnd.nextInt(40));
            both(oracle(root), root);
        }
    }

    @Test @DisplayName("iterative version survives a skewed tree of 1,000,000 nodes")
    void skewed() {
        TreeNode root = new TreeNode(0), cur = root;
        for (int i = 1; i < 1_000_000; i++) { cur.left = new TreeNode(i); cur = cur.left; }
        TreeNode r = root;
        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> assertEquals(999_999, DiameterOfBinaryTree.diameterIterative(r)));
    }

    // ---------- maxPathSum follow-up ----------

    @Test @DisplayName("maxPathSum: [-10,9,20,null,null,15,7] -> 42")
    void pathSum() { assertEquals(42, DiameterOfBinaryTree.maxPathSum(build(-10, 9, 20, null, null, 15, 7))); }

    @Test @DisplayName("maxPathSum: all negative -> the largest single node; single node")
    void pathSumNegative() {
        assertEquals(-3, DiameterOfBinaryTree.maxPathSum(build(-3)));
        assertEquals(-1, DiameterOfBinaryTree.maxPathSum(build(-5, -1, -2)));
    }

    @Test @DisplayName("maxPathSum: negative child contributions are clamped to 0")
    void pathSumClamp() { assertEquals(6, DiameterOfBinaryTree.maxPathSum(build(1, 2, 3, -100, null, null, null))); }
}
