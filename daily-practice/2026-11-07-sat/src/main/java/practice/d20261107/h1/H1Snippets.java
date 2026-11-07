package practice.d20261107.h1;

import java.util.*;

/** The real code. Read it, predict each result in H1Predictions, and let the test compare. Do not run it first. */
public final class H1Snippets {
    private H1Snippets() {}

    static StringBuilder log = new StringBuilder();

    static class A {
        static { log.append("As "); }
        { log.append("Ai "); }
        A() { this(1); log.append("A() "); }
        A(int x) { log.append("A(int) "); init(); }
        void init() { log.append("A.init "); }
    }

    static class B extends A {
        static { log.append("Bs "); }
        int v = 7;
        { log.append("Bi "); }
        B() { super(); log.append("B() v=" + v + " "); }
        @Override void init() { log.append("B.init v=" + v + " "); }
    }

    /** Initialisation order: static blocks once, instance init and constructor chaining, virtual call before field init. */
    public static String s1() {
        log = new StringBuilder();
        new B();
        new B();
        return log.toString().trim();
    }

    /** Equality traps: Integer cache, equals across box types, string constants and interning. */
    public static String s2() {
        Integer a = 127, b = 127, c = 128, d = 128;
        Long l = 127L;
        final String f = "ab";
        String nf = "a";
        String x = "ab", y = "a" + "b", z = nf + "b", w = f + "";
        return (a == b) + "," + (c == d) + "," + c.equals(d) + "," + l.equals(127) + "," + l.equals(127L) + ","
                + (x == y) + "," + (x == z) + "," + (x == z.intern()) + "," + (x == w) + "," + (new String("ab") == x);
    }

    @SuppressWarnings("finally")
    static int f1() { try { return 1; } finally { return 2; } }

    static int f2() { int i = 1; try { return i; } finally { i = 99; } }

    static StringBuilder f3() { StringBuilder sb = new StringBuilder("a"); try { return sb; } finally { sb.append("b"); } }

    static String f4() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            try {
                if (i == 1) continue;
                if (i == 3) break;
                sb.append("t").append(i);
            } finally {
                sb.append("f").append(i);
            }
        }
        return sb.toString();
    }

    /** finally vs return, continue and break. */
    public static String s3() {
        return f1() + "," + f2() + "," + f3() + "," + f4();
    }

    static final class Key {
        int id;
        Key(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof Key k && k.id == id; }
        @Override public int hashCode() { return id; }
        @Override public String toString() { return "K" + id; }
    }

    /** Mutating a key after insertion; equals without hashCode. */
    public static String s4() {
        Map<Key, String> m = new HashMap<>();
        Key k = new Key(1);
        m.put(k, "one");
        m.put(new Key(2), "two");
        String before = m.get(new Key(1));
        k.id = 3;
        String after1 = m.get(new Key(1)), after3 = m.get(new Key(3)), after3b = m.get(k);
        Set<Key> s = new HashSet<>(m.keySet());
        return before + "," + after1 + "," + after3 + "," + after3b + "," + m.size() + "," + s.contains(k) + "," + m.containsKey(new Key(2));
    }

    static String m(long x) { return "long"; }
    static String m(Integer x) { return "Integer"; }
    static String m(Object x) { return "Object"; }
    static String m(int... x) { return "varargs"; }
    static String n(double x) { return "double"; }
    static String n(Object x) { return "Object"; }
    static String p(Object o) { return "Object"; }
    static String p(String s) { return "String"; }

    /** Overload resolution: widening beats boxing beats varargs; most specific wins; static type decides. */
    public static String s5() {
        byte b = 1;
        char c = 'c';
        Object o = "str";
        return m(5) + "," + m(b) + "," + m(c) + "," + m(Integer.valueOf(5)) + "," + m() + "," + m(5L) + "," + m("s") + ","
                + n(5) + "," + n(Integer.valueOf(5)) + "," + p("s") + "," + p(o) + "," + p(null);
    }

    /** Collection views and iteration order. */
    public static String s6() {
        StringBuilder sb = new StringBuilder();
        List<Integer> base = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> sub = base.subList(1, 4);
        sub.set(0, 20);
        sb.append(base).append(' ');
        base.add(6);
        try { sub.size(); sb.append("ok "); } catch (ConcurrentModificationException e) { sb.append("CME "); }
        List<Integer> ro = Collections.unmodifiableList(base);
        base.set(0, 10);
        sb.append(ro.get(0)).append(' ');
        PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(5, 1, 4, 2, 3));
        sb.append(pq).append(' ');
        StringBuilder polled = new StringBuilder();
        while (!pq.isEmpty()) polled.append(pq.poll());
        sb.append(polled).append(' ');
        int[] arr = {3, 1, 2};
        sb.append(Arrays.asList(arr).size()).append(' ');
        Map<String, Integer> lhm = new LinkedHashMap<>(16, 0.75f, true);
        lhm.put("a", 1); lhm.put("b", 2); lhm.put("c", 3);
        lhm.get("a");
        lhm.putIfAbsent("b", 9);
        sb.append(lhm.keySet()).append(' ');
        Set<String> hs = new HashSet<>(List.of("banana", "apple", "cherry"));
        sb.append(new TreeSet<>(hs));
        return sb.toString();
    }
}
