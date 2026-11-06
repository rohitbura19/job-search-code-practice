package practice.d20261106.h1;

import java.util.*;
import java.util.stream.Stream;

/** The real code. Read it, predict each result in H1Predictions, and let the test compare. Do not run it first. */
public final class H1Snippets {
    private H1Snippets() {}

    static class P {
        String name = "P";
        static String who() { return "Ps"; }
        String me() { return "Pm:" + name; }
        final String seen;
        P() { seen = me(); }
    }

    static class C extends P {
        String name = "C";
        static String who() { return "Cs"; }
        @Override String me() { return "Cm:" + name; }
    }

    /** Field hiding, static hiding, virtual call from a super constructor. */
    public static String s1() {
        P p = new C();
        return p.seen + "|" + p.name + "|" + p.who() + "|" + p.me() + "|" + ((C) p).name;
    }

    static class Res implements AutoCloseable {
        private final String id;
        private final StringBuilder log;
        Res(String id, StringBuilder log) { this.id = id; this.log = log; log.append("open").append(id).append(' '); }
        @Override public void close() { log.append("close").append(id).append(' '); throw new IllegalStateException("c" + id); }
    }

    /** try-with-resources: closing order, suppressed exceptions, finally. */
    public static String s2() {
        StringBuilder log = new StringBuilder();
        try (Res a = new Res("A", log); Res b = new Res("B", log)) {
            log.append("body ");
            throw new RuntimeException("body");
        } catch (RuntimeException e) {
            log.append("caught:").append(e.getMessage()).append(" suppressed:");
            for (Throwable t : e.getSuppressed()) log.append(t.getMessage()).append(',');
        } finally {
            log.append(" finally");
        }
        return log.toString();
    }

    /** Lazy, one element at a time through the whole pipeline. */
    public static String s3() {
        StringBuilder log = new StringBuilder();
        List<Integer> out = Stream.of(1, 2, 3, 4, 5, 6)
                .peek(i -> log.append('p').append(i))
                .filter(i -> i % 2 == 0)
                .peek(i -> log.append('f').append(i))
                .limit(2)
                .toList();
        return log + "|" + out;
    }

    /** Conditional operator typing and unboxing. */
    public static String s4() {
        Object a = true ? 1 : 2.0;
        Object b = true ? Integer.valueOf(1) : "s";
        Integer n = null;
        String c;
        try {
            int x = false ? 1 : n;
            c = "x" + x;
        } catch (NullPointerException e) {
            c = "NPE";
        }
        char ch = 'a';
        int i = 0;
        Object d = true ? ch : 0;
        Object e = true ? ch : i;
        return a + "|" + b + "|" + c + "|" + d + "|" + e;
    }

    /** Arithmetic and string concatenation. */
    public static String s5() {
        return ('a' + 1) + "," + (char) ('a' + 1) + "," + "" + 'a' + 1 + "," + ('a' + 1 + "") + "," + 1 / 2 * 2.0 + ","
                + Math.abs(Integer.MIN_VALUE) + "," + -7 % 3 + "," + -7 / 2 + "," + Math.floorMod(-7, 3) + ","
                + (0.1 + 0.2 == 0.3) + "," + (byte) 200 + "," + (int) 3.99e10 + "," + (5 >> 1) + (-5 >>> 31) + (-5 >> 31);
    }

    /** Collection traps. */
    public static String s6() {
        StringBuilder sb = new StringBuilder();
        List<Integer> l = new ArrayList<>(List.of(1, 2, 3, 4));
        for (Integer i : l) if (i == 3) l.remove(i);
        sb.append(l).append(' ');
        List<Integer> m = new ArrayList<>(List.of(1, 2, 3, 4));
        try {
            for (Integer i : m) if (i == 2) m.remove(i);
        } catch (ConcurrentModificationException e) {
            sb.append("CME ");
        }
        sb.append(m).append(' ');
        try { Arrays.asList(1, 2).add(3); } catch (UnsupportedOperationException e) { sb.append("UOE "); }
        try { List.of(1).contains(null); sb.append("ok "); } catch (NullPointerException e) { sb.append("NPE "); }
        List<Integer> view = Arrays.asList(5, 6, 7);
        view.set(0, 9);
        sb.append(view).append(' ');
        Map<String, Integer> tm = new TreeMap<>(Comparator.comparing(String::length));
        tm.put("aa", 1);
        tm.put("bb", 2);
        tm.put("c", 3);
        sb.append(tm);
        return sb.toString();
    }
}
