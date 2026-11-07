package practice.d20261107.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class TreeCodecTest {
    private static TreeNode example() {
        return new TreeNode(1, new TreeNode(2), new TreeNode(3, new TreeNode(4), new TreeNode(5)));
    }

    @Test void buildExamples() {
        TreeNode t = TreeCodec.buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        assertEquals("3,9,20,#,#,15,7", TreeCodec.serialize(t));
        assertNull(TreeCodec.buildTree(new int[]{}, new int[]{}));
        assertEquals("-1", TreeCodec.serialize(TreeCodec.buildTree(new int[]{-1}, new int[]{-1})));
    }

    @Test void serializeFormat() {
        assertEquals("1,2,3,#,#,4,5", TreeCodec.serialize(example()));
        assertEquals("", TreeCodec.serialize(null));
        assertEquals("1", TreeCodec.serialize(new TreeNode(1)));
        assertEquals("1,#,2,3", TreeCodec.serialize(new TreeNode(1, null, new TreeNode(2, new TreeNode(3), null))));
    }

    @Test void deserializeFormat() {
        assertTrue(TreeNode.sameTree(example(), TreeCodec.deserialize("1,2,3,#,#,4,5")));
        assertNull(TreeCodec.deserialize(""));
        assertTrue(TreeNode.sameTree(new TreeNode(Integer.MIN_VALUE), TreeCodec.deserialize("-2147483648")));
    }

    @Test void randomRoundTripAndBuild() {
        Random r = new Random(5);
        for (int t = 0; t < 1000; t++) {
            TreeNode root = randomTree(r, r.nextInt(25));
            assertTrue(TreeNode.sameTree(root, TreeCodec.deserialize(TreeCodec.serialize(root))));
            List<Integer> pre = new ArrayList<>(), in = new ArrayList<>();
            pre(root, pre);
            ino(root, in);
            TreeNode rebuilt = TreeCodec.buildTree(pre.stream().mapToInt(i -> i).toArray(), in.stream().mapToInt(i -> i).toArray());
            assertTrue(TreeNode.sameTree(root, rebuilt), pre + " / " + in);
        }
    }

    @Test void deepSkewedTreesDoNotOverflowTheStack() {
        int n = 100_000;
        int[] pre = new int[n], in = new int[n];
        for (int i = 0; i < n; i++) { pre[i] = i; in[i] = n - 1 - i; } // left-leaning chain
        int[] pre2 = new int[n], in2 = new int[n];
        for (int i = 0; i < n; i++) { pre2[i] = i; in2[i] = i; }        // right-leaning chain
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            TreeNode left = TreeCodec.buildTree(pre, in);
            String s = TreeCodec.serialize(left);
            assertEquals(s, TreeCodec.serialize(TreeCodec.deserialize(s)));
            TreeNode right = TreeCodec.buildTree(pre2, in2);
            String s2 = TreeCodec.serialize(right);
            assertEquals(s2, TreeCodec.serialize(TreeCodec.deserialize(s2)));
            assertTrue(s2.startsWith("0,#,1,#,2,#,3"));
        });
    }

    private static int counter;

    private static TreeNode randomTree(Random r, int n) {
        counter = 0;
        List<Integer> vals = new ArrayList<>();
        for (int i = 0; i < n; i++) vals.add(i - 5);
        Collections.shuffle(vals, r);
        return gen(r, n, vals);
    }

    private static TreeNode gen(Random r, int n, List<Integer> vals) {
        if (n == 0) return null;
        int l = r.nextInt(n);
        TreeNode node = new TreeNode(vals.get(counter++));
        node.left = gen(r, l, vals);
        node.right = gen(r, n - 1 - l, vals);
        return node;
    }

    private static void pre(TreeNode n, List<Integer> o) { if (n == null) return; o.add(n.val); pre(n.left, o); pre(n.right, o); }
    private static void ino(TreeNode n, List<Integer> o) { if (n == null) return; ino(n.left, o); o.add(n.val); ino(n.right, o); }
}
