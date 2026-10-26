package practice.d20261026.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class TreesTest {
    /** Level-order builder: null = missing child (LeetCode style). */
    static TreeNode build(Integer... v) {
        if (v.length == 0 || v[0] == null) return null;
        TreeNode root = new TreeNode(v[0]);
        Queue<TreeNode> q = new LinkedList<>();
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

    static List<String> levels(TreeNode root) {
        List<String> out = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            out.add(n == null ? "#" : String.valueOf(n.val));
            if (n != null) { q.add(n.left); q.add(n.right); }
        }
        return out;
    }

    @Test void invertExample() {
        TreeNode r = Trees.invert(build(4, 2, 7, 1, 3, 6, 9));
        assertEquals(levels(build(4, 7, 2, 9, 6, 3, 1)), levels(r));
    }

    @Test void invertEdges() {
        assertNull(Trees.invert(null));
        TreeNode single = new TreeNode(1);
        assertSame(single, Trees.invert(single));
        TreeNode r = Trees.invert(build(1, 2));
        assertNull(r.left);
        assertEquals(2, r.right.val);
    }

    @Test void invertTwiceIsIdentity() {
        Random rnd = new Random(3);
        for (int t = 0; t < 200; t++) {
            Integer[] vals = new Integer[rnd.nextInt(12)];
            for (int i = 0; i < vals.length; i++) vals[i] = rnd.nextInt(4) == 0 ? null : rnd.nextInt(5);
            if (vals.length > 0 && vals[0] == null) vals[0] = 1;
            List<String> before = levels(build(vals));
            assertEquals(before, levels(Trees.invert(Trees.invert(build(vals)))));
        }
    }

    @Test void symmetricExamples() {
        assertTrue(Trees.isSymmetric(null));
        assertTrue(Trees.isSymmetric(new TreeNode(1)));
        assertTrue(Trees.isSymmetric(build(1, 2, 2, 3, 4, 4, 3)));
        assertFalse(Trees.isSymmetric(build(1, 2, 2, null, 3, null, 3)));
        assertFalse(Trees.isSymmetric(build(1, 2)));
        assertFalse(Trees.isSymmetric(build(1, 2, 3)));
    }

    @Test void symmetricIsInvertInvariant() {
        Random rnd = new Random(4);
        for (int t = 0; t < 300; t++) {
            Integer[] vals = new Integer[1 + rnd.nextInt(10)];
            for (int i = 0; i < vals.length; i++) vals[i] = rnd.nextInt(3) == 0 ? null : rnd.nextInt(2);
            if (vals[0] == null) vals[0] = 0;
            TreeNode a = build(vals);
            TreeNode b = build(vals);
            boolean viaInvert = levels(a).equals(levels(Trees.invert(b)));   // a tree is symmetric iff it equals its mirror
            assertEquals(viaInvert, Trees.isSymmetric(build(vals)));
        }
    }

    @Test void deepChains() {
        int depth = 100_000;
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            TreeNode root = new TreeNode(0), cur = root;
            for (int i = 1; i < depth; i++) { cur.left = new TreeNode(i); cur = cur.left; }
            Trees.invert(root);
            int len = 0;
            for (TreeNode n = root; n != null; n = n.right) len++;
            assertEquals(depth, len);
            assertNull(root.left);

            TreeNode top = new TreeNode(1), l = new TreeNode(2), r = new TreeNode(2);
            top.left = l; top.right = r;
            for (int i = 0; i < depth; i++) { l.left = new TreeNode(3); l = l.left; r.right = new TreeNode(3); r = r.right; }
            assertTrue(Trees.isSymmetric(top));
            r.right = new TreeNode(9);
            assertFalse(Trees.isSymmetric(top));
        });
    }
}
