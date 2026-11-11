package practice.d20261111.h1;

import java.util.*;

/** Given: do NOT run these before predicting. Each returns a log String. */
public final class H1Snippets {
    private H1Snippets() {}

    // ---- s1: class initialisation order, an overridable call from a constructor
    static class Base {
        static final List<String> LOG = new ArrayList<>();
        static { LOG.add("Base.static"); }
        protected String tag = "base-field";
        { LOG.add("Base.init"); }
        Base() { LOG.add("Base() sees " + describe()); }
        String describe() { return "Base.describe"; }
    }

    static class Derived extends Base {
        static { LOG.add("Derived.static"); }
        private String name = "derived-field";
        private final int size = 7;
        { LOG.add("Derived.init name=" + name); }
        Derived() { super(); LOG.add("Derived() sees " + describe()); }
        @Override String describe() { return "Derived.describe name=" + name + " size=" + size + " tag=" + tag; }
    }

    public static String s1() {
        Base.LOG.clear();
        new Derived();
        new Derived();
        return String.join(" | ", Base.LOG);
    }

    // ---- s2: boxing, the Integer cache, ternary typing
    public static String s2() {
        Integer a = 127, b = 127, c = 128, d = 128;
        Long l = 127L;
        StringBuilder sb = new StringBuilder();
        sb.append(a == b).append(' ').append(c == d).append(' ').append(c.equals(d)).append(' ')
                .append(l.equals(127)).append(' ').append(l.equals(127L)).append(' ').append(l == 127).append(' ');
        Object o1 = true ? 1 : 2.0;
        Object o2 = true ? Integer.valueOf(1) : "two";
        Object o3 = false ? 'a' : 98;
        int i = 98;
        Object o4 = false ? 'a' : i;
        sb.append(o1).append(' ').append(o2).append(' ').append(o3).append(' ').append(o4).append(' ');
        Integer nothing = null;
        try {
            int boom = true ? nothing : 0;
            sb.append(boom);
        } catch (NullPointerException e) {
            sb.append("NPE ");
        }
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3));
        list.remove(Integer.valueOf(1));
        list.remove(1);
        sb.append(list);
        return sb.toString();
    }

    // ---- s3: finally overrides, masks and mutates
    @SuppressWarnings("finally")
    static int f1() { try { return 1; } finally { return 2; } }

    static int f2() { int x = 1; try { return x; } finally { x = 99; } }

    static StringBuilder f3() { StringBuilder sb = new StringBuilder("a"); try { return sb; } finally { sb.append("b"); } }

    @SuppressWarnings("finally")
    static String f4() {
        try { throw new IllegalStateException("try"); }
        finally { if (true) throw new IllegalArgumentException("finally"); }
    }

    @SuppressWarnings("finally")
    static String f5() {
        for (int i = 0; i < 3; i++) {
            try { if (i == 1) continue; if (i == 2) return "returned"; }
            finally { if (i == 2) break; }
        }
        return "fell through";
    }

    public static String s3() {
        String r4;
        try { f4(); r4 = "none"; } catch (RuntimeException e) { r4 = e.getClass().getSimpleName() + ":" + e.getMessage() + ":" + e.getSuppressed().length; }
        return f1() + " " + f2() + " " + f3() + " " + r4 + " " + f5();
    }

    // ---- s4: overload resolution: widening beats boxing beats varargs; most specific wins
    static String m(int x) { return "int"; }
    static String m(long x) { return "long"; }
    static String m(Integer x) { return "Integer"; }
    static String m(Object x) { return "Object"; }
    static String m(int... x) { return "int..."; }

    static String n(Object o) { return "Object"; }
    static String n(String s) { return "String"; }
    static String n(StringBuilder s) { return "StringBuilder"; }

    static String p(long x, int y) { return "long,int"; }
    static String p(int x, long y) { return "int,long"; }
    static String p(double x) { return "double"; }
    static String p(char x) { return "char"; }

    public static String s4() {
        byte by = 1;
        short sh = 2;
        char ch = 'c';
        Object str = "text";
        return m(1) + " " + m(by) + " " + m(sh) + " " + m(ch) + " " + m(1L) + " " + m(Integer.valueOf(1)) + " " + m(1.5) + " " + m() + " " + m(1, 2)
                + " | " + n("s") + " " + n((Object) "s") + " " + n(str) + " " + n(new StringBuilder()) + " " + n((String) null)
                + " | " + p('x') + " " + p(1) + " " + p(1L) + " " + p(1.5f) + " " + p(3L, 4);
    }

    // ---- s5: hashing, equality and ordering contracts
    static class Key {
        int id;
        Key(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof Key k && k.id == id; }
        @Override public int hashCode() { return Integer.hashCode(id); }
    }

    static class NoHash {
        final int id;
        NoHash(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof NoHash n && n.id == id; }
    }

    public static String s5() {
        StringBuilder sb = new StringBuilder();
        Set<Key> set = new HashSet<>();
        Key k = new Key(1);
        set.add(k);
        k.id = 2;                                               // mutated after insertion
        sb.append(set.contains(k)).append(' ').append(set.contains(new Key(1))).append(' ').append(set.contains(new Key(2))).append(' ').append(set.size()).append(' ');
        Set<Key> again = new HashSet<>(set);                    // re-hashed copy
        sb.append(again.contains(k)).append(' ');
        Set<NoHash> nh = new HashSet<>(List.of(new NoHash(1)));
        sb.append(nh.contains(new NoHash(1))).append(' ').append(new NoHash(1).equals(new NoHash(1))).append(' ');
        TreeSet<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
        byLength.addAll(List.of("bb", "a", "cc", "ddd", "ee"));
        sb.append(byLength).append(' ').append(byLength.contains("zz")).append(' ');
        Map<String, Integer> lhm = new LinkedHashMap<>(4, 0.75f, true);     // access-ordered
        lhm.put("x", 1); lhm.put("y", 2); lhm.put("z", 3);
        lhm.get("x");
        lhm.putIfAbsent("y", 9);
        sb.append(lhm.keySet()).append(' ');
        List<Integer> sorted = new ArrayList<>(List.of(3, 1, 2));
        sorted.sort(null);
        sb.append(sorted).append(' ').append(Collections.binarySearch(sorted, 4)).append(' ').append(Objects.hash(1, 2, 3)).append(' ').append(List.of(1, 2, 3).hashCode()).append(' ').append("Aa".hashCode() == "BB".hashCode());
        return sb.toString();
    }

    // ---- s6: Strings and switch
    static String sw(int x) {
        StringBuilder sb = new StringBuilder();
        switch (x) {
            case 1: sb.append("one");
            case 2: sb.append("two"); break;
            default: sb.append("dflt");
            case 3: sb.append("three");
        }
        return sb.toString();
    }

    static String arrow(Object o) {
        return switch (o) {
            case Integer i when i > 10 -> "big" + i;
            case Integer i -> "int" + i;
            case String s -> "str" + s.length();
            case null -> "null!";
            default -> "other";
        };
    }

    public static String s6() {
        StringBuilder sb = new StringBuilder();
        sb.append(sw(1)).append(',').append(sw(2)).append(',').append(sw(3)).append(',').append(sw(9)).append(' ');
        sb.append(arrow(5)).append(',').append(arrow(50)).append(',').append(arrow("abc")).append(',').append(arrow(null)).append(',').append(arrow(2.0)).append(' ');
        sb.append("a,b,,c,,".split(",").length).append(' ').append("a,b,,c,,".split(",", -1).length).append(' ').append(",a".split(",").length).append(' ').append("".split(",").length).append(' ');
        sb.append("a.b.c".replace(".", "-")).append(' ').append("a.b.c".replaceAll(".", "-")).append(' ').append(String.valueOf((Object) null)).append(' ');
        sb.append("hello".substring(5).isEmpty()).append(' ').append("  x ".strip().length()).append(' ').append("ab".compareTo("abc")).append(' ').append("b".compareTo("a")).append(' ');
        String text = "text";
        text.concat("!");
        text.toUpperCase();
        sb.append(text).append(' ').append("x".repeat(3)).append(' ').append(String.join("-", List.of("p", "q"))).append(' ').append("Java".chars().sum());
        return sb.toString();
    }
}
