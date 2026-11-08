package practice.d20261108.c2;

/** Prefix tree over lowercase a-z words. Words are never null; the empty word is allowed. */
public class Trie {
    /** Adds the word (duplicates allowed and counted). */
    public void insert(String word) { throw new UnsupportedOperationException("TODO"); }

    /** True if the exact word was inserted at least once. */
    public boolean search(String word) { throw new UnsupportedOperationException("TODO"); }

    /** True if some inserted word starts with the prefix (the empty prefix matches iff anything was inserted). */
    public boolean startsWith(String prefix) { throw new UnsupportedOperationException("TODO"); }

    /** Number of insert() calls (duplicates included) whose word starts with the prefix. */
    public int countWithPrefix(String prefix) { throw new UnsupportedOperationException("TODO"); }

    /** Removes one occurrence of the word. Returns false if it was not present. Must not break other words. */
    public boolean erase(String word) { throw new UnsupportedOperationException("TODO"); }
}
