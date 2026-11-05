package practice.d20261105.h1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Given: do not edit. Each sN() returns a String; predict it WITHOUT running. */
public final class H1Snippets {
    private H1Snippets() {}

    static final StringBuilder LOG = new StringBuilder();

    static class Base {
        static { LOG.append("Bs "); }
        { LOG.append("Bi "); }
        protected int x = init("Bx");
        Base() { LOG.append("Bc "); hook(); }
        void hook() { LOG.append("Bh "); }
        static int init(String s) { LOG.append(s).append(' '); return 1; }
    }

    static class Derived extends Base {
        static { LOG.append("Ds "); }
        private int y = init("Dy") + 41;
        { LOG.append("Di "); }
        Derived() { super(); LOG.append("Dc "); }
        @Override void hook() { LOG.append("Dh(y=").append(y).append(") "); }
    }

    /** Initialisation order: statics once, then super fields/blocks/ctor, then subclass fields/blocks/ctor; an overridden method called from the super constructor sees default field values. */
    public static String s1() {
        LOG.setLength(0);
        new Derived();
        LOG.append("| ");
        new Derived();
        return LOG.toString().trim();
    }

    @SuppressWarnings("finally")
    static int f1() { try { return 1; } finally { return 2; } }

    static int f2() { int i = 1; try { return i; } finally { i = 99; } }

    @SuppressWarnings("finally")
    static String f3() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            try {
                if (i == 1) continue;
                sb.append("t").append(i);
                if (i == 2) throw new RuntimeException("x");
            } catch (RuntimeException e) {
                sb.append("c");
            } finally {
                sb.append("f").append(i);
            }
        }
        return sb.toString();
    }

    static int f4() {
        int[] a = {1};
        try { return a[0]; } finally { a[0] = 50; }
    }

    /** try/finally and return values. */
    public static String s2() { return f1() + "," + f2() + "," + f3() + "," + f4(); }

    /** Boxing, caching, numeric promotion and char arithmetic. */
    public static String s3() {
        Integer a = 127, b = 127, c = 128, d = 128;
        Long l = 127L;
        char ch = 'a';
        ch += 1;
        byte by = (byte) 200;
        short sh = 10;
        sh += 70_000;
        Object o = true ? 1 : "s";
        Object p = true ? 1 : 2.0;
        Object q = false ? 'x' : 120;
        return (a == b) + "," + (c == d) + "," + c.equals(d) + "," + l.equals(127) + "," + l.equals(127L) + "," + ch + "," + (ch + 1) + "," + (char) (ch + 1) + ","
            + by + "," + sh + "," + o + "," + p + "," + q + "," + ('a' + 'b') + "," + "" + 'a' + 'b' + "," + (1 + 2 + "3" + 4 + 5);
    }

    /** Collection corner cases. */
    public static String s4() {
        List<Integer> list = new ArrayList<>(List.of(10, 20, 30, 1, 2));
        list.remove(1);
        list.remove(Integer.valueOf(1));
        String afterRemoves = list.toString();

        String[] arr = {"a", "b", "c"};
        List<String> view = Arrays.asList(arr);
        view.set(0, "z");
        arr[1] = "y";
        String write = Arrays.toString(arr) + view;
        String add;
        try { view.add("d"); add = "added"; } catch (UnsupportedOperationException e) { add = "UOE"; }

        List<Integer> src = new ArrayList<>(List.of(1, 2, 3));
        List<Integer> ro = Collections.unmodifiableList(src);
        List<Integer> copy = List.copyOf(src);
        src.add(4);

        record Key(List<Integer> parts) {}
        List<Integer> mutable = new ArrayList<>(List.of(1));
        Set<Key> keys = new HashSet<>();
        Key k = new Key(mutable);
        keys.add(k);
        mutable.add(2);
        boolean found = keys.contains(k);

        return afterRemoves + "|" + write + "|" + add + "|" + ro.size() + copy.size() + "|" + found + "|" + keys.size();
    }

    /** Strings: interning, compile-time constants, null handling, compareTo. */
    public static String s5() {
        String a = "hel" + "lo";
        String h = "hel";
        String b = h + "lo";
        final String fh = "hel";
        String c = fh + "lo";
        String d = new String("hello");
        return ("hello" == a) + "," + ("hello" == b) + "," + ("hello" == c) + "," + ("hello" == d) + "," + ("hello" == d.intern()) + ","
            + new StringBuilder("x").equals(new StringBuilder("x")) + "," + String.valueOf((Object) null).length() + "," + ("b".compareTo("abc")) + "," + ("abc".compareTo("abd")) + ","
            + "a,b,,".split(",").length + "," + ",a".split(",").length + "," + "x".repeat(0).isEmpty() + "," + "Hello".substring(5).length();
    }

    static String m(Object o) { return "Object"; }
    static String m(String s) { return "String"; }
    static String m(Integer i) { return "Integer"; }
    static String n(long x) { return "long"; }
    static String n(Integer x) { return "Integer"; }
    static String n(int... x) { return "varargs"; }
    static String p(double x) { return "double"; }
    static String p(Object x) { return "Object"; }

    /** Overload resolution: phases (no boxing, then boxing, then varargs), most specific type. */
    public static String s6() {
        Object o = "str";
        short s = 1;
        char c = 'c';
        return m("x") + "," + m(o) + "," + m(1) + "," + n(1) + "," + n(s) + "," + n() + "," + n(1, 2) + "," + p(1) + "," + p(1f) + "," + p(c) + "," + p("s") + ","
            + new ArrayList<String>().getClass().equals(new ArrayList<Integer>().getClass());
    }
}
