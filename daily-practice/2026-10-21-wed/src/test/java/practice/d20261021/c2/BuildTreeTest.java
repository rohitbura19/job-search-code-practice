package practice.d20261021.c2;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BuildTreeTest {

    // "(left value right)" with # for null: two trees are equal iff their strings are equal
    static String ser(TreeNode n) { return n == null ? "#" : "(" + ser(n.left) + " " + n.val + " " + ser(n.right) + ")"; }
    static void pre(TreeNode n, List<Integer> o) { if (n == null) return; o.add(n.val); pre(n.left, o); pre(n.right, o); }
    static void in(TreeNode n, List<Integer> o) { if (n == null) return; in(n.left, o); o.add(n.val); in(n.right, o); }
    static void post(TreeNode n, List<Integer> o) { if (n == null) return; post(n.left, o); post(n.right, o); o.add(n.val); }
    static int[] arr(List<Integer> l) { return l.stream().mapToInt(Integer::intValue).toArray(); }
    static TreeNode random(Random rnd, List<Integer> vals) {
        if (vals.isEmpty()) return null;
        int k = rnd.nextInt(vals.size()); TreeNode n = new TreeNode(vals.get(k));
        n.left = random(rnd, vals.subList(0, k)); n.right = random(rnd, vals.subList(k + 1, vals.size()));
        return n;
    }

    @Test @DisplayName("example: preorder + inorder")
    void example() {
        TreeNode t = BuildTree.fromPreIn(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        assertEquals("((# 9 #) 3 ((# 15 #) 20 (# 7 #)))", ser(t));
    }

    @Test @DisplayName("example: postorder + inorder gives the same tree")
    void examplePost() {
        TreeNode t = BuildTree.fromPostIn(new int[]{9, 15, 7, 20, 3}, new int[]{9, 3, 15, 20, 7});
        assertEquals("((# 9 #) 3 ((# 15 #) 20 (# 7 #)))", ser(t));
    }

    @Test @DisplayName("empty input gives null, one value gives one node")
    void tiny() {
        assertNull(BuildTree.fromPreIn(new int[]{}, new int[]{}));
        assertNull(BuildTree.fromPostIn(new int[]{}, new int[]{}));
        assertEquals("(# -1 #)", ser(BuildTree.fromPreIn(new int[]{-1}, new int[]{-1})));
    }

    @Test @DisplayName("left chain and right chain")
    void chains() {
        assertEquals("(((# 3 #) 2 #) 1 #)", ser(BuildTree.fromPreIn(new int[]{1, 2, 3}, new int[]{3, 2, 1})));
        assertEquals("(# 1 (# 2 (# 3 #)))", ser(BuildTree.fromPreIn(new int[]{1, 2, 3}, new int[]{1, 2, 3})));
        assertEquals("(# 1 (# 2 (# 3 #)))", ser(BuildTree.fromPostIn(new int[]{3, 2, 1}, new int[]{1, 2, 3})));
    }

    @Test @DisplayName("2,000 random trees round-trip through both traversal pairs")
    void randomRoundTrip() {
        Random rnd = new Random(3);
        for (int t = 0; t < 2000; t++) {
            List<Integer> vals = new ArrayList<>();
            for (int i = 0, n = rnd.nextInt(12); i < n; i++) vals.add(i * 7 - 20);
            TreeNode tree = random(rnd, vals);
            List<Integer> p = new ArrayList<>(), i = new ArrayList<>(), q = new ArrayList<>();
            pre(tree, p); in(tree, i); post(tree, q);
            assertEquals(ser(tree), ser(BuildTree.fromPreIn(arr(p), arr(i))));
            assertEquals(ser(tree), ser(BuildTree.fromPostIn(arr(q), arr(i))));
        }
    }

    @Disabled("bonus: remove @Disabled after writing an ITERATIVE version (a recursive one overflows the stack here)")
    @Test @DisplayName("bonus: a left-skewed chain of 200,000 nodes")
    void deepChain() {
        int n = 200_000; int[] p = new int[n], i = new int[n];
        for (int k = 0; k < n; k++) { p[k] = k; i[k] = n - 1 - k; }
        TreeNode t = BuildTree.fromPreIn(p, i);
        int depth = 0; for (TreeNode c = t; c != null; c = c.left) depth++;
        assertEquals(n, depth);
    }
}
