package practice.d20261024.h1;

import java.util.*;

/** Read each snippet, predict what it returns, write the String in H1Predictions. Do not run it first. */
public final class H1Snippets {
    private H1Snippets() {}

    // ---- 1: boxing, caches and equality
    public static String s1() {
        Integer a = 127, b = 127, c = 128, d = 128;
        Long l = 127L;
        String s1 = "hi", s2 = new String("hi"), s3 = s2.intern();
        return (a == b) + "," + (c == d) + "," + c.equals(d) + "," + l.equals(127) + "," + (l == 127) + ","
                + (s1 == s2) + "," + (s1 == s3);
    }

    // ---- 2: remove(int) vs remove(Object), and removing while iterating
    public static String s2() {
        List<Integer> list = new ArrayList<>(List.of(10, 20, 30, 1, 2));
        list.remove(1);
        list.remove(Integer.valueOf(1));
        Set<Integer> set = new HashSet<>(List.of(1, 2, 3));
        set.remove(1);
        List<String> names = new ArrayList<>(List.of("a", "b", "c"));
        String r;
        try {
            for (String n : names) if (n.equals("b")) names.remove(n);
            r = "no-throw " + names;
        } catch (ConcurrentModificationException e) {
            r = "CME " + names;
        }
        List<String> more = new ArrayList<>(List.of("a", "b", "c"));
        String r2;
        try {
            for (String n : more) if (n.equals("a")) more.remove(n);
            r2 = "no-throw " + more;
        } catch (ConcurrentModificationException e) {
            r2 = "CME " + more;
        }
        return list + " " + set + " " + r + " " + r2;
    }

    // ---- 3: initialisation order and an overridable call in a constructor
    static final List<String> LOG = new ArrayList<>();
    static class Base {
        static { LOG.add("Base.static"); }
        { LOG.add("Base.init"); }
        Base() { LOG.add("Base() sees " + describe()); }
        String describe() { return "base"; }
    }
    static class Derived extends Base {
        static { LOG.add("Derived.static"); }
        private String tag = "set";
        private final int fixed = 7;
        { LOG.add("Derived.init"); }
        Derived() { LOG.add("Derived() tag=" + tag); }
        @Override String describe() { return "derived tag=" + tag + " fixed=" + fixed; }
    }
    public static String s3() {
        LOG.clear();
        new Derived();
        new Derived();
        return String.join("|", LOG);
    }

    // ---- 4: numeric traps
    public static String s4() {
        byte b = 120;
        b += 10;
        char ch = 'a';
        ch += 1;
        int big = Integer.MAX_VALUE;
        long ok = big + 1L, wrong = big + 1;
        return b + "," + ch + "," + (ch + 1) + "," + (char) (ch + 1) + "," + ok + "," + wrong + ","
                + Math.abs(Integer.MIN_VALUE) + "," + (int) 3.99e10 + "," + (0.1 + 0.2 == 0.3) + "," + (-7 / 2) + "," + (-7 % 3)
                + "," + Math.floorMod(-7, 3) + "," + (1 / 2 * 2.0);
    }

    // ---- 5: String API surprises
    public static String s5() {
        return "a,b,,".split(",").length + "," + ",a,,b".split(",").length + "," + "a,b,,".split(",", -1).length + ","
                + "".split(",").length + "," + "a.b".replaceAll(".", "x") + "," + "a.b".replace(".", "x") + ","
                + "hello".substring(2, 2).isEmpty() + "," + "b".compareTo("abc") + "," + "  x ".strip().length() + ","
                + String.valueOf((Object) null) + "," + ("a" + 1 + 2) + "," + (1 + 2 + "a");
    }

    // ---- 6: equals/hashCode contract with a HashMap
    static final class Key {
        final int id;
        Key(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof Key k && k.id == id; }
    }
    static final class Key2 {
        final int id;
        Key2(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof Key2 k && k.id == id; }
        @Override public int hashCode() { return id % 2; }
    }
    public static String s6() {
        Map<Key, String> m1 = new HashMap<>();
        m1.put(new Key(1), "x");
        Map<Key2, String> m2 = new HashMap<>();
        for (int i = 0; i < 6; i++) m2.put(new Key2(i), "v" + i);
        m2.put(new Key2(2), "again");
        return m1.get(new Key(1)) + "," + m1.containsKey(new Key(1)) + "," + new Key(1).equals(new Key(1)) + ","
                + m2.size() + "," + m2.get(new Key2(2)) + "," + m2.get(new Key2(9)) + "," + new HashSet<>(List.of(new Key(1), new Key(1))).size();
    }
}
