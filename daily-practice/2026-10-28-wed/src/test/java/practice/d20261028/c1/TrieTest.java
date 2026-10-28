package practice.d20261028.c1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class TrieTest {
    @Test void wordDictionaryExamples() {
        WordDictionary d = new WordDictionary();
        assertFalse(d.startsWith(""));
        assertFalse(d.search("a"));
        d.addWord("bad"); d.addWord("dad"); d.addWord("mad");
        assertTrue(d.search("bad"));
        assertTrue(d.search(".ad"));
        assertTrue(d.search("b.."));
        assertTrue(d.search("..."));
        assertFalse(d.search("pad"));
        assertFalse(d.search("ba"));
        assertFalse(d.search("bade"));
        assertFalse(d.search("...."));
        assertTrue(d.startsWith("ba"));
        assertTrue(d.startsWith(""));
        assertFalse(d.startsWith("bx"));
        assertFalse(d.startsWith("badd"));
    }

    @Test void wordDictionaryDuplicatesAndSingles() {
        WordDictionary d = new WordDictionary();
        d.addWord("a"); d.addWord("a"); d.addWord("ab");
        assertTrue(d.search("."));
        assertTrue(d.search(".."));
        assertFalse(d.search("..."));
        assertTrue(d.search("a"));
    }

    @Test void wordDictionaryMatchesBruteForce() {
        Random r = new Random(28);
        WordDictionary d = new WordDictionary();
        List<String> words = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            StringBuilder sb = new StringBuilder();
            int len = 1 + r.nextInt(5);
            for (int k = 0; k < len; k++) sb.append((char) ('a' + r.nextInt(3)));
            words.add(sb.toString());
            d.addWord(sb.toString());
        }
        for (int q = 0; q < 2000; q++) {
            StringBuilder sb = new StringBuilder();
            int len = 1 + r.nextInt(5);
            for (int k = 0; k < len; k++) sb.append(r.nextInt(3) == 0 ? '.' : (char) ('a' + r.nextInt(3)));
            String pat = sb.toString();
            boolean want = words.stream().anyMatch(w -> w.matches(pat));
            assertEquals(want, d.search(pat), pat);
        }
    }

    @Test void wordDictionaryLargeIsFast() {
        WordDictionary d = new WordDictionary();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            Random r = new Random(3);
            for (int i = 0; i < 100_000; i++) {
                char[] w = new char[5 + r.nextInt(6)];
                for (int k = 0; k < w.length; k++) w[k] = (char) ('a' + r.nextInt(26));
                d.addWord(new String(w));
            }
            d.addWord("hello");
            assertTrue(d.search("....."));
            assertTrue(d.search("h...o"));
            assertFalse(d.search("zzzzzzzzzzzzzzzzzzzz"));
        });
    }

    @Test void autocompleteExamples() {
        Autocomplete a = new Autocomplete();
        a.record("i love you", 5);
        a.record("island", 3);
        a.record("i love leetcode", 2);
        a.record("ironman", 2);
        a.record("iron man", 2);
        assertEquals(List.of("i love you", "island", "i love leetcode"), a.suggest("i"));
        assertEquals(List.of("i love you", "i love leetcode"), a.suggest("i l"));
        assertEquals(List.of("iron man", "ironman"), a.suggest("iron"));
        assertEquals(List.of(), a.suggest("x"));
        a.record("ironman", 10);
        assertEquals(List.of("ironman", "i love you", "island"), a.suggest("i"));
        assertEquals(List.of("island"), a.suggest("isl"));
        assertEquals(List.of("island"), a.suggest("island"));
        assertEquals(List.of(), a.suggest("islands"));
    }

    @Test void autocompleteTiesAndEmptyTrie() {
        Autocomplete a = new Autocomplete();
        assertEquals(List.of(), a.suggest(""));
        for (String s : List.of("d", "c", "b", "a", "e")) a.record(s, 1);
        assertEquals(List.of("a", "b", "c"), a.suggest(""));
        a.record("e", 1);
        assertEquals(List.of("e", "a", "b"), a.suggest(""));
    }

    @Test void autocompleteLarge() {
        Autocomplete a = new Autocomplete();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 50_000; i++) a.record("q" + (i % 5000) + " term" + i, 1 + i % 7);
            for (int i = 0; i < 2000; i++) assertTrue(a.suggest("q" + i).size() <= 3);
            assertEquals(3, a.suggest("q1").size());
        });
    }
}
