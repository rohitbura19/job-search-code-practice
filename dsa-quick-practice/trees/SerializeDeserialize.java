package trees;

// Serialize and Deserialize Binary Tree  [Hard | Level order with "#" markers]
// Format: values comma separated, "#" for a missing child, trailing "#" removed, empty tree -> "".
//
// tree 1 -> (2, 3), 3 -> (4, 5)  ->  "1,2,3,#,#,4,5"   and back to the same tree

public class SerializeDeserialize {

    static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    static String serialize(TreeNode root) {
        // TODO
        return "";
    }

    static TreeNode deserialize(String data) {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);
        String s = serialize(root);
        System.out.println(s);                         // 1,2,3,#,#,4,5
        System.out.println(serialize(deserialize(s))); // 1,2,3,#,#,4,5
    }
}
