package practice.d20261109.h1;

import java.util.*;

/** Given: do NOT run these before predicting. Each returns a log String. */
public final class H1Snippets {
    private H1Snippets() {}

    // ---- s1: initialisation order and virtual calls from a constructor
    static final StringBuilder LOG = new StringBuilder();

    static class Base {
        static { LOG.append("Bs "); }
        int x = init("Bx ", 1);
        { LOG.append("Bi "); }
        Base() { LOG.append("Bc "); show(); }
        void show() { LOG.append("Base.show "); }
        static int init(String s, int v) { LOG.append(s); return v; }
    }

    static class Derived extends Base {
        static { LOG.append("Ds "); }
        int y = init("Dy ", 7);
        final int z = 9;
        Derived() { super(); LOG.append("Dc "); }
        @Override void show() { LOG.append("Derived.show(y=" + y + ",z=" + z + ") "); }
    }

    public static String s1() {
        LOG.setLength(0);
        new Derived();
        LOG.append("| ");
        new Derived();
        return LOG.toString().trim();
    }

    // ---- s2: try / finally and return values
    @SuppressWarnings("finally")
    static int f1() { try { return 1; } finally { return 2; } }
    static int f2() { int i = 1; try { return i; } finally { i = 99; } }
    static int[] f3() { int[] a = {1}; try { return a; } finally { a[0] = 99; } }
    @SuppressWarnings("finally")
    static String f4() {
        try { throw new IllegalStateException("boom"); }
        finally { return "swallowed"; }
    }
    static String f5() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            try {
                if (i == 1) continue;
                sb.append("t").append(i);
            } finally {
                sb.append("f").append(i);
            }
        }
        return sb.toString();
    }

    public static String s2() { return f1() + " " + f2() + " " + f3()[0] + " " + f4() + " " + f5(); }

    // ---- s3: boxing, caching, equals
    public static String s3() {
        Integer a = 127, b = 127, c = 128, d = 128;
        Long l = 127L;
        StringBuilder sb = new StringBuilder();
        sb.append(a == b).append(' ').append(c == d).append(' ').append(c.equals(d)).append(' ');
        sb.append(l.equals(127)).append(' ').append(l.equals(127L)).append(' ').append(l == 127).append(' ');
        Integer n = null;
        try { int bad = n; sb.append(bad); } catch (NullPointerException e) { sb.append("NPE"); }
        sb.append(' ').append(new Object() { int v = 5; }.v);
        Integer i1 = 10;
        Integer i2 = i1;
        i1++;
        sb.append(' ').append(i1).append('/').append(i2);
        sb.append(' ').append(Double.valueOf(0.0).equals(-0.0)).append(' ').append(0.0 == -0.0);
        sb.append(' ').append(Double.NaN == Double.NaN).append(' ').append(Double.valueOf(Double.NaN).equals(Double.NaN));
        return sb.toString();
    }

    // ---- s4: mutable keys, views and fail-fast iteration
    static final class Key {
        int id;
        Key(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof Key k && k.id == id; }
        @Override public int hashCode() { return id; }
        @Override public String toString() { return "K" + id; }
    }

    public static String s4() {
        StringBuilder sb = new StringBuilder();
        Map<Key, String> m = new HashMap<>();
        Key k = new Key(1);
        m.put(k, "one");
        k.id = 2;
        sb.append(m.get(k)).append(' ').append(m.get(new Key(1))).append(' ').append(m.get(new Key(2))).append(' ').append(m.size()).append(' ');
        m.put(new Key(1), "uno");
        sb.append(m.size()).append(' ');

        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4));
        try { for (Integer x : list) if (x == 2) list.remove(x); sb.append(list); }
        catch (ConcurrentModificationException e) { sb.append("CME"); }
        sb.append(' ');
        List<Integer> l2 = new ArrayList<>(List.of(1, 2, 3, 4));
        try { for (Integer x : l2) if (x == 3) l2.remove(x); sb.append(l2); }
        catch (ConcurrentModificationException e) { sb.append("CME"); }
        sb.append(' ');
        Map<String, Integer> tm = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        tm.put("b", 1); tm.put("B", 2); tm.put("a", 3);
        sb.append(tm);
        try { List.of(1).add(2); } catch (UnsupportedOperationException e) { sb.append(" UOE"); }
        return sb.toString();
    }

    // ---- s5: strings and chars
    public static String s5() {
        StringBuilder sb = new StringBuilder();
        char c = 'a';
        c += 1;
        c++;
        sb.append(c).append(' ');
        sb.append(1 + 2 + "x" + 1 + 2).append(' ');
        sb.append('a' + 'b').append(' ').append("" + 'a' + 'b').append(' ').append((char) ('a' + 1)).append(' ');
        String s1 = "hi", s2 = "h" + "i", s3 = new String("hi"), part = "h", s4 = part + "i";
        sb.append(s1 == s2).append(' ').append(s1 == s3).append(' ').append(s1 == s4).append(' ').append(s1 == s4.intern()).append(' ');
        sb.append("a,b,,".split(",").length).append(' ').append("a,b,,".split(",", -1).length).append(' ');
        sb.append("Straße".toUpperCase()).append(' ').append("  x ".strip().length()).append(' ');
        sb.append(String.valueOf((Object) null)).append(' ').append("abc".substring(3).isEmpty()).append(' ');
        sb.append((int) 'A' + (byte) 200).append(' ').append((short) 70000).append(' ').append(7 / 2 * 2.0).append(' ').append(-7 % 3).append(' ').append(Math.floorMod(-7, 3));
        return sb.toString();
    }

    // ---- s6: overload resolution and varargs
    static String m(Object o) { return "Object"; }
    static String m(String s) { return "String"; }
    static String m(Integer i) { return "Integer"; }
    static String m(long l) { return "long"; }
    static String m(int... xs) { return "int..."; }
    static String n(long l) { return "long"; }
    static String n(Integer i) { return "Integer"; }
    static String n(Object o) { return "Object"; }
    static String v(Object... os) { return "len=" + (os == null ? "null" : os.length); }

    public static String s6() {
        StringBuilder sb = new StringBuilder();
        byte b = 1; char ch = 'c'; short sh = 2;
        sb.append(m(5)).append(' ').append(m(b)).append(' ').append(m(ch)).append(' ').append(m("s")).append(' ');
        sb.append(m(5.0)).append(' ').append(m()).append(' ').append(m(1, 2)).append(' ').append(m((Object) "s")).append(' ');
        sb.append(n(sh)).append(' ').append(n(Integer.valueOf(3))).append(' ').append(n(3.5f)).append(' ');
        sb.append(v()).append(' ').append(v((Object[]) null)).append(' ').append(v((Object) null)).append(' ').append(v(new String[] {"a", "b"})).append(' ').append(v(new int[] {1, 2}));
        return sb.toString();
    }
}
