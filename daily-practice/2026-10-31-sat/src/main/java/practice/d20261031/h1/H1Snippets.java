package practice.d20261031.h1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** The snippets. Do NOT run them before predicting. Each returns a String that H1Predictions must reproduce exactly. */
public final class H1Snippets {
    private H1Snippets() {}

    /** (1) boxing cache, ternary type promotion, unboxing null */
    public static String s1() {
        Integer a = 127, b = 127, c = 128, d = 128;
        boolean flag = true;
        Object o1 = flag ? 1 : "x";
        Object o2 = flag ? 1 : 2.0;
        Object o3 = flag ? (Object) 1 : 2.0;
        Integer nothing = null;
        String npe;
        try { int x = flag ? nothing : 0; npe = "no" + x; } catch (NullPointerException e) { npe = "NPE"; }
        return (a == b) + "," + (c == d) + "," + c.equals(d) + "," + o1 + "," + o2 + "," + o3 + "," + npe;
    }

    /** (2) overload resolution: widening vs boxing vs varargs, null argument, most specific type */
    static String m(long x) { return "long"; }
    static String m(Integer x) { return "Integer"; }
    static String m(Object x) { return "Object"; }
    static String m(int... x) { return "varargs"; }
    static String n(Object o) { return "Object"; }
    static String n(String s) { return "String"; }

    public static String s2() {
        short sh = 1;
        char ch = 'a';
        return m(1) + "," + m(Integer.valueOf(1)) + "," + m("s") + "," + m() + "," + m(sh) + "," + m(ch) + "," + m(1.5) + "," + n(null) + "," + n((Object) "x");
    }

    /** (3) field hiding, overriding, static hiding */
    static class P {
        String name = "P";
        String who() { return "P.who"; }
        static String s() { return "P.s"; }
        String show() { return name + "/" + who() + "/" + s(); }
    }

    static class C extends P {
        String name = "C";
        @Override String who() { return "C.who"; }
        static String s() { return "C.s"; }
    }

    public static String s3() {
        P x = new C();
        return x.name + "," + x.who() + "," + x.s() + "," + x.show() + "," + ((C) x).name + "," + ((C) x).show();
    }

    /** (4) string identity, interning, char arithmetic in concatenation */
    public static String s4() {
        String s1 = "ab", s2 = "a" + "b", s3 = new String("ab"), part = "a", s4 = part + "b";
        final String fpart = "a";
        String s5 = fpart + "b";
        StringBuilder sb = new StringBuilder().append(1).append('a').append(1 + 'a').append((char) ('a' + 1));
        return (s1 == s2) + "," + (s1 == s3) + "," + (s1 == s4) + "," + (s1 == s4.intern()) + "," + (s1 == s5) + "," + sb + "," + (1 + 2 + "x" + 1 + 2) + "," + ('a' + 'b' + "c");
    }

    /** (5) List.remove overloads, structural modification during for-each, mutable hash keys */
    public static String s5() {
        List<Integer> l = new ArrayList<>(List.of(10, 20, 30, 1, 2));
        l.remove(1);
        l.remove(Integer.valueOf(1));
        String log = l.toString();
        for (Integer i : l) if (i == 30) l.remove(i);
        log += l;
        Set<List<Integer>> set = new HashSet<>();
        List<Integer> key = new ArrayList<>(List.of(1));
        set.add(key);
        key.add(2);
        log += "," + set.contains(key) + "," + set.contains(List.of(1, 2)) + "," + set.size();
        List<Integer> two = new ArrayList<>(List.of(1, 2, 3));
        String cme;
        try { for (Integer i : two) if (i == 1) two.remove(i); cme = "none"; } catch (java.util.ConcurrentModificationException e) { cme = "CME"; }
        return log + "," + cme;
    }

    /** (6) try-with-resources: close order, suppressed exceptions, finally */
    static final class Res implements AutoCloseable {
        final String name; final StringBuilder log;
        Res(String name, StringBuilder log) { this.name = name; this.log = log; log.append("open").append(name).append(' '); }
        @Override public void close() { log.append("close").append(name).append(' '); throw new IllegalStateException("c" + name); }
    }

    public static String s6() {
        StringBuilder log = new StringBuilder();
        try (Res a = new Res("A", log); Res b = new Res("B", log)) {
            log.append("body ");
            throw new RuntimeException("boom");
        } catch (RuntimeException e) {
            log.append(e.getMessage()).append(" suppressed=");
            for (Throwable t : e.getSuppressed()) log.append(t.getMessage()).append(';');
            log.append(' ');
        } finally {
            log.append("fin");
        }
        return log.toString();
    }
}
