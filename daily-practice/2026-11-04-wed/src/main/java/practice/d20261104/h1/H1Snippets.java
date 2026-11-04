package practice.d20261104.h1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/** Given: do not edit. Each sN() returns a String; predict it WITHOUT running. */
public final class H1Snippets {
    private H1Snippets() {}

    static class P {
        String name = "P";
        static String who() { return "Ps"; }
        String me() { return "Pm"; }
        String call() { return name + who() + me(); }
        String f(Object o) { return "Po"; }
    }

    static class C extends P {
        String name = "C";
        static String who() { return "Cs"; }
        @Override String me() { return "Cm"; }
        String f(String s) { return "Cstr"; }
    }

    /** Fields and static methods are not polymorphic; overloads are chosen by the STATIC type. */
    public static String s1() {
        P p = new C();
        C c = (C) p;
        return p.name + "," + c.name + "," + ((P) c).name + "," + p.call() + "," + p.f("x") + "," + c.f("x") + "," + c.f((Object) "x");
    }

    static final StringBuilder LOG = new StringBuilder();

    static class Res implements AutoCloseable {
        final String n;
        final boolean failOnClose;
        Res(String n, boolean failOnClose) { this.n = n; this.failOnClose = failOnClose; LOG.append("open").append(n).append(' '); }
        @Override public void close() {
            LOG.append("close").append(n).append(' ');
            if (failOnClose) throw new IllegalStateException("c" + n);
        }
    }

    /** try-with-resources: closing order, suppressed exceptions, finally. */
    public static String s2() {
        LOG.setLength(0);
        try (Res a = new Res("A", true); Res b = new Res("B", true)) {
            LOG.append("body ");
            throw new RuntimeException("main");
        } catch (RuntimeException e) {
            LOG.append(e.getMessage()).append(" suppressed=").append(e.getSuppressed().length).append(' ')
                    .append(e.getSuppressed()[0].getMessage()).append(',').append(e.getSuppressed()[1].getMessage());
        } finally {
            LOG.append(" fin | ");
        }
        try (Res c = new Res("C", true)) {
            LOG.append("ok ");
        } catch (IllegalStateException e) {
            LOG.append("caught-").append(e.getMessage()).append(' ').append(e.getSuppressed().length);
        }
        return LOG.toString();
    }

    /** String corner cases. */
    public static String s3() {
        return String.join("|",
                "" + "a,b,,".split(",").length,
                "" + "".split(",").length,
                "" + ",a".split(",").length,
                "" + "a,b,,".split(",", -1).length,
                "a.b".replaceAll(".", "x"),
                "a.b".replace(".", "x"),
                "" + "abc".compareTo("abd"),
                "" + "b".compareTo("abc"),
                "x" + null,
                String.valueOf((Object) null),
                1 + 2 + "3" + 4 + 5,
                "" + "abc".indexOf("") + "abc".lastIndexOf(""),
                "" + "  x ".strip().length() + "  x ".length());
    }

    /** Collections: modification during iteration, views, heap order, comparator-defined equality. */
    public static String s4() {
        List<String> out = new ArrayList<>();
        List<Integer> l = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        for (Integer x : l) if (x == 4) l.remove(x);               // second to last element
        out.add(l.toString());
        try {
            for (Integer x : l) if (x == 2) l.remove(x);
            out.add("no cme");
        } catch (ConcurrentModificationException e) {
            out.add("cme " + l);
        }
        List<Integer> base = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> sub = base.subList(1, 4);
        sub.set(0, 9);
        base.add(6);
        try { out.add("size " + sub.size()); } catch (ConcurrentModificationException e) { out.add("sub cme " + base); }
        PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(5, 1, 4, 2, 3));
        out.add(pq.toString());
        StringBuilder polled = new StringBuilder();
        while (!pq.isEmpty()) polled.append(pq.poll());
        out.add(polled.toString());
        Set<String> ci = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        ci.add("a"); ci.add("A"); ci.add("b");
        out.add(ci.toString());
        Set<int[]> arrays = new HashSet<>();
        arrays.add(new int[]{1}); arrays.add(new int[]{1});
        out.add("" + arrays.size());
        Map<String, Integer> lru = new LinkedHashMap<>(16, 0.75f, true);
        lru.put("a", 1); lru.put("b", 2); lru.put("c", 3);
        lru.get("a");
        out.add(lru.keySet().toString());
        List<Integer> backing = new ArrayList<>(List.of(1));
        List<Integer> view = Collections.unmodifiableList(backing);
        backing.add(2);
        out.add(view.toString());
        return String.join(" ; ", out);
    }

    /** Lazy, one-element-at-a-time streams; reuse; toMap duplicates; capture. */
    public static String s5() {
        StringBuilder sb = new StringBuilder();
        Stream.of("a", "b", "c", "d")
                .filter(s -> { sb.append("f").append(s).append(' '); return !s.equals("a"); })
                .map(s -> { sb.append("m").append(s).append(' '); return s.toUpperCase(); })
                .limit(2)
                .forEach(s -> sb.append("e").append(s).append(' '));
        sb.append("| ");
        Stream<Integer> st = Stream.of(1, 2, 3);
        st.count();
        try { st.count(); } catch (IllegalStateException e) { sb.append(e.getMessage()).append(" | "); }
        int[] peeks = {0};
        Stream.iterate(1, x -> x * 2).peek(x -> peeks[0]++).anyMatch(x -> x > 8);
        sb.append("peeks=").append(peeks[0]).append(" | ");
        try {
            IntStream.range(0, 5).boxed().collect(Collectors.toMap(i -> i % 2, i -> i));
        } catch (IllegalStateException e) { sb.append(e.getMessage()).append(" | "); }
        List<Runnable> rs = new ArrayList<>();
        for (int i = 0; i < 3; i++) { int j = i * i; rs.add(() -> sb.append(j)); }
        rs.forEach(Runnable::run);
        return sb.toString();
    }

    /** Numeric conversions and floating point. */
    public static String s6() {
        char c = 'a';
        c += 1;
        float f1 = 0.1f + 0.2f;
        return String.join(",",
                "" + c + (char) ('a' + 1) + ('a' + 1),
                "" + ('a' + 'b') + "" + 'a' + 'b',
                "" + (byte) 200 + "/" + (short) 70000 + "/" + (int) 3.99e10 + "/" + (int) -3.99 + "/" + (long) 1e19,
                "" + Math.round(-2.5) + Math.round(2.5) + Math.round(-3.5),
                "" + Math.abs(Integer.MIN_VALUE) + "/" + (Integer.MAX_VALUE + 1) + "/" + (Long.MIN_VALUE == -Long.MIN_VALUE),
                "" + -7 / 2 + (-7 % 2) + Math.floorMod(-7, 2) + (7 >> 1) + (-7 >> 1) + (-7 >>> 30),
                "" + (0.1 + 0.2 == 0.3) + (f1 == 0.3f) + (1 / 0.0) + (0.0 == -0.0) + Double.valueOf(0.0).equals(-0.0),
                "" + (Double.NaN == Double.NaN) + Double.valueOf(Double.NaN).equals(Double.NaN) + (0.0 / 0 > 1) + !(0.0 / 0 > 1));
    }
}
