package practice.d20261029.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class AnagramsTest {
    @Test void isAnagramExamples() {
        assertTrue(Anagrams.isAnagram("listen", "silent"));
        assertFalse(Anagrams.isAnagram("aab", "abb"));
        assertFalse(Anagrams.isAnagram("a", "ab"));
        assertTrue(Anagrams.isAnagram("", ""));
        assertFalse(Anagrams.isAnagram("Ab", "ab"));
        assertTrue(Anagrams.isAnagram("héllo世", "世olléh"));
    }

    @Test void groupExamples() {
        assertEquals(List.of(List.of("eat", "tea", "ate"), List.of("tan", "nat"), List.of("bat")),
                Anagrams.groupAnagrams(List.of("eat", "tea", "tan", "ate", "nat", "bat")));
        assertEquals(List.of(List.of("a", "a"), List.of("b")), Anagrams.groupAnagrams(List.of("a", "b", "a")));
        assertEquals(List.of(List.of("")), Anagrams.groupAnagrams(List.of("")));
        assertEquals(List.of(), Anagrams.groupAnagrams(List.of()));
    }

    @Test void findExamples() {
        assertEquals(List.of(0, 6), Anagrams.findAnagrams("cbaebabacd", "abc"));
        assertEquals(List.of(0, 1, 2), Anagrams.findAnagrams("abab", "ab"));
        assertEquals(List.of(), Anagrams.findAnagrams("a", "ab"));
        assertEquals(List.of(), Anagrams.findAnagrams("abc", ""));
        assertEquals(List.of(0), Anagrams.findAnagrams("ab", "ba"));
        assertEquals(List.of(0, 1, 2, 3), Anagrams.findAnagrams("aaaaa", "aa"));
    }

    @Test void randomFindAgainstBruteForce() {
        Random rnd = new Random(29);
        for (int round = 0; round < 3000; round++) {
            String s = rnd.ints(rnd.nextInt(14), 'a', 'd').collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            String p = rnd.ints(rnd.nextInt(4), 'a', 'd').collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            List<Integer> expected = new ArrayList<>();
            if (!p.isEmpty()) for (int i = 0; i + p.length() <= s.length(); i++) if (sorted(s.substring(i, i + p.length())).equals(sorted(p))) expected.add(i);
            assertEquals(expected, Anagrams.findAnagrams(s, p), s + " / " + p);
        }
    }

    @Test void randomGroupingIsConsistent() {
        Random rnd = new Random(7);
        for (int round = 0; round < 500; round++) {
            List<String> words = new ArrayList<>();
            for (int i = rnd.nextInt(10); i > 0; i--) words.add(rnd.ints(rnd.nextInt(4), 'a', 'd').collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString());
            List<List<String>> groups = Anagrams.groupAnagrams(words);
            assertEquals(words.size(), groups.stream().mapToInt(List::size).sum());
            for (List<String> g : groups) for (String w : g) assertTrue(Anagrams.isAnagram(g.get(0), w));
            for (int i = 0; i < groups.size(); i++) for (int j = i + 1; j < groups.size(); j++) assertFalse(Anagrams.isAnagram(groups.get(i).get(0), groups.get(j).get(0)));
        }
    }

    static String sorted(String s) { char[] c = s.toCharArray(); Arrays.sort(c); return new String(c); }

    @Test void largeInputIsFast() {
        String s = "ab".repeat(500_000);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(1_000_000 - 3, Anagrams.findAnagrams(s, "abab").size());
            assertTrue(Anagrams.isAnagram(s, new StringBuilder(s).reverse().toString()));
        });
    }
}
