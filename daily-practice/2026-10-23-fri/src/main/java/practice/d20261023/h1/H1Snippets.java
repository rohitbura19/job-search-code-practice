package practice.d20261023.h1;

import java.util.*;
import java.util.stream.Stream;

/** Read each snippet, predict what it returns, write the String in H1Predictions. Do not run it first. */
public final class H1Snippets {
    private H1Snippets() {}

    // ---- 1: try-with-resources
    static final List<String> EV = new ArrayList<>();
    static final class Res implements AutoCloseable {
        final String name;
        Res(String name) { this.name = name; EV.add("open " + name); }
        @Override public void close() { EV.add("close " + name); throw new IllegalStateException("close-" + name); }
    }
    public static String s1() {
        EV.clear();
        try (Res a = new Res("a"); Res b = new Res("b")) {
            EV.add("body");
            throw new RuntimeException("body-ex");
        } catch (RuntimeException e) {
            Throwable[] sup = e.getSuppressed();
            EV.add("catch " + e.getMessage() + " " + sup.length + " " + sup[0].getMessage() + " " + sup[1].getMessage());
        } finally {
            EV.add("finally");
        }
        return String.join("|", EV);
    }

    // ---- 2: ternary typing
    public static String s2() {
        Object o1 = true ? 1 : 2.0;
        Object o2 = true ? (Object) 1 : 2.0;
        Integer n = null;
        String r3;
        try { int v = false ? 1 : n; r3 = "v" + v; } catch (NullPointerException e) { r3 = "NPE"; }
        char c = 'a';
        int i = 0;
        Object o4 = true ? c : 0;
        Object o5 = true ? c : i;
        return o1 + "," + o2 + "," + r3 + "," + o4 + "," + o5;
    }

    // ---- 3: mutable hash key
    public static String s3() {
        Set<List<Integer>> set = new HashSet<>();
        List<Integer> k = new ArrayList<>(List.of(1));
        set.add(k);
        k.add(2);
        return set.contains(k) + "," + set.contains(List.of(1, 2)) + "," + set.contains(List.of(1)) + "," + set.size()
                + "," + set.stream().anyMatch(x -> x.equals(List.of(1, 2)));
    }

    // ---- 4: stream laziness
    public static String s4() {
        List<String> log = new ArrayList<>();
        List<Integer> r = Stream.of(1, 2, 3, 4, 5)
                .peek(x -> log.add("p" + x))
                .filter(x -> x % 2 == 1)
                .map(x -> { log.add("m" + x); return x * 10; })
                .limit(2)
                .toList();
        return r + " " + log;
    }

    // ---- 5: hiding vs overriding
    static class P {
        String name = "P";
        static String s() { return "Ps"; }
        String i() { return "Pi"; }
        String who() { return name; }
    }
    static class C extends P {
        String name = "C";
        static String s() { return "Cs"; }
        @Override String i() { return "Ci"; }
    }
    @SuppressWarnings("static-access")
    public static String s5() {
        P p = new C();
        C c = (C) p;
        return p.name + "," + c.name + "," + ((P) c).name + "," + p.who() + "," + p.s() + "," + c.s() + "," + p.i();
    }

    // ---- 6: views and fixed-size lists
    public static String s6() {
        List<Integer> base = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> sub = base.subList(1, 4);
        sub.set(0, 20);
        sub.remove(Integer.valueOf(3));
        Integer[] arr = {3, 1, 2};
        List<Integer> view = Arrays.asList(arr);
        view.set(0, 9);
        String r1;
        try { view.add(4); r1 = "added"; } catch (UnsupportedOperationException e) { r1 = "UOE"; }
        String r2;
        try { List.of(1).set(0, 2); r2 = "set"; } catch (UnsupportedOperationException e) { r2 = "UOE"; }
        return base + " " + sub + " " + Arrays.toString(arr) + " " + r1 + " " + r2;
    }
}
