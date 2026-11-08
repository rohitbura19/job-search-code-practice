package practice.d20261108.d1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class ReconstructItineraryTest {
    private static List<List<String>> tickets(String... pairs) {
        List<List<String>> l = new ArrayList<>();
        for (String p : pairs) l.add(List.of(p.split(">")));
        return l;
    }

    @Test void examples() {
        assertEquals(List.of("JFK", "MUC", "LHR", "SFO", "SJC"),
                ReconstructItinerary.findItinerary(tickets("MUC>LHR", "JFK>MUC", "SFO>SJC", "LHR>SFO")));
        assertEquals(List.of("JFK", "ATL", "JFK", "SFO", "ATL", "SFO"),
                ReconstructItinerary.findItinerary(tickets("JFK>SFO", "JFK>ATL", "SFO>ATL", "ATL>JFK", "ATL>SFO")));
    }

    @Test void edgeCases() {
        assertEquals(List.of("JFK", "A"), ReconstructItinerary.findItinerary(tickets("JFK>A")));
        assertEquals(List.of("JFK", "A", "JFK", "A"), ReconstructItinerary.findItinerary(tickets("JFK>A", "JFK>A", "A>JFK")));
        // greedy smallest first ("A") is a dead end, so the answer must go to B first
        assertEquals(List.of("JFK", "B", "JFK", "A"), ReconstructItinerary.findItinerary(tickets("JFK>A", "JFK>B", "B>JFK")));
        assertEquals(List.of("JFK", "JFK", "JFK"), ReconstructItinerary.findItinerary(tickets("JFK>JFK", "JFK>JFK")));
    }

    @Test void randomVsBacktracking() {
        Random r = new Random(41);
        String[] ap = {"JFK", "A", "B", "C"};
        for (int t = 0; t < 3000; t++) {
            int m = 1 + r.nextInt(8);
            List<List<String>> ts = new ArrayList<>();
            String at = "JFK";
            for (int i = 0; i < m; i++) { String to = ap[r.nextInt(ap.length)]; ts.add(List.of(at, to)); at = to; }
            Collections.shuffle(ts, r);
            List<String> expected = brute(ts);
            assertEquals(expected, ReconstructItinerary.findItinerary(ts), ts.toString());
        }
    }

    private static List<String> brute(List<List<String>> ts) {
        List<List<String>> sorted = new ArrayList<>(ts);
        sorted.sort(Comparator.<List<String>, String>comparing(x -> x.get(0)).thenComparing(x -> x.get(1)));
        List<String> path = new ArrayList<>(List.of("JFK"));
        return dfs(sorted, new boolean[sorted.size()], path) ? path : null;
    }

    private static boolean dfs(List<List<String>> ts, boolean[] used, List<String> path) {
        if (path.size() == ts.size() + 1) return true;
        String cur = path.get(path.size() - 1);
        for (int i = 0; i < ts.size(); i++) {
            if (used[i] || !ts.get(i).get(0).equals(cur)) continue;
            used[i] = true;
            path.add(ts.get(i).get(1));
            if (dfs(ts, used, path)) return true;
            path.remove(path.size() - 1);
            used[i] = false;
        }
        return false;
    }

    @Test void largeChainAndLoops() {
        List<List<String>> chain = new ArrayList<>();
        String prev = "JFK";
        for (int i = 0; i < 300_000; i++) { String next = "N" + i; chain.add(List.of(prev, next)); prev = next; }
        Collections.shuffle(chain, new Random(42));
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> assertEquals(300_001, ReconstructItinerary.findItinerary(chain).size()));

        List<List<String>> loops = new ArrayList<>();
        for (int i = 0; i < 150_000; i++) { loops.add(List.of("JFK", "A")); loops.add(List.of("A", "JFK")); }
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> assertEquals(300_001, ReconstructItinerary.findItinerary(loops).size()));
    }
}
