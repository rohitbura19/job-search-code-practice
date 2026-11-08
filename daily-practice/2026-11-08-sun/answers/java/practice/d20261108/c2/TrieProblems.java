package practice.d20261108.c2;

import java.util.*;

/**
 * replaceWords: insert roots, then walk each word until a node marks a root end (shortest by construction).
 * longestBuildableWord: sort by (length, lexicographic); a word is buildable iff its prefix of length-1 is buildable
 * (or it has length 1); keep the best. O(total chars) apart from the sort.
 */
public final class TrieProblems {
    private TrieProblems() {}

    public static String replaceWords(List<String> roots, String sentence) {
        Trie t = new Trie();
        for (String r : roots) t.insert(r);
        StringBuilder out = new StringBuilder();
        for (String w : sentence.split(" ")) {
            if (out.length() > 0) out.append(' ');
            Trie.Node n = t.root;
            int cut = -1;
            for (int i = 0; i < w.length() && n != null; i++) {
                n = n.next[w.charAt(i) - 'a'];
                if (n != null && n.end > 0) { cut = i + 1; break; }
            }
            out.append(cut < 0 ? w : w.substring(0, cut));
        }
        return out.toString();
    }

    public static String longestBuildableWord(String[] words) {
        String[] sorted = words.clone();
        Arrays.sort(sorted, Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        Set<String> ok = new HashSet<>();
        String best = "";
        for (String w : sorted) {
            if (w.isEmpty()) continue;
            if (w.length() == 1 || ok.contains(w.substring(0, w.length() - 1))) {
                ok.add(w);
                if (w.length() > best.length()) best = w;
            }
        }
        return best;
    }
}
