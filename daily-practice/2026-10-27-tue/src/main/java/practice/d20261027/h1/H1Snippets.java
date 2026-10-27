package practice.d20261027.h1;

import java.util.*;

/** The snippets. Do NOT run them before predicting. Each returns a String that H1Predictions must reproduce exactly. */
public final class H1Snippets {
    private H1Snippets() {}

    /** (1) return values captured before finally; finally that overrides; continue inside try */
    @SuppressWarnings("finally")
    static int a() { int x = 1; try { return x; } finally { x = 2; } }
    @SuppressWarnings("finally")
    static int b() { try { throw new RuntimeException("boom"); } finally { return 3; } }
    static StringBuilder c() { StringBuilder sb = new StringBuilder("a"); try { return sb; } finally { sb.append("b"); } }
    static String d() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            try { if (i == 1) continue; sb.append(i); } finally { sb.append('f'); }
        }
        return sb.toString();
    }

    public static String s1() { return a() + " " + b() + " " + c() + " " + d(); }

    /** (2) numeric edge cases */
    public static String s2() {
        int big = Integer.MAX_VALUE;
        return Math.abs(Integer.MIN_VALUE) + " " + (big + 1) + " " + (int) 3.99e10 + " " + (int) Double.NaN + " " + (int) -2.9 + " "
                + (0.1 + 0.2 == 0.3) + " " + (byte) (127 + 1) + " " + (1.0 / 0) + " " + (0.0 == -0.0) + " "
                + Double.valueOf(0.0).equals(-0.0) + " " + (Double.NaN == Double.NaN) + " " + (long) big * 2 + " " + big * 2 + " "
                + (char) 65 + (int) 'A' + " " + (5 & 3 | 4 ^ 1);
    }

    /** (3) collections: immutability, views, fail-fast iterators */
    public static String s3() {
        StringBuilder sb = new StringBuilder();
        try { List.of(1).add(2); } catch (UnsupportedOperationException e) { sb.append("UOE "); }
        List<Integer> fixed = Arrays.asList(1, 2, 3);
        fixed.set(0, 9);
        try { fixed.add(4); } catch (UnsupportedOperationException e) { sb.append("UOE2 "); }
        sb.append(fixed).append(' ');
        List<Integer> base = new ArrayList<>(List.of(1, 2, 3, 4));
        List<Integer> sub = base.subList(1, 3);
        sub.set(0, 20);
        sb.append(base).append(' ');
        base.add(5);
        try { sub.size(); } catch (ConcurrentModificationException e) { sb.append("CME "); }
        List<Integer> l = new ArrayList<>(List.of(1, 2, 3));
        for (Integer x : l) if (x == 2) l.remove(x);
        sb.append(l).append(' ');
        List<Integer> l2 = new ArrayList<>(List.of(1, 2, 3));
        try { for (Integer x : l2) if (x == 1) l2.remove(x); } catch (ConcurrentModificationException e) { sb.append("CME2 "); }
        TreeSet<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
        byLength.add("aa");
        byLength.add("bb");
        byLength.add("c");
        sb.append(byLength);
        return sb.toString();
    }

    /** (4) overload resolution; static methods are hidden, instance methods overridden */
    static String p(long x) { return "long"; }
    static String p(Integer x) { return "Integer"; }
    static String p(Object x) { return "Object"; }
    static String p(int... x) { return "varargs"; }

    static class P {
        static String who() { return "P"; }
        String me() { return "P"; }
        String call() { return who() + me(); }
    }

    static class C extends P {
        static String who() { return "C"; }
        @Override String me() { return "C"; }
    }

    public static String s4() {
        P p = new C();
        short sh = 5;
        return p(5) + " " + p(Integer.valueOf(5)) + " " + p(sh) + " " + p('a') + " " + p("s") + " " + p() + " " + p(5.0) + " "
                + p.call() + " " + p.who() + " " + p.me() + " " + new C().call();
    }

    /** (5) try-with-resources: closing order and suppressed exceptions */
    static final class Res implements AutoCloseable {
        final String name;
        final StringBuilder log;
        Res(String name, StringBuilder log) { this.name = name; this.log = log; log.append("open-").append(name).append(' '); }
        @Override public void close() { log.append("close-").append(name).append(' '); throw new IllegalStateException("c" + name); }
    }

    public static String s5() {
        StringBuilder log = new StringBuilder();
        try (Res a = new Res("a", log); Res b = new Res("b", log)) {
            log.append("body ");
            throw new RuntimeException("body");
        } catch (RuntimeException e) {
            log.append(e.getMessage()).append(" suppressed=").append(e.getSuppressed().length).append(' ');
            for (Throwable t : e.getSuppressed()) log.append(t.getMessage()).append(' ');
        } finally {
            log.append("| ");
        }
        try (Res a = new Res("a", log); Res b = new Res("b", log)) {
            log.append("body2 ");
        } catch (RuntimeException e) {
            log.append(e.getMessage()).append(" suppressed=").append(e.getSuppressed().length).append(' ');
            log.append(e.getSuppressed()[0].getMessage());
        }
        return log.toString();
    }

    /** (6) conditional operator typing and unboxing */
    public static String s6() {
        int k = 0;
        Object o1 = true ? 1 : "s";
        Object o2 = true ? 1 : 2.0;
        char ch = 'x';
        Integer nul = null;
        String r;
        try { int v = true ? nul : 0; r = "v" + v; } catch (NullPointerException e) { r = "NPE"; }
        return o1 + " " + o2 + " " + (true ? ch : 0) + " " + (true ? ch : k) + " " + (false ? 1 : 'b') + " " + r + " "
                + o1.getClass().getSimpleName() + " " + o2.getClass().getSimpleName() + " " + (true ? null : 0);
    }
}
