package practice.d20261112.g;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.Test;

class AutocompleteServiceTest {
    private static AutocompleteService svc() { return new AutocompleteService(40); }
    private static void rec(AutocompleteService s, String q, int times) { for (int i = 0; i < times; i++) s.record(q); }

    @Test void ranksByCountThenAlphabet() {
        AutocompleteService s = svc();
        rec(s, "java stream", 5); rec(s, "java spring", 5); rec(s, "java", 7); rec(s, "javascript", 2); rec(s, "python", 9);
        assertEquals(List.of("java", "java spring", "java stream", "javascript"), s.suggest("jav", 10));
        assertEquals(List.of("java", "java spring"), s.suggest("java", 2));
        assertEquals(List.of("python", "java"), s.suggest("", 2));
    }
    @Test void normalisation() {
        AutocompleteService s = svc();
        assertTrue(s.record("  Java   Stream "));
        assertTrue(s.record("java stream"));
        assertEquals(2, s.count("JAVA STREAM"));
        assertEquals(List.of("java stream"), s.suggest("  JAVA S", 5));
        assertEquals(1, s.distinctQueries());
    }
    @Test void invalidInputIsIgnored() {
        AutocompleteService s = new AutocompleteService(5);
        assertFalse(s.record(null));
        assertFalse(s.record("   "));
        assertFalse(s.record("toolong"));
        assertTrue(s.record("exact"));
        assertEquals(1, s.distinctQueries());
        assertEquals(List.of(), s.suggest("exact", 0));
        assertEquals(List.of(), s.suggest("exact", -3));
        assertEquals(List.of(), s.suggest("zzz", 5));
        assertEquals(List.of("exact"), s.suggest(null, 5));
        assertEquals(0, s.count("nothing"));
    }
    @Test void blockedQueriesAreHiddenButCounted() {
        AutocompleteService s = svc();
        rec(s, "bad word", 10); rec(s, "bad day", 1);
        s.block("Bad Word");
        assertEquals(List.of("bad day"), s.suggest("bad", 5));
        assertEquals(10, s.count("bad word"));
        assertEquals(2, s.distinctQueries());
        s.unblock("bad word");
        assertEquals(List.of("bad word", "bad day"), s.suggest("bad", 5));
        s.block("never recorded");
        assertEquals(2, s.distinctQueries());
    }
    @Test void prefixOfRecordedQueryDoesNotCountUnlessRecorded() {
        AutocompleteService s = svc();
        rec(s, "abcd", 3);
        assertEquals(List.of("abcd"), s.suggest("ab", 5));
        assertEquals(0, s.count("ab"));
        assertEquals(List.of("abcd"), s.suggest("abcd", 5));
    }
    @Test void matchesBruteForce() {
        Random r = new Random(21);
        AutocompleteService s = svc();
        Map<String, Long> model = new HashMap<>();
        Set<String> blocked = new HashSet<>();
        String[] alphabet = {"a", "b", "c", " "};
        for (int i = 0; i < 3000; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 1 + r.nextInt(5); j > 0; j--) sb.append(alphabet[r.nextInt(4)]);
            String raw = sb.toString();
            String norm = raw.trim().replaceAll("\\s+", " ");
            int op = r.nextInt(10);
            if (op == 0 && !norm.isEmpty()) { s.block(raw); if (model.containsKey(norm)) blocked.add(norm); }
            else if (op == 1 && !norm.isEmpty()) { s.unblock(raw); blocked.remove(norm); }
            else { s.record(raw); if (!norm.isEmpty()) model.merge(norm, 1L, Long::sum); }
            if (i % 50 == 0) {
                String p = alphabet[r.nextInt(3)] + (r.nextBoolean() ? alphabet[r.nextInt(3)] : "");
                int k = 1 + r.nextInt(6);
                List<String> expected = model.entrySet().stream().filter(e -> e.getKey().startsWith(p) && !blocked.contains(e.getKey()))
                        .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                        .limit(k).map(Map.Entry::getKey).toList();
                assertEquals(expected, s.suggest(p, k), "prefix=" + p + " k=" + k);
            }
        }
        assertEquals(model.size(), s.distinctQueries());
    }
    @Test void concurrentRecordsAreNotLost() throws Exception {
        AutocompleteService s = svc();
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) {
            int id = t;
            fs.add(pool.submit(() -> { for (int i = 0; i < 2000; i++) { s.record("shared"); s.record("own " + id); s.suggest("s", 3); } }));
        }
        for (Future<?> f : fs) f.get(10, TimeUnit.SECONDS);
        pool.shutdown();
        assertEquals(16000, s.count("shared"));
        assertEquals(2000, s.count("own 3"));
        assertEquals(9, s.distinctQueries());
    }
    @Test void largeVocabularyIsFast() {
        AutocompleteService s = new AutocompleteService(40);
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            for (int i = 0; i < 200_000; i++) s.record("query " + (i % 50_000));
            for (int i = 0; i < 2000; i++) assertEquals(5, s.suggest("query 4", 5).size());
        });
    }
}
