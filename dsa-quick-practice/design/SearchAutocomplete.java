package design;

import java.util.List;

// Search Autocomplete (top 3)  [Hard | Trie + counts]
// record(sentence, times) adds hits. suggest(prefix): up to 3 sentences, higher count first, ties alphabetical.
//
// record("i love you",5), record("island",3), record("ironman",2), record("i love leetcode",2)
// suggest("i")    -> [i love you, island, i love leetcode]
// suggest("i l")  -> [i love you, i love leetcode]

public class SearchAutocomplete {

    public SearchAutocomplete() {
        // TODO
    }

    void record(String sentence, int times) {
        // TODO
    }

    List<String> suggest(String prefix) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        SearchAutocomplete a = new SearchAutocomplete();
        a.record("i love you", 5);
        a.record("island", 3);
        a.record("ironman", 2);
        a.record("i love leetcode", 2);
        System.out.println(a.suggest("i"));   // [i love you, island, i love leetcode]
        System.out.println(a.suggest("i l")); // [i love you, i love leetcode]
    }
}
