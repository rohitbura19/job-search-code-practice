package practice.d20261028.c1;

import java.util.List;

/** Search suggestions backed by a trie. Sentences are lowercase letters and spaces. */
public class Autocomplete {
    public Autocomplete() {}

    /** Adds `times` (>= 1) to the hit count of the sentence (creating it if new). */
    public void record(String sentence, int times) { throw new UnsupportedOperationException("TODO"); }

    /** Up to 3 recorded sentences that start with prefix: higher count first, ties alphabetical. Unknown prefix -> empty list. */
    public List<String> suggest(String prefix) { throw new UnsupportedOperationException("TODO"); }
}
