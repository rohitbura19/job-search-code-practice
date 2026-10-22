package practice.d20261011.h1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * GIVEN CODE - do not edit. Each method runs one puzzle about ConcurrentHashMap (see questions/H1-concurrenthashmap.md)
 * and returns what it "printed". Read the code, predict the output, and write your prediction into {@link H1Predictions}.
 */
public final class H1Snippets {
    private H1Snippets() {}

    /** Which exception does a null KEY give? Returns its simple class name. */
    public static String line1() {
        try { new ConcurrentHashMap<String, Integer>().put(null, 1); return "no exception"; }
        catch (Exception e) { return e.getClass().getSimpleName(); }
    }

    /** Which exception does a null VALUE give? And what does HashMap do with the same call? */
    public static String line2() {
        StringBuilder out = new StringBuilder();
        try { new ConcurrentHashMap<String, Integer>().put("k", null); out.append("no exception"); }
        catch (Exception e) { out.append(e.getClass().getSimpleName()); }
        Map<String, Integer> hm = new HashMap<>();
        hm.put(null, null);
        out.append(' ').append(hm);
        return out.toString();
    }

    /** merge as an atomic counter. */
    public static String line3() {
        Map<String, Integer> hits = new ConcurrentHashMap<>();
        StringBuilder out = new StringBuilder();
        out.append(hits.merge("k", 1, Integer::sum)).append(' ');
        out.append(hits.merge("k", 1, Integer::sum)).append(' ');
        out.append(hits.merge("k", 1, Integer::sum));
        return out.toString();
    }

    /** What does merge do when the remapping function returns null? */
    public static String line4() {
        Map<String, Integer> m = new ConcurrentHashMap<>();
        m.put("k", 1);
        Integer r = m.merge("k", 5, (oldV, newV) -> null);
        return r + " " + m.containsKey("k") + " " + m.size();
    }

    /** computeIfAbsent: what does it return for a present and for an absent key, and does it run the function for the present one? */
    public static String line5() {
        Map<String, Integer> m = new ConcurrentHashMap<>();
        m.put("a", 1);
        StringBuilder out = new StringBuilder();
        out.append(m.computeIfAbsent("a", k -> { out.append("[fn a] "); return 99; })).append(' ');
        out.append(m.computeIfAbsent("b", k -> { out.append("[fn b] "); return 2; })).append(' ');
        out.append(new java.util.TreeMap<>(m));
        return out.toString();
    }

    /** Structural change while iterating: HashMap versus ConcurrentHashMap. */
    public static String line6() {
        StringBuilder out = new StringBuilder();
        Map<String, Integer> hm = new HashMap<>();
        for (String k : new String[]{"a", "b", "c"}) hm.put(k, 1);
        try { for (String k : hm.keySet()) hm.put(k + "x", 1); out.append("HashMap ok"); }
        catch (Exception e) { out.append(e.getClass().getSimpleName()); }
        out.append(" | ");
        Map<String, Integer> chm = new ConcurrentHashMap<>();
        for (String k : new String[]{"a", "b", "c"}) chm.put(k, 1);
        try { for (String k : chm.keySet()) chm.remove(k); out.append("size ").append(chm.size()); }
        catch (Exception e) { out.append(e.getClass().getSimpleName()); }
        return out.toString();
    }

    /**
     * The check-then-act race, replayed step by step in ONE thread in the unlucky interleaving.
     * (Both "threads" run: Integer c = hits.get(key); hits.put(key, c == null ? 1 : c + 1);)
     */
    public static String line7() {
        Map<String, Integer> hits = new ConcurrentHashMap<>();
        hits.put("k", 5);
        Integer c1 = hits.get("k");          // thread 1 reads
        Integer c2 = hits.get("k");          // thread 2 reads
        hits.put("k", c1 + 1);               // thread 1 writes
        hits.put("k", c2 + 1);               // thread 2 writes
        return String.valueOf(hits.get("k"));
    }

    /** 8 real threads x 10_000 merge() calls on one key. */
    public static String line8() throws InterruptedException {
        Map<String, Integer> hits = new ConcurrentHashMap<>();
        Thread[] ts = new Thread[8];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(() -> { for (int j = 0; j < 10_000; j++) hits.merge("k", 1, Integer::sum); });
            ts[i].start();
        }
        for (Thread t : ts) t.join();
        return String.valueOf(hits.get("k"));
    }

    /** 8 real threads x 10_000 LongAdder increments through computeIfAbsent. */
    public static String line9() throws InterruptedException {
        Map<String, LongAdder> hits = new ConcurrentHashMap<>();
        Thread[] ts = new Thread[8];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(() -> { for (int j = 0; j < 10_000; j++) hits.computeIfAbsent("k", x -> new LongAdder()).increment(); });
            ts[i].start();
        }
        for (Thread t : ts) t.join();
        return hits.get("k").sum() + " " + hits.size();
    }
}
