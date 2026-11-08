package practice.d20261108.c2;

/**
 * Array-of-26 children per node; each node keeps pass (how many words go through it) and end (how many words stop here),
 * so countWithPrefix is a walk and erase is a decrement. Nodes are not pruned on erase (simple, memory O(total chars));
 * pruning when pass hits 0 is the follow-up. All operations O(len).
 */
public class Trie {
    static final class Node { final Node[] next = new Node[26]; int pass, end; }

    final Node root = new Node();

    public void insert(String word) {
        Node n = root;
        n.pass++;
        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i) - 'a';
            if (n.next[c] == null) n.next[c] = new Node();
            n = n.next[c];
            n.pass++;
        }
        n.end++;
    }

    public boolean search(String word) { Node n = find(word); return n != null && n.end > 0; }
    public boolean startsWith(String prefix) { Node n = find(prefix); return n != null && n.pass > 0; }
    public int countWithPrefix(String prefix) { Node n = find(prefix); return n == null ? 0 : n.pass; }

    public boolean erase(String word) {
        Node last = find(word);
        if (last == null || last.end == 0) return false;
        Node n = root;
        n.pass--;
        for (int i = 0; i < word.length(); i++) { n = n.next[word.charAt(i) - 'a']; n.pass--; }
        n.end--;
        return true;
    }

    Node find(String s) {
        Node n = root;
        for (int i = 0; i < s.length() && n != null; i++) n = n.next[s.charAt(i) - 'a'];
        return n;
    }
}
