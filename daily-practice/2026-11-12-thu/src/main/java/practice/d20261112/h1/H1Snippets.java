package practice.d20261112.h1;

import java.util.*;
import java.util.stream.*;

/** Given: do NOT run these before predicting. Each returns a log String. */
public final class H1Snippets {
    private H1Snippets() {}

    // ---- s1: when does a class get initialised? compile-time constants vs other statics
    static class Config {
        static final int LIMIT = 10;                 // constant variable
        static final Integer BOXED = 20;             // not a constant variable
        static final List<String> LOG = new ArrayList<>();
        static { LOG.add("Config.init"); }
        static int touch() { return 1; }
    }

    static class Lazy {
        static { Config.LOG.add("Lazy.init"); }
        static final String NAME = "lazy";           // constant
        static final String COMPUTED = String.valueOf(5);
    }

    public static String s1() {
        StringBuilder sb = new StringBuilder();
        sb.append(Lazy.NAME).append(' ');
        sb.append(Config.LOG).append(' ');
        sb.append(Lazy.COMPUTED).append(' ');
        sb.append(Config.LOG).append(' ');
        int limit = Config.LIMIT;
        sb.append(limit).append(' ');
        sb.append(Config.BOXED);
        return sb.toString();
    }

    // ---- s2: String identity, interning and compile-time constant folding
    public static String s2() {
        String a = "ja" + "va";
        String ja = "ja";
        String b = ja + "va";
        final String fja = "ja";
        String c = fja + "va";
        String d = new String("java");
        StringBuilder sb = new StringBuilder();
        sb.append(a == "java").append(' ').append(b == "java").append(' ').append(c == "java").append(' ')
                .append(d == "java").append(' ').append(d.intern() == "java").append(' ').append(b.equals(a)).append(' ')
                .append(("" + 1 + 2)).append(' ').append((1 + 2 + "")).append(' ').append('a' + 'b' + "c").append(' ').append("c" + 'a' + 'b');
        return sb.toString();
    }

    // ---- s3: arrays are covariant, generics are erased
    public static String s3() {
        StringBuilder sb = new StringBuilder();
        Object[] objs = new String[2];
        try { objs[0] = 1; } catch (ArrayStoreException e) { sb.append("ASE ").append(e.getMessage()).append(' '); }
        List<String> strings = new ArrayList<>();
        @SuppressWarnings({"unchecked", "rawtypes"})
        List<Object> raw = (List) strings;
        raw.add(42);
        sb.append(strings.size()).append(' ');
        try { String s = strings.get(0); sb.append(s); } catch (ClassCastException e) { sb.append("CCE "); }
        sb.append(new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass()).append(' ');
        List<? extends Number> nums = List.of(1, 2.5);
        Number first = nums.get(0);
        sb.append(first.getClass().getSimpleName()).append(' ').append(nums.get(1).getClass().getSimpleName());
        return sb.toString();
    }

    // ---- s4: collection pitfalls
    public static String s4() {
        StringBuilder sb = new StringBuilder();
        List<Integer> list = new ArrayList<>(List.of(10, 20, 30, 1, 2));
        list.remove(1);
        list.remove(Integer.valueOf(1));
        sb.append(list).append(' ');
        List<String> fixed = Arrays.asList("a", "b", "c");
        fixed.set(0, "z");
        try { fixed.add("d"); } catch (UnsupportedOperationException e) { sb.append("UOE "); }
        sb.append(fixed).append(' ');
        List<Integer> base = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> view = base.subList(1, 4);
        view.set(0, 99);
        base.set(4, 55);
        sb.append(base).append(' ');
        try { for (Integer x : base) if (x == 99) base.remove(x); } catch (ConcurrentModificationException e) { sb.append("CME "); }
        List<Integer> two = new ArrayList<>(List.of(1, 2, 3));
        for (Integer x : two) if (x == 2) two.remove(x);          // removing the second-to-last element
        sb.append(two).append(' ');
        try { List.of(1).contains(null); sb.append("ok "); } catch (NullPointerException e) { sb.append("NPE "); }
        Map<String, Integer> m = new HashMap<>();
        m.put("k", null);
        sb.append(m.containsKey("k")).append(' ').append(m.getOrDefault("k", 7)).append(' ').append(m.computeIfAbsent("k", k -> 8)).append(' ').append(m.merge("k", 5, Integer::sum));
        return sb.toString();
    }

    // ---- s5: lazy streams, eager arguments, lambdas capturing state
    public static String s5() {
        List<String> log = new ArrayList<>();
        Stream<Integer> st = Stream.of(1, 2, 3, 4).peek(x -> log.add("p" + x)).filter(x -> x % 2 == 0).map(x -> { log.add("m" + x); return x * 10; });
        log.add("built");
        Optional<Integer> first = st.findFirst();
        log.add("first=" + first.get());
        Optional<String> some = Optional.of("v");
        String r1 = some.orElse(compute(log, "eager"));
        String r2 = some.orElseGet(() -> compute(log, "lazy"));
        List<Supplier2> sups = new ArrayList<>();
        for (int i = 0; i < 3; i++) { int copy = i * i; sups.add(() -> copy); }
        int[] counter = {0};
        Runnable inc = () -> counter[0]++;
        inc.run(); inc.run();
        log.add(r1 + r2 + sups.stream().map(s -> String.valueOf(s.get())).collect(Collectors.joining("")) + counter[0]);
        return String.join(",", log);
    }
    interface Supplier2 { int get(); }
    private static String compute(List<String> log, String tag) { log.add("compute-" + tag); return tag; }

    // ---- s6: try-with-resources, close order, suppressed exceptions
    static class Res implements AutoCloseable {
        final String name; final List<String> log; final boolean failClose;
        Res(String name, List<String> log, boolean failClose) { this.name = name; this.log = log; this.failClose = failClose; log.add("open " + name); }
        @Override public void close() { log.add("close " + name); if (failClose) throw new IllegalStateException("close-" + name); }
    }

    public static String s6() {
        List<String> log = new ArrayList<>();
        try (Res a = new Res("A", log, true); Res b = new Res("B", log, true)) {
            log.add("body");
            throw new RuntimeException("body-fail");
        } catch (RuntimeException e) {
            log.add(e.getMessage() + " suppressed=" + Arrays.stream(e.getSuppressed()).map(Throwable::getMessage).toList());
        } finally {
            log.add("finally");
        }
        try (Res c = new Res("C", log, true)) {
            log.add("body2");
        } catch (RuntimeException e) {
            log.add(e.getMessage() + " suppressed=" + e.getSuppressed().length);
        }
        return String.join(" | ", log);
    }
}
