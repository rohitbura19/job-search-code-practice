package design;

// Design Add and Search Words  [Medium | Trie + DFS for '.']
// '.' matches any one letter. Whole word must match.
//
// add bad, dad, mad: search(pad)=false, search(bad)=true, search(.ad)=true, search(b..)=true

public class WordDictionary {

    public WordDictionary() {
        // TODO
    }

    void addWord(String word) {
        // TODO
    }

    boolean search(String pattern) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        WordDictionary d = new WordDictionary();
        d.addWord("bad");
        d.addWord("dad");
        d.addWord("mad");
        System.out.println(d.search("pad")); // false
        System.out.println(d.search("bad")); // true
        System.out.println(d.search(".ad")); // true
        System.out.println(d.search("b..")); // true
    }
}
