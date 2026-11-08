package practice.d20261108.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class TrieTest {
    @Test void basics() {
        Trie t = new Trie();
        assertFalse(t.startsWith(""));
        t.insert("apple");
        assertTrue(t.search("apple"));
        assertFalse(t.search("app"));
        assertTrue(t.startsWith("app"));
        assertFalse(t.startsWith("apples"));
        t.insert("app");
        assertTrue(t.search("app"));
        assertEquals(2, t.countWithPrefix("app"));
        assertEquals(2, t.countWithPrefix(""));
        assertEquals(0, t.countWithPrefix("b"));
    }

    @Test void duplicatesAndErase() {
        Trie t = new Trie();
        t.insert("a"); t.insert("a"); t.insert("ab");
        assertEquals(3, t.countWithPrefix("a"));
        assertTrue(t.erase("a"));
        assertTrue(t.search("a"));
        assertEquals(2, t.countWithPrefix("a"));
        assertTrue(t.erase("a"));
        assertFalse(t.search("a"));
        assertTrue(t.search("ab"));
        assertTrue(t.startsWith("a"));
        assertFalse(t.erase("a"));
        assertFalse(t.erase("zzz"));
        assertTrue(t.erase("ab"));
        assertFalse(t.startsWith("a"));
        assertFalse(t.startsWith(""));
    }

    @Test void emptyWord() {
        Trie t = new Trie();
        assertFalse(t.search(""));
        t.insert("");
        assertTrue(t.search(""));
        assertTrue(t.startsWith(""));
        assertEquals(1, t.countWithPrefix(""));
        assertTrue(t.erase(""));
        assertFalse(t.search(""));
    }

    @Test void randomVsMultiset() {
        Random r = new Random(31);
        Trie t = new Trie();
        Map<String, Integer> ref = new HashMap<>();
        for (int i = 0; i < 20_000; i++) {
            String w = word(r);
            int op = r.nextInt(4);
            if (op < 2) { t.insert(w); ref.merge(w, 1, Integer::sum); }
            else if (op == 2) {
                boolean had = ref.getOrDefault(w, 0) > 0;
                assertEquals(had, t.erase(w));
                if (had) ref.merge(w, -1, Integer::sum);
            } else {
                assertEquals(ref.getOrDefault(w, 0) > 0, t.search(w));
                int cnt = ref.entrySet().stream().filter(e -> e.getKey().startsWith(w)).mapToInt(Map.Entry::getValue).sum();
                assertEquals(cnt, t.countWithPrefix(w));
                assertEquals(cnt > 0, t.startsWith(w));
            }
        }
    }

    @Test void replaceWordsExamples() {
        assertEquals("the cat was rat by the bat",
                TrieProblems.replaceWords(List.of("cat", "bat", "rat"), "the cattle was rattled by the battery"));
        assertEquals("a a b c", TrieProblems.replaceWords(List.of("a", "b", "c"), "aadsfasf absbs bbab cadsfafs"));
        assertEquals("hello", TrieProblems.replaceWords(List.of(), "hello"));
        assertEquals("ab", TrieProblems.replaceWords(List.of("abc", "ab"), "abcd")); // shortest root wins, not first listed
        assertEquals("x", TrieProblems.replaceWords(List.of("x"), "x"));
    }

    @Test void longestWordExamples() {
        assertEquals("world", TrieProblems.longestBuildableWord(new String[]{"w", "wo", "wor", "worl", "world"}));
        assertEquals("apple", TrieProblems.longestBuildableWord(new String[]{"a", "banana", "app", "appl", "ap", "apply", "apple"}));
        assertEquals("", TrieProblems.longestBuildableWord(new String[]{"abc", "bc"}));
        assertEquals("a", TrieProblems.longestBuildableWord(new String[]{"a"}));
        assertEquals("", TrieProblems.longestBuildableWord(new String[]{}));
        assertEquals("ab", TrieProblems.longestBuildableWord(new String[]{"b", "a", "bc", "ab"})); // tie ab < bc
    }

    @Test void randomWordsVsBrute() {
        Random r = new Random(32);
        for (int t = 0; t < 2000; t++) {
            String[] words = new String[r.nextInt(10)];
            for (int i = 0; i < words.length; i++) words[i] = word(r, 4);
            Set<String> set = new HashSet<>(Arrays.asList(words));
            String best = "";
            for (String w : set) {
                boolean ok = !w.isEmpty();
                for (int l = 1; l <= w.length() && ok; l++) ok = set.contains(w.substring(0, l));
                if (ok && (w.length() > best.length() || (w.length() == best.length() && w.compareTo(best) < 0))) best = w;
            }
            assertEquals(best, TrieProblems.longestBuildableWord(words), Arrays.toString(words));

            List<String> roots = new ArrayList<>();
            for (int i = 0; i < r.nextInt(5); i++) roots.add(word(r, 3));
            roots.removeIf(String::isEmpty);
            StringJoiner in = new StringJoiner(" "), exp = new StringJoiner(" ");
            for (int i = 0; i < 1 + r.nextInt(5); i++) {
                String w = word(r, 5);
                if (w.isEmpty()) w = "a";
                in.add(w);
                String rep = w;
                for (String root : roots) if (w.startsWith(root) && root.length() < rep.length()) rep = root;
                exp.add(rep);
            }
            assertEquals(exp.toString(), TrieProblems.replaceWords(roots, in.toString()));
        }
    }

    @Test void large() {
        Random r = new Random(33);
        Trie t = new Trie();
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            for (int i = 0; i < 200_000; i++) t.insert(word(r, 12));
            for (int i = 0; i < 200_000; i++) t.countWithPrefix(word(r, 6));
        });
        String[] chain = new String[5_000];
        for (int i = 0; i < chain.length; i++) chain[i] = "a".repeat(i + 1);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertEquals(5_000, TrieProblems.longestBuildableWord(chain).length()));
    }

    private static String word(Random r) { return word(r, 4); }
    private static String word(Random r, int max) {
        int len = r.nextInt(max + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) sb.append((char) ('a' + r.nextInt(3)));
        return sb.toString();
    }
}
