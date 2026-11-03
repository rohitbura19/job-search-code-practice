package practice.d20261103.h1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/** Given: do not edit. Each sN() returns a String; predict it WITHOUT running. */
public final class H1Snippets {
    private H1Snippets() {}

    static final StringBuilder LOG = new StringBuilder();
    static int note(String s) { LOG.append(s).append(' '); return 1; }

    static class Base {
        static { note("Bs"); }
        int a = note("Ba");
        { note("Bi"); }
        Base() { note("Bc"); show(); }
        void show() { note("Bshow"); }
    }

    static class Derived extends Base {
        static { note("Ds"); }
        int b = note("Db");
        int z = 7;
        Derived() { super(); note("Dc" + z); }
        @Override void show() { note("Dshow" + z); }
    }

    /** Class initialisation and construction order. NOTE: static blocks run once per JVM, so call this ONCE. */
    public static String s1() {
        LOG.setLength(0);
        new Derived();
        note("|");
        new Derived();
        return LOG.toString().trim();
    }

    @SuppressWarnings({"finally", "ReturnInsideFinallyBlock"})
    static int f1() { try { return 1; } finally { return 2; } }
    static int f2() { int x = 1; try { return x; } finally { x = 5; } }
    @SuppressWarnings({"finally", "ReturnInsideFinallyBlock"})
    static String f3() { try { throw new RuntimeException("a"); } catch (RuntimeException e) { throw new IllegalStateException("b"); } finally { return "fin"; } }
    static int f4() { int i = 0; try { i++; throw new Exception(); } catch (Exception e) { i += 10; return i; } finally { i += 100; } }
    static StringBuilder f5() { StringBuilder sb = new StringBuilder("a"); try { return sb; } finally { sb.append("b"); } }

    /** try / catch / finally and return values. */
    public static String s2() { return f1() + "," + f2() + "," + f3() + "," + f4() + "," + f5(); }

    /** Wrapper caching, String pool, numeric promotion in ?: and +. */
    public static String s3() {
        Integer a = 127, b = 127, c = 128, d = 128;
        String h = "hel";
        Long l = 127L;
        Object o = true ? 1 : "s";
        Object p = true ? 1 : 2.0;
        char ch = 'a';
        ch += 1;
        short sh = 10;
        sh += 5.7;
        return (a == b) + "," + (c == d) + "," + c.equals(d) + "," + ("hel" + "lo" == "hello") + "," + (h + "lo" == "hello") + ","
                + ((h + "lo").intern() == "hello") + "," + (new String("x") == "x") + "," + l.equals(127) + "," + l.equals(127L) + ","
                + o + "," + p + "," + ch + "," + (ch + 1) + "," + (char) (ch + 1) + "," + sh + ","
                + ("" + 'a' + 'b') + "," + ('a' + 'b' + "") + "," + (1 + 2 + "3" + 4 + 5);
    }

    static final class Key {
        int v;
        Key(int v) { this.v = v; }
        @Override public boolean equals(Object o) { return o instanceof Key k && k.v == v; }
        @Override public int hashCode() { return v; }
    }

    /** equals/hashCode contract, mutable keys, list and set equality, remove overloads. */
    public static String s4() {
        Map<Key, String> m = new HashMap<>();
        Key k = new Key(1);
        m.put(k, "one");
        k.v = 2;
        String r1 = m.get(k) + "/" + m.get(new Key(1)) + "/" + m.get(new Key(2)) + "/" + m.containsKey(k) + "/" + m.size();

        Set<int[]> arrays = new HashSet<>(List.of(new int[]{1}, new int[]{1}));
        String r2 = arrays.size() + "/" + Arrays.equals(new int[]{1}, new int[]{1}) + "/" + new int[]{1}.equals(new int[]{1});

        String r3 = List.of(1, 2).equals(Arrays.asList(1, 2)) + "/" + new ArrayList<>(List.of(1)).equals(new LinkedList<>(List.of(1)))
                + "/" + Set.of(1, 2).equals(new TreeSet<>(List.of(2, 1))) + "/" + List.of(1).equals(Set.of(1));

        List<Integer> x = new ArrayList<>(List.of(3, 2, 1, 0)), y = new ArrayList<>(List.of(3, 2, 1, 0));
        x.remove(1);
        y.remove(Integer.valueOf(1));
        String r4 = x + "" + y;

        String r5;
        try { Arrays.asList(1, 2, 3).add(4); r5 = "added"; } catch (UnsupportedOperationException e) { r5 = "UOE"; }
        String r6;
        try { List.of(1).set(0, 2); r6 = "set"; } catch (UnsupportedOperationException e) { r6 = "UOE"; }
        List<Integer> backed = Arrays.asList(1, 2, 3);
        backed.set(0, 9);
        return r1 + " " + r2 + " " + r3 + " " + r4 + " " + r5 + " " + r6 + " " + backed;
    }

    static String h(Object o) { return "O"; }
    static String h(String s) { return "S"; }
    static String w(Object o) { return "O"; }
    static String w(Integer i) { return "I"; }
    static String w(long l) { return "l"; }

    /** Erasure, raw types, array covariance, overload with null / primitives. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String s5() {
        List<String> ls = new ArrayList<>();
        List<Integer> li = new ArrayList<>();
        List raw = ls;
        raw.add(1);
        String r1;
        try { String s = ls.get(0); r1 = "got " + s; } catch (ClassCastException e) { r1 = "CCE"; }
        Object[] oa = new String[1];
        String r2;
        try { oa[0] = 1; r2 = "stored"; } catch (ArrayStoreException e) { r2 = "ASE"; }
        int[] ints = {1, 2, 3};
        Object o = ints;
        return (ls.getClass() == li.getClass()) + " " + r1 + " " + r2 + " " + h(null) + h("s") + h((Object) "s") + " "
                + w(1) + w(1L) + w((short) 1) + w('c') + w(1.5f) + " " + (o instanceof int[]) + (o instanceof Object[]) + " "
                + ints.getClass().getSimpleName() + " " + ls.size();
    }

    /** Operators: increments, compound assignment, shifts, precedence, labelled loops, integer corners. */
    public static String s6() {
        int i = 0;
        i = i++ + ++i;
        int j = 5;
        j += j++ * 2;
        int x = 10;
        x = x++;
        int k = 0;
        outer:
        for (int a = 0; a < 3; a++) {
            for (int b = 0; b < 3; b++) {
                if (b == 2) continue outer;
                if (a == 2) break outer;
                k += 10 * a + b;
            }
        }
        return i + "," + j + "," + x + "," + k + "," + (-8 >> 1) + "," + (-8 >>> 28) + "," + (1 << 33) + "," + (1L << 33) + "," + ((byte) 0x80 >> 1) + ","
                + (5 & 3 | 4 ^ 1) + "," + Math.abs(Integer.MIN_VALUE) + "," + (Integer.MAX_VALUE + 1) + "," + (int) 3.99e10 + "," + (int) -2.7 + "," + (-7 / 2) + "," + (7 >> 1);
    }
}
