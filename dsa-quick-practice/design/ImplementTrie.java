package design;

// Implement Trie (Prefix Tree)  [Medium | 26-child nodes]
// Bonus: countWithPrefix and erase (remove one occurrence).
//
// insert(apple); search(apple)=true; search(app)=false; startsWith(app)=true; insert(app); search(app)=true

public class ImplementTrie {

    public ImplementTrie() {
        // TODO
    }

    void insert(String word) {
        // TODO
    }

    boolean search(String word) {
        // TODO
        return false;
    }

    boolean startsWith(String prefix) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        ImplementTrie t = new ImplementTrie();
        t.insert("apple");
        System.out.println(t.search("apple"));   // true
        System.out.println(t.search("app"));     // false
        System.out.println(t.startsWith("app")); // true
        t.insert("app");
        System.out.println(t.search("app"));     // true
    }
}
