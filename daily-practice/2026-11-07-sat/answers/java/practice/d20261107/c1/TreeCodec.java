package practice.d20261107.c1;

import java.util.*;

/**
 * buildTree: O(n) time/space. preorder[0] is the root; its position in inorder splits left/right sizes. Iterative variant:
 * walk preorder, push nodes on a stack; a pointer into inorder marks "the next node to be finished". When the stack top equals
 * inorder[ip], the left spine is complete: pop until it no longer matches and attach the next preorder node as a right child.
 * serialize/deserialize: BFS with a queue, O(n) time and space, no recursion.
 */
public final class TreeCodec {
    private TreeCodec() {}

    public static TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length == 0) return null;
        TreeNode root = new TreeNode(preorder[0]);
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        int ip = 0;
        for (int i = 1; i < preorder.length; i++) {
            TreeNode node = new TreeNode(preorder[i]);
            TreeNode top = stack.peek();
            if (top.val != inorder[ip]) {
                top.left = node;
            } else {
                while (!stack.isEmpty() && stack.peek().val == inorder[ip]) { top = stack.pop(); ip++; }
                top.right = node;
            }
            stack.push(node);
        }
        return root;
    }

    public static String serialize(TreeNode root) {
        if (root == null) return "";
        List<String> out = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>(); // ArrayDeque rejects null elements
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode n = q.poll();
            if (n == null) { out.add("#"); continue; }
            out.add(Integer.toString(n.val));
            q.add(n.left);
            q.add(n.right);
        }
        int end = out.size();
        while (end > 0 && out.get(end - 1).equals("#")) end--;
        return String.join(",", out.subList(0, end));
    }

    public static TreeNode deserialize(String data) {
        if (data.isEmpty()) return null;
        String[] t = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(t[0]));
        Deque<TreeNode> q = new ArrayDeque<>();
        q.add(root);
        int i = 1;
        while (!q.isEmpty() && i < t.length) {
            TreeNode n = q.poll();
            if (i < t.length && !t[i].equals("#")) { n.left = new TreeNode(Integer.parseInt(t[i])); q.add(n.left); }
            i++;
            if (i < t.length && !t[i].equals("#")) { n.right = new TreeNode(Integer.parseInt(t[i])); q.add(n.right); }
            i++;
        }
        return root;
    }
}
