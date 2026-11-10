package practice.d20261110.h1;

import java.util.*;
import java.util.stream.*;

/** Given: do NOT run these before predicting. Each returns a log String. */
public final class H1Snippets {
    private H1Snippets() {}

    // ---- s1: fields are not virtual, static methods are hidden, overloads resolve at compile time
    static class Animal {
        String name = "animal";
        static String kind() { return "Animal.kind"; }
        String speak() { return "..."; }
        String greet(Animal a) { return "Animal-Animal"; }
        String greet(Dog d) { return "Animal-Dog"; }
    }

    static class Dog extends Animal {
        String name = "dog";
        static String kind() { return "Dog.kind"; }
        @Override String speak() { return "woof"; }
        @Override String greet(Animal a) { return "Dog-Animal"; }
    }

    public static String s1() {
        Animal a = new Dog();
        Dog d = new Dog();
        Animal plain = new Animal();
        return a.name + " " + d.name + " " + ((Animal) d).name + " " + a.speak() + " " + a.kind() + " " + d.kind()
                + " " + a.greet(d) + " " + a.greet(a) + " " + plain.greet(d) + " " + d.greet((Animal) d);
    }

    // ---- s2: String identity, the pool and compile-time constants
    public static String s2() {
        String a = "ja" + "va";
        String j = "ja";
        String b = j + "va";
        final String fj = "ja";
        String c = fj + "va";
        String d = new String("java");
        StringBuilder sb = new StringBuilder();
        sb.append(a == "java").append(' ').append(b == "java").append(' ').append(c == "java").append(' ')
                .append(d == "java").append(' ').append(d.intern() == "java").append(' ').append(b.equals("java")).append(' ')
                .append((b.intern() == a));
        String s = "x";
        s += 1 + 2;
        s += 'c' + 1;
        sb.append(' ').append(s).append(' ').append(1 + 2 + "z" + 1 + 2).append(' ').append((char) ('a' + 1)).append(' ').append('a' + 1);
        return sb.toString();
    }

    // ---- s3: integer arithmetic corner cases
    public static String s3() {
        int i = 5;
        int r1 = i++ + ++i;                      // 5 + 7
        int r2 = i-- - --i;
        byte b = (byte) 200;
        short sh = (short) 40000;
        byte shifted = (byte) (b >>> 4);
        int neg = -17;
        StringBuilder sb = new StringBuilder();
        sb.append(r1).append(' ').append(r2).append(' ').append(i).append(' ')
                .append(b).append(' ').append(sh).append(' ').append(shifted).append(' ')
                .append(neg % 5).append(' ').append(Math.floorMod(neg, 5)).append(' ').append(neg / 5).append(' ').append(neg >> 1).append(' ').append(neg >>> 28).append(' ')
                .append(Math.abs(Integer.MIN_VALUE)).append(' ').append(Integer.MAX_VALUE + 1).append(' ').append((long) Integer.MAX_VALUE + 1).append(' ')
                .append(0.1 + 0.2 == 0.3).append(' ').append((int) 3.99).append(' ').append((int) -3.99).append(' ').append(Math.round(-2.5)).append(' ').append(Math.round(2.5)).append(' ')
                .append(1 / 2 * 2.0).append(' ').append(5 / 2.0);
        byte x = 10;
        x += 300;                                // compound assignment narrows silently
        sb.append(' ').append(x);
        return sb.toString();
    }

    // ---- s4: lazy streams, ordering and short-circuiting
    public static String s4() {
        List<String> log = new ArrayList<>();
        List<Integer> out = Stream.of(5, 3, 8, 1, 9)
                .peek(x -> log.add("p" + x))
                .filter(x -> x > 2)
                .sorted()
                .peek(x -> log.add("s" + x))
                .limit(2)
                .toList();
        List<String> log2 = new ArrayList<>();
        Optional<Integer> first = Stream.of(1, 2, 3, 4).peek(x -> log2.add("a" + x)).map(x -> x * 10).peek(x -> log2.add("b" + x)).filter(x -> x > 15).findFirst();
        Stream<Integer> never = Stream.of(1, 2).peek(x -> log2.add("never" + x));   // no terminal operation
        long count = IntStream.range(0, 5).boxed().peek(x -> log2.add("c" + x)).count();    // since 9 the size is computed without traversal
        return out + " " + log + " | " + first.get() + " " + log2 + " " + count;
    }

    // ---- s5: try-with-resources order and suppressed exceptions
    static class Res implements AutoCloseable {
        final String name; final boolean failOnClose; final List<String> log;
        Res(String name, boolean failOnClose, List<String> log) { this.name = name; this.failOnClose = failOnClose; this.log = log; log.add("open " + name); }
        @Override public void close() { log.add("close " + name); if (failOnClose) throw new IllegalStateException("close-" + name); }
    }

    public static String s5() {
        List<String> log = new ArrayList<>();
        String result;
        try (Res a = new Res("A", true, log); Res b = new Res("B", true, log)) {
            log.add("body");
            throw new RuntimeException("body-fail");
        } catch (RuntimeException e) {
            result = e.getMessage() + " suppressed=" + Arrays.stream(e.getSuppressed()).map(Throwable::getMessage).toList();
        } finally {
            log.add("finally");
        }
        List<String> log2 = new ArrayList<>();
        String result2;
        try (Res c = new Res("C", true, log2)) {
            log2.add("ok-body");
            result2 = "returned";
        } catch (IllegalStateException e) {
            result2 = "caught " + e.getMessage() + " suppressed=" + e.getSuppressed().length;
        }
        return log + " " + result + " | " + log2 + " " + result2;
    }

    // ---- s6: collection views and removal
    public static String s6() {
        StringBuilder sb = new StringBuilder();
        List<Integer> nums = new ArrayList<>(List.of(10, 20, 30, 1, 2));
        nums.remove(1);                               // index
        nums.remove(Integer.valueOf(1));              // object
        sb.append(nums).append(' ');
        String[] arr = {"a", "b", "c"};
        List<String> view = Arrays.asList(arr);
        view.set(0, "z");
        arr[1] = "y";
        sb.append(Arrays.toString(arr)).append(view).append(' ');
        try { view.add("d"); } catch (UnsupportedOperationException e) { sb.append("UOE "); }
        List<Integer> base = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> sub = base.subList(1, 4);
        sub.set(0, 99);
        sub.remove(Integer.valueOf(3));
        sb.append(base).append(sub).append(' ');
        base.add(6);
        try { sub.size(); } catch (ConcurrentModificationException e) { sb.append("CME "); }
        List<Integer> ro = Collections.unmodifiableList(base);
        base.add(7);
        sb.append(ro.size()).append(' ');
        try { List.of(1, 2).contains(null); } catch (NullPointerException e) { sb.append("NPE "); }
        Map<String, Integer> m = new HashMap<>();
        m.put(null, 1);
        sb.append(m.get(null)).append(' ').append(m.merge("k", 5, Integer::sum)).append(m.merge("k", 5, Integer::sum)).append(' ').append(m.computeIfPresent("none", (k, v) -> v + 1));
        return sb.toString();
    }
}
