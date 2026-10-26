package practice.d20261026.h1;

import java.util.*;

/** The snippets. Do NOT run them before predicting. Each returns a String that H1Predictions must reproduce exactly. */
public final class H1Snippets {
    private H1Snippets() {}

    /** (1) Integer cache, equals across types, string constants and intern */
    public static String s1() {
        Integer a = 127, b = 127, c = 128, d = 128;
        Long l = 127L;
        String x = "ab", y = "a" + "b";
        String half = "a";
        String z = half + "b";
        final String fhalf = "a";
        String w = fhalf + "b";
        return (a == b) + " " + (c == d) + " " + c.equals(d) + " " + l.equals(127) + " " + l.equals(127L) + " "
                + (x == y) + " " + (x == z) + " " + (x == z.intern()) + " " + (x == w) + " " + new String("ab").equals(x);
    }

    /** (2) initialisation order with inheritance; an overridable method called from a constructor */
    static class Base {
        static final StringBuilder LOG = new StringBuilder();
        static { LOG.append("Bs "); }
        int n = log("Bf ", 1);
        { LOG.append("Bi "); }
        Base() { LOG.append("Bc "); hook(); }
        void hook() { LOG.append("Bh "); }
        static int log(String s, int v) { LOG.append(s); return v; }
    }

    static class Derived extends Base {
        static { LOG.append("Ds "); }
        int m = log("Df ", 5);
        { LOG.append("Di "); }
        Derived() { super(); LOG.append("Dc "); }
        @Override void hook() { LOG.append("Dh(m=" + m + ") "); }
    }

    public static String s2() {
        Base.LOG.setLength(0);
        new Derived();
        Base.LOG.append("| ");
        new Derived();
        return Base.LOG.toString().trim();
    }

    /** (3) mutable keys and equals without hashCode */
    static final class Key {
        int id;
        Key(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof Key k && k.id == id; }
        @Override public int hashCode() { return id; }
    }

    static final class NoHash {
        final int id;
        NoHash(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof NoHash n && n.id == id; }
    }

    public static String s3() {
        Map<Key, String> map = new HashMap<>();
        Key k = new Key(1);
        map.put(k, "v");
        k.id = 2;
        StringBuilder sb = new StringBuilder();
        sb.append(map.get(k)).append(' ').append(map.get(new Key(2))).append(' ').append(map.get(new Key(1))).append(' ')
                .append(map.containsValue("v")).append(' ').append(map.size()).append(' ');
        Set<NoHash> set = new HashSet<>();
        set.add(new NoHash(1));
        set.add(new NoHash(1));
        sb.append(set.size()).append(' ');
        List<NoHash> list = new ArrayList<>(List.of(new NoHash(1)));
        sb.append(list.contains(new NoHash(1))).append(' ');
        List<Integer> nums = new ArrayList<>(List.of(10, 20, 30, 1));
        nums.remove(1);
        nums.remove(Integer.valueOf(1));
        sb.append(nums);
        return sb.toString();
    }

    /** (4) char arithmetic, string concatenation order, compound assignment */
    public static String s4() {
        char c = 'a';
        c += 1;
        c++;
        int i = 'a' + 1;
        short s = 10;
        s += 5.7;
        byte b = (byte) 200;
        int sh = -16 >> 2, ush = -16 >>> 28;
        return c + " " + i + " " + (char) (c + 1) + " " + 1 + 2 + " " + (1 + 2) + " " + 'a' + 'b' + " " + (char) ('a' + 'b' - 'a') + " "
                + s + " " + b + " " + sh + " " + ush + " " + (5 / 2) + " " + (-5 / 2) + " " + (-5 % 3) + " " + Math.floorMod(-5, 3);
    }

    /** (5) arrays: covariance, asList on primitives, clone depth, varargs */
    static int count(Object... xs) { return xs == null ? -1 : xs.length; }

    public static String s5() {
        StringBuilder sb = new StringBuilder();
        Object[] objs = new String[1];
        try { objs[0] = 1; } catch (ArrayStoreException e) { sb.append("ASE "); }
        int[] prim = {1, 2, 3};
        Integer[] boxed = {1, 2, 3};
        sb.append(Arrays.asList(prim).size()).append(Arrays.asList(boxed).size()).append(' ');
        int[][] grid = {{1, 2}, {3, 4}};
        int[][] copy = grid.clone();
        copy[0][0] = 99;
        copy[1] = new int[]{7, 7};
        sb.append(grid[0][0]).append(grid[1][0]).append(' ');
        sb.append(count()).append(count((Object[]) null)).append(count((Object) null)).append(count(new String[]{"a", "b"})).append(count(prim)).append(count(1, 2));
        sb.append(' ').append(new int[0].equals(new int[0])).append(Arrays.equals(new int[]{1}, new int[]{1})).append(Arrays.hashCode(new int[]{1, 2}));
        return sb.toString();
    }

    /** (6) records, pattern-matching switch, unboxing a null in a conditional */
    record Point(int x, int y) {
        Point { if (x < 0) x = 0; }
    }

    sealed interface Shape permits Circle, Square {}
    record Circle(int r) implements Shape {}
    record Square(int side) implements Shape {}

    static String describe(Object o) {
        return switch (o) {
            case null -> "null";
            case Integer i when i > 10 -> "big" + i;
            case Integer i -> "int" + i;
            case Circle c -> "circle" + c.r();
            case Shape s -> "shape";
            case String s -> "str" + s.length();
            default -> "other";
        };
    }

    public static String s6() {
        StringBuilder sb = new StringBuilder();
        sb.append(new Point(-5, 2)).append(' ').append(new Point(1, 2).equals(new Point(1, 2))).append(' ');
        sb.append(describe(42)).append(describe(7)).append(describe(new Circle(3))).append(describe(new Square(1)))
                .append(describe("abc")).append(describe(null)).append(describe(2.5)).append(' ');
        Integer boxed = null;
        boolean flag = false;
        try {
            int v = flag ? 1 : boxed;
            sb.append(v);
        } catch (NullPointerException e) {
            sb.append("NPE");
        }
        Integer ok = flag ? Integer.valueOf(1) : boxed;
        sb.append(' ').append(ok);
        return sb.toString();
    }
}
