package practice.d20261031.c1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import org.junit.jupiter.api.Test;

class BstTest {
    /** level-order with nulls, LeetCode style */
    static TreeNode tree(Integer... v) {
        if (v.length == 0 || v[0] == null) return null;
        TreeNode root = new TreeNode(v[0]);
        java.util.ArrayDeque<TreeNode> q = new java.util.ArrayDeque<>();
        q.add(root);
        int i = 1;
        while (i < v.length) {
            TreeNode n = q.poll();
            if (i < v.length && v[i] != null) { n.left = new TreeNode(v[i]); q.add(n.left); }
            i++;
            if (i < v.length && v[i] != null) { n.right = new TreeNode(v[i]); q.add(n.right); }
            i++;
        }
        return root;
    }

    static TreeNode insert(TreeNode root, int x) {
        if (root == null) return new TreeNode(x);
        TreeNode c = root;
        while (true) {
            if (x < c.val) { if (c.left == null) { c.left = new TreeNode(x); break; } c = c.left; }
            else { if (c.right == null) { c.right = new TreeNode(x); break; } c = c.right; }
        }
        return root;
    }

    static void inorder(TreeNode n, List<Integer> out) { if (n == null) return; inorder(n.left, out); out.add(n.val); inorder(n.right, out); }

    static int height(TreeNode n) { return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right)); }

    @Test void validExamples() {
        assertTrue(Bst.isValidBST(null));
        assertTrue(Bst.isValidBST(tree(2, 1, 3)));
        assertFalse(Bst.isValidBST(tree(5, 1, 4, null, null, 3, 6)));
        assertFalse(Bst.isValidBST(tree(5, 4, 6, null, null, 3, 7)));     // 3 is right of 5: children-only check passes, ancestors fail
        assertFalse(Bst.isValidBST(tree(2, 2, 2)));                        // duplicates are invalid (strict)
    }

    @Test void validExtremes() {
        assertTrue(Bst.isValidBST(new TreeNode(Integer.MIN_VALUE)));
        assertTrue(Bst.isValidBST(new TreeNode(0, new TreeNode(Integer.MIN_VALUE), new TreeNode(Integer.MAX_VALUE))));
        assertFalse(Bst.isValidBST(new TreeNode(Integer.MIN_VALUE, new TreeNode(Integer.MIN_VALUE), null)));
        assertFalse(Bst.isValidBST(new TreeNode(Integer.MAX_VALUE, null, new TreeNode(Integer.MAX_VALUE))));
    }

    @Test void kthExamples() {
        TreeNode t = tree(5, 3, 6, 2, 4, null, null, 1);
        assertEquals(1, Bst.kthSmallest(t, 1));
        assertEquals(3, Bst.kthSmallest(t, 3));
        assertEquals(6, Bst.kthSmallest(t, 6));
        assertEquals(7, Bst.kthSmallest(new TreeNode(7), 1));
    }

    @Test void lcaExamples() {
        TreeNode t = tree(6, 2, 8, 0, 4, 7, 9, null, null, 3, 5);
        assertEquals(6, Bst.lowestCommonAncestor(t, 2, 8));
        assertEquals(2, Bst.lowestCommonAncestor(t, 2, 4));
        assertEquals(4, Bst.lowestCommonAncestor(t, 3, 5));
        assertEquals(7, Bst.lowestCommonAncestor(t, 7, 7));
        assertEquals(6, Bst.lowestCommonAncestor(t, 9, 0));
    }

    @Test void sortedToBst() {
        assertNull(Bst.sortedArrayToBST(new int[] {}));
        assertEquals(1, height(Bst.sortedArrayToBST(new int[] {1})));
        for (int n = 1; n <= 40; n++) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = i * 2 - 7;
            TreeNode t = Bst.sortedArrayToBST(a);
            assertTrue(Bst.isValidBST(t));
            List<Integer> in = new ArrayList<>();
            inorder(t, in);
            assertEquals(n, in.size());
            assertTrue(height(t) <= 32 - Integer.numberOfLeadingZeros(n), "not balanced for n=" + n);
        }
    }

    @Test void randomTreesAgainstSortedList() {
        Random r = new Random(6);
        for (int t = 0; t < 200; t++) {
            TreeSet<Integer> vals = new TreeSet<>();
            TreeNode root = null;
            for (int i = 0, n = 1 + r.nextInt(20); i < n; i++) {
                int x = r.nextInt(50);
                if (vals.add(x)) root = insert(root, x);
            }
            assertTrue(Bst.isValidBST(root));
            List<Integer> sorted = new ArrayList<>(vals);
            for (int k = 1; k <= sorted.size(); k++) assertEquals(sorted.get(k - 1), Bst.kthSmallest(root, k));
            int p = sorted.get(r.nextInt(sorted.size())), q = sorted.get(r.nextInt(sorted.size()));
            int lo = Math.min(p, q), hi = Math.max(p, q);
            // LCA in a BST = the first node on the root path whose value lies in [lo, hi]
            int expect = -1;
            for (TreeNode c = root; c != null; c = hi < c.val ? c.left : c.right) if (c.val >= lo && c.val <= hi) { expect = c.val; break; }
            assertEquals(expect, Bst.lowestCommonAncestor(root, p, q));
            // break the BST: swap two different values
            if (sorted.size() > 2 && root.left != null && root.left.val != root.val) {
                int tmp = root.val; root.val = root.left.val; root.left.val = tmp;
                assertFalse(Bst.isValidBST(root));
            }
        }
    }

    @Test void deepSkewedTreeDoesNotOverflowTheStack() {
        TreeNode root = new TreeNode(0), c = root;
        for (int i = 1; i < 100_000; i++) { c.right = new TreeNode(i); c = c.right; }
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertTrue(Bst.isValidBST(root));
            assertEquals(99_999, Bst.kthSmallest(root, 100_000));
            assertEquals(0, Bst.lowestCommonAncestor(root, 0, 99_999));
        });
    }
}
