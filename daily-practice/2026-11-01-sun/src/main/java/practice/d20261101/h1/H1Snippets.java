package practice.d20261101.h1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Given: do not edit. Each sN() prints a String; predict it WITHOUT running. */
public final class H1Snippets {
    private H1Snippets() {}

    static class A {
        static final StringBuilder L = new StringBuilder();
        static { L.append("As "); }
        { L.append("Ai "); }
        A() { L.append("Ac "); }
    }

    static class B extends A {
        static { L.append("Bs "); }
        { L.append("Bi "); }
        B() { L.append("Bc "); }
    }

    /** Class initialisation and instance initialisation order. */
    public static String s1() {
        new B();
        new B();
        return A.L.toString().trim();
    }

    @SuppressWarnings("finally")
    static int thrower() {
        try { throw new RuntimeException("boom"); } finally { return 3; }
    }

    static int boxed() { int x = 1; try { return x; } finally { x = 2; } }

    static String builder() {
        StringBuilder sb = new StringBuilder("a");
        try { return sb.append("b").toString(); } finally { sb.append("c"); }
    }

    static int[] array() { int[] a = {1}; try { return a; } finally { a[0] = 9; } }

    /** return value vs finally. */
    public static String s2() {
        return boxed() + "," + thrower() + "," + builder() + "," + array()[0];
    }

    /** Numeric promotion, compound assignment, overflow. */
    public static String s3() {
        byte b = 10; b += 300;
        char c = 'a'; c += 1;
        int i = 5; i = i++ + ++i;
        String s = 1 + 2 + "x" + 1 + 2;
        String t = 'a' + 1 + "x";
        int m = Integer.MAX_VALUE + 1;
        return b + "," + c + "," + i + "," + s + "," + t + "," + m + "," + Math.abs(Integer.MIN_VALUE) + ","
                + (0.1 + 0.2 == 0.3) + "," + (1 << 33) + "," + (1L << 33) + "," + (-7 / 2) + "," + (-7 % 3) + "," + (int) 3.99e10;
    }

    /** Integer cache, equals across wrapper types, ternary typing. */
    public static String s4() {
        Integer a = 127, b = 127, c = 128, d = 128;
        Long l = 127L;
        Object o = true ? 1 : 2.0;
        return (a == b) + "," + (c == d) + "," + c.equals(d) + "," + l.equals(127) + "," + l.equals(127L) + "," + o;
    }

    /** Arrays and collections gotchas. */
    public static String s5() {
        Object[] objs = new String[1];
        String r1;
        try { objs[0] = 1; r1 = "ok"; } catch (ArrayStoreException e) { r1 = "ASE"; }

        List<Integer> fixed = Arrays.asList(1, 2, 3);
        fixed.set(0, 9);
        String r2;
        try { fixed.add(4); r2 = "added"; } catch (UnsupportedOperationException e) { r2 = "UOE"; }

        Integer[] arr = {1, 2, 3};
        List<Integer> view = Arrays.asList(arr);
        arr[1] = 7;

        Set<List<Integer>> set = new HashSet<>();
        List<Integer> key = new ArrayList<>(List.of(1));
        set.add(key);
        key.add(2);

        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3));
        nums.remove(1);
        nums.remove(Integer.valueOf(1));

        String r3;
        try { r3 = String.valueOf(List.of(1).contains(null)); } catch (NullPointerException e) { r3 = "NPE"; }

        return r1 + "," + fixed + "," + r2 + "," + view.get(1) + "," + set.contains(key) + "," + set.size() + "," + nums + "," + r3;
    }

    /** String API and the string pool. */
    public static String s6() {
        String lit = "java";
        String folded = "ja" + "va";
        String p = "ja";
        String runtime = p + "va";
        return "a.b".replaceAll(".", "x") + "," + "a.b".replace(".", "x") + "," + "a,b,,".split(",").length + ","
                + ",a".split(",").length + "," + "".split(",").length + "," + String.valueOf((Object) null) + ","
                + "abc".compareTo("abd") + "," + (lit == folded) + "," + (lit == runtime) + "," + (lit == runtime.intern());
    }
}
