package practice.d20261022.h1;

import java.util.ArrayList;
import java.util.List;

/** Read each snippet, predict what it returns, write the String in H1Predictions. Do not run it first. */
public final class H1Snippets {
    private H1Snippets() {}

    // ---- 1: finally
    @SuppressWarnings("finally")
    static int f1() { int x = 1; try { return x; } finally { x = 2; } }
    @SuppressWarnings("finally")
    static int f2() { try { throw new IllegalStateException("boom"); } finally { return 5; } }
    @SuppressWarnings("finally")
    static String f3() {
        StringBuilder sb = new StringBuilder("a");
        try { return sb.append("b").toString(); } finally { sb.append("c"); }
    }
    public static String s1() { return f1() + "," + f2() + "," + f3(); }

    // ---- 2: initialisation order
    static final StringBuilder LOG = new StringBuilder();
    static class Base {
        static { LOG.append("Bs "); }
        { LOG.append("Bi "); }
        Base() { LOG.append("Bc "); hook(); }
        void hook() { LOG.append("Bh "); }
    }
    static class Derived extends Base {
        static { LOG.append("Ds "); }
        private int n = 7;
        private final String tag = "t";
        { LOG.append("Di "); }
        Derived() { LOG.append("Dc "); }
        @Override void hook() { LOG.append("Dh(n=" + n + ",tag=" + tag + ") "); }
    }
    public static String s2() {
        LOG.setLength(0);
        new Derived();
        LOG.append("| ");
        new Derived();
        return LOG.toString().trim();
    }

    // ---- 3: overload resolution
    static String m(Object o) { return "Object"; }
    static String m(String s) { return "String"; }
    static String m(long l) { return "long"; }
    static String m(Integer i) { return "Integer"; }
    static String m(int... xs) { return "varargs"; }
    public static String s3() {
        Object o = "text";
        char c = 'a';
        byte b = 1;
        return m(1) + "," + m(c) + "," + m(b) + "," + m(Integer.valueOf(1)) + "," + m("x") + "," + m(o) + "," + m(1, 2) + "," + m((Object) null) + "," + m(1.5);
    }

    // ---- 4: equality
    public static String s4() {
        Integer a = 127, b = 127, c = 128, d = 128;
        String s = "a";
        String lit = "ab";
        String dyn = s + "b";
        final String k = "a";
        return (a == b) + "," + (c == d) + "," + c.equals(d) + "," + ("a" + "b" == lit) + "," + (dyn == lit) + "," + (dyn.intern() == lit) + "," + ((k + "b") == lit)
                + "," + (new String("x") == new String("x")) + "," + new String("x").equals("x");
    }

    // ---- 5: numbers
    public static String s5() {
        short sh = 10;
        sh += 5.7;
        long big = Integer.MAX_VALUE + 1;
        long big2 = Integer.MAX_VALUE + 1L;
        return (byte) 200 + "," + big + "," + big2 + "," + (5 / 2 * 2.0) + "," + ('a' + 1) + "," + (char) ('a' + 1) + "," + (-7 % 3) + "," + (-7 / 2) + "," + Math.abs(Integer.MIN_VALUE)
                + "," + (0.1 + 0.2 == 0.3) + "," + sh + "," + (int) 3.99 + "," + (int) -3.99 + "," + (1 / 0.0) + "," + (0.0 / 0.0 == 0.0 / 0.0) + "," + (char) 98 + (int) 'b';
    }

    // ---- 6: collections
    public static String s6() {
        List<Integer> l = new ArrayList<>(List.of(10, 20, 30));
        l.remove(1);
        l.remove(Integer.valueOf(10));
        List<Integer> m = new ArrayList<>(List.of(1, 2, 3));
        for (Integer x : m) if (x == 2) m.remove(x);
        String second;
        List<Integer> n = new ArrayList<>(List.of(1, 2, 3));
        try {
            for (Integer x : n) if (x == 1) n.remove(x);
            second = "no exception";
        } catch (java.util.ConcurrentModificationException e) {
            second = "CME";
        }
        return l + " " + m + " " + second + " " + n;
    }
}
