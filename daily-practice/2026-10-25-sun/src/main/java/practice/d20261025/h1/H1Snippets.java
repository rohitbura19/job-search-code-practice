package practice.d20261025.h1;

import java.util.*;
import java.util.stream.*;

/** The snippets. Do NOT run them before predicting. Each returns a String that H1Predictions must reproduce exactly. */
public final class H1Snippets {
    private H1Snippets() {}

    /** (1) conditional-operator typing, switch fall-through, char in a switch */
    public static String s1() {
        StringBuilder sb = new StringBuilder();
        Object o1 = true ? 1 : "x";
        Object o2 = true ? 1 : 2.0;
        Object o3 = false ? 'a' : 98;
        char c = 'a';
        int i = 0;
        Object o4 = true ? c : i;
        sb.append(o1).append('|').append(o2).append('|').append(o3).append('|').append(o4).append('|');
        for (int k = 0; k < 4; k++) {
            switch (k) {
                case 0: sb.append('A');
                case 1: sb.append('B'); break;
                case 2: sb.append('C');
                default: sb.append('D');
            }
        }
        return sb.toString();
    }

    /** (2) list views: Arrays.asList, subList, unmodifiableList, List.copyOf */
    public static String s2() {
        StringBuilder sb = new StringBuilder();
        String[] arr = {"a", "b", "c"};
        List<String> view = Arrays.asList(arr);
        view.set(0, "z");
        sb.append(arr[0]).append(' ');
        try { view.add("d"); } catch (UnsupportedOperationException e) { sb.append("UOE "); }
        arr[1] = "y";
        sb.append(view).append(' ');
        List<Integer> base = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> sub = base.subList(1, 4);
        sub.set(0, 20);
        sub.remove(Integer.valueOf(3));
        sb.append(base).append(' ');
        List<Integer> ro = Collections.unmodifiableList(base);
        List<Integer> copy = List.copyOf(base);
        base.add(6);
        sb.append(ro.size()).append(copy.size()).append(' ');
        base.add(7);
        try { sub.size(); } catch (ConcurrentModificationException e) { sb.append("CME"); }
        return sb.toString();
    }

    /** (3) finally vs return, try-with-resources order and suppressed exceptions */
    @SuppressWarnings("finally")
    static int f() {
        try { return 1; } finally { return 2; }
    }

    static int g() {
        int x = 1;
        try { return x; } finally { x = 99; }
    }

    static class Res implements AutoCloseable {
        final String name;
        final StringBuilder log;
        Res(String name, StringBuilder log) { this.name = name; this.log = log; log.append("open").append(name).append(' '); }
        @Override public void close() { log.append("close").append(name).append(' '); throw new IllegalStateException("close" + name); }
    }

    public static String s3() {
        StringBuilder sb = new StringBuilder();
        sb.append(f()).append(g()).append(' ');
        try (Res a = new Res("A", sb); Res b = new Res("B", sb)) {
            sb.append("body ");
            throw new RuntimeException("body");
        } catch (RuntimeException e) {
            sb.append(e.getMessage()).append(' ');
            for (Throwable t : e.getSuppressed()) sb.append(t.getMessage()).append(' ');
        }
        return sb.toString().trim();
    }

    /** (4) overload resolution: boxing vs widening vs varargs, null argument */
    static String m(long x) { return "long"; }
    static String m(Integer x) { return "Integer"; }
    static String m(int... x) { return "varargs"; }
    static String n(Object o) { return "Object"; }
    static String n(String s) { return "String"; }
    static String p(double d) { return "double"; }
    static String p(Object o) { return "Object"; }

    public static String s4() {
        byte b = 1;
        short sh = 2;
        char ch = 'c';
        return m(5) + " " + m(b) + " " + m(Integer.valueOf(5)) + " " + m() + " " + n(null) + " " + n((Object) "s") + " "
                + p(5) + " " + p(Integer.valueOf(5)) + " " + p(sh) + " " + p(ch) + " " + p('c' + 1);
    }

    /** (5) floating point and narrowing */
    public static String s5() {
        double nan = Double.NaN;
        return (0.1 + 0.2 == 0.3) + " " + (0.1f + 0.2f == 0.3f) + " " + (nan == nan) + " " + Double.valueOf(nan).equals(Double.NaN) + " "
                + (0.0 == -0.0) + " " + Double.valueOf(0.0).equals(-0.0) + " " + (int) 3.99e10 + " " + (int) -2.7 + " " + (byte) 200 + " "
                + (char) 66 + (int) 'B' + " " + (long) Float.MAX_VALUE + " " + Math.round(-2.5) + " " + Math.round(2.5) + " " + 5 / 2.0 + " "
                + (1 / 0.0) + " " + Integer.MAX_VALUE * 2 + " " + (Integer.MAX_VALUE + 1L);
    }

    /** (6) stream laziness and statefulness */
    public static String s6() {
        StringBuilder sb = new StringBuilder();
        List<Integer> out = Stream.of(5, 4, 3, 2, 1)
                .peek(x -> sb.append('p').append(x))
                .filter(x -> x % 2 == 1)
                .map(x -> { sb.append('m').append(x); return x * 10; })
                .limit(2)
                .collect(Collectors.toList());
        sb.append(' ').append(out).append(' ');
        Stream<Integer> lazy = Stream.of(1, 2, 3).peek(x -> sb.append('L').append(x));
        sb.append("built ");
        sb.append(lazy.anyMatch(x -> x == 2)).append(' ');
        Stream<Integer> s = Stream.of(1, 2);
        s.count();
        try { s.count(); } catch (IllegalStateException e) { sb.append("ISE"); }
        return sb.toString();
    }
}
