package practice.d20261029.h1;

import java.util.*;

/** The snippets. Do NOT run them before predicting. Each returns a String that H1Predictions must reproduce exactly. */
public final class H1Snippets {
    private H1Snippets() {}

    /** (1) ternary operand types: binary numeric promotion, and unboxing a null */
    public static String s1() {
        StringBuilder sb = new StringBuilder();
        boolean t = true;
        Object o1 = t ? 1 : "x";
        Object o2 = t ? 1 : 2.0;
        Object o3 = t ? (Integer) 1 : (Double) 2.0;
        Object o4 = t ? 'a' : 0;
        int i = 0;
        Object o5 = t ? 'b' : i;
        sb.append(o1).append(' ').append(o2).append(' ').append(o3).append(' ').append(o4).append(' ').append(o5).append(' ');
        Integer nul = null;
        try {
            Object o6 = !t ? 1 : nul;
            sb.append(o6);
        } catch (NullPointerException e) {
            sb.append("NPE");
        }
        try {
            int n = t ? nul : 0;
            sb.append(n);
        } catch (NullPointerException e) {
            sb.append(" NPE2");
        }
        return sb.toString();
    }

    /** (2) boxing, caching, equals across types */
    public static String s2() {
        Integer a = 127, b = 127, c = 128, d = 128;
        Long l = 127L;
        Set<Object> set = new HashSet<>();
        set.add(1); set.add(1L); set.add((short) 1); set.add((byte) 1); set.add(1);
        Map<Long, String> m = new HashMap<>();
        m.put(1L, "x");
        return (a == b) + " " + (c == d) + " " + c.equals(d) + " " + l.equals(127) + " " + l.equals(127L)
                + " " + Integer.valueOf(5).equals(5) + " " + set.size() + " " + m.get(1) + " " + (c <= d && c >= d && c != d);
    }

    static String pick(Object o) { return "Object"; }
    static String pick(String s) { return "String"; }
    static String pick(Integer i) { return "Integer"; }
    static String pick(int i) { return "int"; }
    static String pick(long l) { return "long"; }
    static String pick(int... xs) { return "varargs"; }
    static String pick(double d) { return "double"; }
    static String wide(long l) { return "long"; }
    static String wide(Integer i) { return "Integer"; }
    static String wide(Object o) { return "Object"; }

    /** (3) overload resolution: phase 1 (no boxing), phase 2 (boxing), phase 3 (varargs) */
    public static String s3() {
        byte by = 1;
        char ch = 'c';
        short sh = 2;
        float f = 1f;
        return pick(1) + " " + pick(by) + " " + pick(ch) + " " + pick(sh) + " " + pick(f) + " " + pick(Integer.valueOf(1)) + " "
                + pick("s") + " " + pick((Object) "s") + " " + pick() + " " + pick(1, 2) + " " + pick(1L) + " " + wide(5) + " " + wide((short) 5);
    }

    static final StringBuilder LOG = new StringBuilder();

    static class Res implements AutoCloseable {
        final String name; final boolean failOnClose;
        Res(String name, boolean failOnClose) { this.name = name; this.failOnClose = failOnClose; LOG.append("open-").append(name).append(' '); }
        @Override public void close() {
            LOG.append("close-").append(name).append(' ');
            if (failOnClose) throw new IllegalStateException("close-" + name);
        }
    }

    @SuppressWarnings("finally")
    static int tricky() {
        int x = 1;
        try {
            return x;
        } finally {
            x = 2;
            LOG.append("fin ");
        }
    }

    @SuppressWarnings("finally")
    static int swallow() {
        try {
            throw new RuntimeException("lost");
        } finally {
            return 9;
        }
    }

    /** (4) try-with-resources order, suppressed exceptions, finally and return */
    public static String s4() {
        LOG.setLength(0);
        String result;
        try (Res a = new Res("a", true); Res b = new Res("b", true)) {
            LOG.append("body ");
            throw new RuntimeException("body-fail");
        } catch (RuntimeException e) {
            Throwable[] sup = e.getSuppressed();
            result = e.getMessage() + " suppressed=" + sup.length + ":" + sup[0].getMessage() + "," + sup[1].getMessage();
        } finally {
            LOG.append("finally ");
        }
        int t = tricky();
        int s = swallow();
        return LOG + "| " + result + " | " + t + " " + s;
    }

    /** (5) collection views and removal overloads */
    public static String s5() {
        StringBuilder sb = new StringBuilder();
        List<Integer> list = new ArrayList<>(List.of(10, 20, 30, 1, 2));
        list.remove(1);
        list.remove(Integer.valueOf(1));
        sb.append(list).append(' ');
        List<Integer> asList = Arrays.asList(1, 2, 3);
        asList.set(0, 9);
        try { asList.add(4); } catch (UnsupportedOperationException e) { sb.append("UOE "); }
        List<Integer> fixed = List.of(1, 2, 3);
        try { fixed.set(0, 5); } catch (UnsupportedOperationException e) { sb.append("UOE2 "); }
        List<Integer> base = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> sub = base.subList(1, 4);
        sub.set(0, 20);
        sub.remove(Integer.valueOf(3));
        base.add(6);
        try { sub.size(); } catch (ConcurrentModificationException e) { sb.append("CME "); }
        sb.append(base).append(' ');
        int[] arr = {1, 2, 3};
        List<int[]> wrapped = Arrays.asList(arr);
        sb.append(wrapped.size()).append(' ').append(Arrays.asList(1, 2, 3).size());
        return sb.toString();
    }

    static final class Key {
        int id;
        Key(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof Key k && k.id == id; }
        @Override public int hashCode() { return id; }
    }

    /** (6) mutable hash keys, TreeMap with a comparator that disagrees with equals, Iterator removal */
    public static String s6() {
        StringBuilder sb = new StringBuilder();
        Map<Key, String> map = new HashMap<>();
        Key k = new Key(1);
        map.put(k, "v");
        k.id = 2;
        sb.append(map.get(k)).append(' ').append(map.get(new Key(1))).append(' ').append(map.containsKey(new Key(2))).append(' ').append(map.size()).append(' ');
        TreeSet<String> ci = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        ci.add("Apple"); ci.add("apple"); ci.add("BANANA");
        sb.append(ci).append(' ').append(ci.contains("APPLE")).append(' ').append(new HashSet<>(ci).contains("APPLE")).append(' ');
        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        for (Iterator<Integer> it = nums.iterator(); it.hasNext(); ) if (it.next() % 2 == 0) it.remove();
        sb.append(nums).append(' ');
        List<Integer> two = new ArrayList<>(List.of(1, 2));
        for (Integer x : two) if (x == 1) two.remove(x);
        sb.append(two);
        return sb.toString();
    }
}
