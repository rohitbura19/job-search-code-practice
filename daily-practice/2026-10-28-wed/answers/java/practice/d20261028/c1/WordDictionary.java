package practice.d20261028.c1;

/**
 * Array-based trie. search with '.' branches over all 26 children (DFS), so worst case O(26^dots * len) but only existing
 * children are followed, bounded by the trie size. addWord/startsWith O(len). Space O(total letters * 26) worst case.
 */
public class WordDictionary {
    private static final class Node {
        final Node[] next = new Node[26];
        boolean end;
    }

    private final Node root = new Node();
    private boolean any;

    public void addWord(String word) {
        Node n = root;
        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i) - 'a';
            if (n.next[c] == null) n.next[c] = new Node();
            n = n.next[c];
        }
        n.end = true;
        any = true;
    }

    public boolean search(String pattern) { return match(root, pattern, 0); }

    private boolean match(Node n, String p, int i) {
        if (i == p.length()) return n.end;
        char ch = p.charAt(i);
        if (ch != '.') {
            Node child = n.next[ch - 'a'];
            return child != null && match(child, p, i + 1);
        }
        for (Node child : n.next) if (child != null && match(child, p, i + 1)) return true;
        return false;
    }

    public boolean startsWith(String prefix) {
        Node n = root;
        for (int i = 0; i < prefix.length(); i++) {
            n = n.next[prefix.charAt(i) - 'a'];
            if (n == null) return false;
        }
        return any;
    }
}
