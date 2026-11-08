package practice.d20261108.h1;

import java.util.*;
import java.util.stream.*;

/** The real code. Read it, predict each result in H1Predictions, and let the test compare. Do not run it first. */
public final class H1Snippets {
    private H1Snippets() {}

    static StringBuilder log = new StringBuilder();

    static class Res implements AutoCloseable {
        final String name;
        final boolean failOnClose;
        Res(String name, boolean failOnClose) { this.name = name; this.failOnClose = failOnClose; log.append("open ").append(name).append(' '); }
        @Override public void close() {
            log.append("close ").append(name).append(' ');
            if (failOnClose) throw new IllegalStateException("close-" + name);
        }
    }

    /** try-with-resources: close order, suppressed exceptions, catch/finally order. */
    public static String s1() {
        log = new StringBuilder();
        try (Res a = new Res("a", true); Res b = new Res("b", true)) {
            log.append("body ");
            throw new RuntimeException("body-ex");
        } catch (RuntimeException e) {
            log.append("caught ").append(e.getMessage()).append(" suppressed=")
                    .append(Arrays.stream(e.getSuppressed()).map(Throwable::getMessage).toList()).append(' ');
        } finally {
            log.append("finally");
        }
        return log.toString();
    }

    /** Numeric promotion, narrowing, integer and floating point corner cases. */
    public static String s2() {
        Object o1 = true ? 1 : 2.0;
        Object o2 = true ? (Object) 1 : 2.0;
        char c = 'a';
        int i = c + 1;
        char d = (char) (c + 1);
        c += 1;
        byte b = (byte) 200;
        short sh = (short) 70_000;
        List<Object> out = List.of(o1, o2, i, d, c, b, sh,
                Math.abs(Integer.MIN_VALUE), -7 / 2, -7 % 2, Math.floorMod(-7, 2),
                0.1 + 0.2 == 0.3, Double.NaN == Double.NaN, Double.valueOf(Double.NaN).equals(Double.NaN),
                1 / 0.0, (int) 3.99e10, (int) -0.9, Integer.MAX_VALUE + 1, 5 / 2 * 2.0, 'a' + 'b', "" + 'a' + 'b');
        return out.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    /** List behaviour: remove overloads, views, fixed-size and immutable lists, structural modification. */
    public static String s3() {
        List<Integer> l = new ArrayList<>(List.of(10, 20, 30, 1, 2));
        l.remove(1);
        l.remove(Integer.valueOf(1));
        List<Integer> view = Collections.unmodifiableList(l);
        l.add(99);
        List<Integer> fixed = Arrays.asList(1, 2, 3);
        fixed.set(0, 9);
        String fixedAdd;
        try { fixed.add(4); fixedAdd = "added"; } catch (UnsupportedOperationException e) { fixedAdd = "UOE"; }
        String containsNull;
        try { containsNull = String.valueOf(List.of(1, 2).contains(null)); } catch (NullPointerException e) { containsNull = "NPE"; }
        int[] prim = {1, 2, 3};
        List<Integer> parent = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> sub = parent.subList(1, 4);
        sub.set(0, 20);
        parent.add(6);
        String subResult;
        try { subResult = String.valueOf(sub.size()); } catch (ConcurrentModificationException e) { subResult = "CME"; }
        return l + " " + view.size() + " " + fixed + " " + fixedAdd + " " + containsNull + " "
                + Arrays.asList(prim).size() + " " + parent + " " + subResult;
    }

    static class P {
        String name = "P";
        static String st() { return "P.st"; }
        String who() { return "P.who"; }
        String call() { return who() + "/" + name + "/" + st(); }
        private String priv() { return "P.priv"; }
        String callPriv() { return priv(); }
    }

    static class Q extends P {
        String name = "Q";
        static String st() { return "Q.st"; }
        @Override String who() { return "Q.who"; }
        String priv() { return "Q.priv"; }
    }

    static String f(Object o) { return "Object"; }
    static String f(String s) { return "String"; }
    static String h(long x) { return "long"; }
    static String h(Integer x) { return "Integer"; }
    static String h(Object x) { return "Object"; }
    static String h(int... x) { return "varargs"; }

    /** Field hiding vs overriding, static and private methods, overload resolution phases. */
    public static String s4() {
        P p = new Q();
        String fields = p.name + "," + ((Q) p).name;
        String calls = p.call() + "," + p.callPriv() + "," + ((Q) p).priv() + "," + p.st();
        Object str = "x";
        String ov = f("x") + "," + f(str) + "," + f(null) + "," + h(5) + "," + h((short) 5) + "," + h('a')
                + "," + h(Integer.valueOf(5)) + "," + h(5, 6) + "," + h(5L) + "," + h(5.0);
        return fields + " " + calls + " " + ov;
    }

    /** String and StringBuilder behaviour. */
    public static String s5() {
        List<Object> out = new ArrayList<>();
        out.add("a,b,,c,,".split(",").length);
        out.add(",a".split(",").length);
        out.add("".split(",").length);
        out.add("a1b22c".split("\\d").length);
        out.add("a.b".replace(".", "-"));
        out.add("a.b".replaceAll(".", "-"));
        out.add("hello".substring(1, 3) + "hello".substring(5).length());
        out.add(new StringBuilder("abc").insert(1, "XY").reverse().deleteCharAt(0).append(1).append('c').append((char) 100).toString());
        out.add("b".compareTo("abc"));
        out.add("apple".compareTo("app"));
        out.add("Java".chars().sum());
        out.add("x".repeat(3).equals("xxx") + "" + ("x".repeat(3) == "xxx"));
        return out.stream().map(String::valueOf).collect(Collectors.joining("|"));
    }

    /** Stream laziness: element-at-a-time pipelines, sorted() as a barrier, short-circuiting, single use. */
    public static String s6() {
        List<String> trace = new ArrayList<>();
        Optional<Integer> first = Stream.of(5, 3, 8, 1, 9)
                .peek(x -> trace.add("p" + x))
                .filter(x -> { trace.add("f" + x); return x > 4; })
                .map(x -> { trace.add("m" + x); return x * 2; })
                .findFirst();
        trace.add("=" + first.orElse(-1));
        Stream.of(3, 1, 2).peek(x -> trace.add("a" + x)).sorted().peek(x -> trace.add("b" + x)).findFirst();
        List<Integer> powers = Stream.iterate(1, x -> x * 2).filter(x -> x > 10).limit(2).toList();
        trace.add(powers.toString());
        Stream<Integer> reused = Stream.of(1, 2);
        reused.count();
        String second;
        try { reused.count(); second = "ok"; } catch (IllegalStateException e) { second = "ISE"; }
        trace.add(second);
        trace.add(IntStream.rangeClosed(1, 4).boxed().collect(Collectors.partitioningBy(x -> x % 2 == 0, Collectors.summingInt(x -> x))).toString());
        return String.join(" ", trace);
    }
}
