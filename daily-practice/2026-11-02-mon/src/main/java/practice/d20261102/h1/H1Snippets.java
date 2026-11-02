package practice.d20261102.h1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.stream.Stream;

/** Given: do not edit. Each sN() returns a String; predict it WITHOUT running. */
public final class H1Snippets {
    private H1Snippets() {}

    static class Animal {
        String name = "animal";
        static String kind() { return "Animal"; }
        String speak() { return "..."; }
        String who() { return name + "/" + kind() + "/" + speak(); }
    }

    static class Dog extends Animal {
        String name = "dog";
        static String kind() { return "Dog"; }
        @Override String speak() { return "woof"; }
    }

    static String f(Object o) { return "Object"; }
    static String f(String s) { return "String"; }
    static String f(Integer i) { return "Integer"; }
    static String f(long l) { return "long"; }
    static String f(Object... os) { return "varargs"; }
    static String g(long l) { return "long"; }
    static String g(Integer i) { return "Integer"; }
    static String g(int... is) { return "varargs"; }

    /** Field hiding, static hiding, overriding, overload resolution. */
    public static String s1() {
        Animal a = new Dog();
        Dog d = new Dog();
        Object o = "text";
        return a.name + "," + d.name + "," + ((Animal) d).name + "," + a.who() + "," + d.who() + ","
                + f(o) + "," + f("s") + "," + f(1) + "," + f(1L) + "," + f('c') + "," + f(1.5) + "," + f() + "," + g(5);
    }

    static class Res implements AutoCloseable {
        final String id;
        final List<String> log;
        final boolean failOnClose;
        Res(String id, List<String> log, boolean failOnClose) { this.id = id; this.log = log; this.failOnClose = failOnClose; log.add("open " + id); }
        @Override public void close() {
            log.add("close " + id);
            if (failOnClose) throw new IllegalStateException("close-" + id);
        }
    }

    /** try-with-resources order, suppressed exceptions, finally. */
    public static String s2() {
        List<String> log = new ArrayList<>();
        String outcome;
        try (Res a = new Res("A", log, true); Res b = new Res("B", log, true)) {
            log.add("body");
            throw new RuntimeException("body-fail");
        } catch (RuntimeException e) {
            List<String> sup = new ArrayList<>();
            for (Throwable t : e.getSuppressed()) sup.add(t.getMessage());
            outcome = e.getMessage() + sup;
        } finally {
            log.add("finally");
        }
        return log + " " + outcome;
    }

    /** Stream laziness and evaluation order. */
    public static String s3() {
        List<String> log = new ArrayList<>();
        List<Integer> r1 = Stream.of(5, 4, 3, 2, 1)
                .peek(x -> log.add("p" + x))
                .filter(x -> x % 2 == 1)
                .map(x -> x * 10)
                .limit(2)
                .toList();
        List<String> log2 = new ArrayList<>();
        Stream.of(3, 1, 2).peek(x -> log2.add("a" + x)).sorted().peek(x -> log2.add("b" + x)).findFirst();
        List<Integer> src = new ArrayList<>(List.of(1, 2));
        Stream<Integer> lazy = src.stream().map(x -> x * 2);
        src.add(3);
        return r1 + " " + log + " " + log2 + " " + lazy.toList();
    }

    /** Floating point, char arithmetic, integer corner cases. */
    public static String s4() {
        double nan = Double.NaN, negZero = -0.0;
        return (nan == nan) + "," + Double.valueOf(nan).equals(nan) + "," + (negZero == 0.0) + "," + Double.valueOf(negZero).equals(0.0) + ","
                + Math.round(-2.5) + "," + Math.round(2.5) + "," + Math.round(-2.51) + "," + (char) ('a' + 2) + "," + ('a' + 2) + ","
                + (int) 'A' + "," + (5 / 2.0) + "," + (5 / 2) + "," + Math.floorMod(-7, 3) + "," + (-7 % 3) + "," + (Integer.MIN_VALUE == -Integer.MIN_VALUE)
                + "," + (0.1f + 0.2f == 0.3f) + "," + (1.0 / 0) + "," + (long) 1e19 + "," + (byte) 200;
    }

    /** Collections: views, fail-fast iteration, ordering structures. */
    public static String s5() {
        List<Integer> base = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> sub = base.subList(1, 4);
        sub.set(0, 20);
        base.set(3, 40);
        String r1 = sub.toString() + base;

        String r2;
        try { sub.add(99); base.add(6); sub.size(); r2 = "ok"; } catch (java.util.ConcurrentModificationException e) { r2 = "CME"; }

        List<Integer> ev = new ArrayList<>(List.of(1, 2, 3, 4));
        for (Iterator<Integer> it = ev.iterator(); it.hasNext(); ) if (it.next() % 2 == 0) it.remove();

        List<Integer> trap = new ArrayList<>(List.of(1, 2, 3));
        String r3;
        try { for (Integer x : trap) if (x == 2) trap.remove(x); r3 = "no-exception " + trap; } catch (java.util.ConcurrentModificationException e) { r3 = "CME"; }

        PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(5, 1, 4, 2, 3));
        StringBuilder polled = new StringBuilder();
        while (!pq.isEmpty()) polled.append(pq.poll());

        TreeMap<String, Integer> byLen = new TreeMap<>(Comparator.comparingInt(String::length));
        byLen.put("aa", 1); byLen.put("bb", 2); byLen.put("c", 3);
        return r1 + " " + r2 + " " + ev + " " + r3 + " " + polled + " " + byLen;
    }

    sealed interface Shape permits Circle, Square, Rect {}
    record Circle(int r) implements Shape {}
    record Square(int side) implements Shape {}
    record Rect(int w, int h) implements Shape {}
    record Holder(int[] values, String label) {}
    enum Level { LOW, MID, HIGH }

    static String describe(Object o) {
        return switch (o) {
            case Circle c when c.r() > 10 -> "big circle";
            case Circle c -> "circle " + c.r();
            case Rect(int w, int h) when w == h -> "square-ish " + w;
            case Rect(int w, int h) -> "rect " + (w * h);
            case Square s -> "square " + s.side();
            case Level l -> "level " + l.ordinal();
            case null -> "null!";
            default -> "other";
        };
    }

    static int fall(int n) {
        int acc = 0;
        switch (n) {
            case 1: acc += 1;
            case 2: acc += 10;
            case 3: acc += 100; break;
            default: acc += 1000;
            case 4: acc += 10000;
        }
        return acc;
    }

    /** Records, enums, pattern-matching switch, fall-through. */
    public static String s6() {
        int[] arr = {1, 2};
        Holder h1 = new Holder(arr, "x"), h2 = new Holder(arr, "x"), h3 = new Holder(new int[]{1, 2}, "x");
        return new Rect(2, 3) + "," + new Circle(5).equals(new Circle(5)) + "," + h1.equals(h2) + "," + h1.equals(h3) + ","
                + describe(new Circle(11)) + "," + describe(new Circle(3)) + "," + describe(new Rect(4, 4)) + "," + describe(new Rect(2, 5)) + ","
                + describe(new Square(7)) + "," + describe(Level.HIGH) + "," + describe(null) + "," + describe("s") + ","
                + fall(1) + "," + fall(2) + "," + fall(3) + "," + fall(4) + "," + fall(9) + ","
                + Level.valueOf("MID").compareTo(Level.HIGH) + "," + Level.values().length;
    }
}
