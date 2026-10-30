package practice.d20261030.h1;

import java.util.*;
import java.util.stream.*;

/** The snippets. Do NOT run them before predicting. Each returns a String that H1Predictions must reproduce exactly. */
public final class H1Snippets {
    private H1Snippets() {}

    /** (1) class initialisation order, a virtual call from a constructor, static vs instance init */
    static final StringBuilder LOG = new StringBuilder();

    static class Base {
        static { LOG.append("Bs "); }
        { LOG.append("Bi "); }
        Base() { LOG.append("Bc "); init(); }
        void init() { LOG.append("Binit "); }
    }

    static class Derived extends Base {
        static { LOG.append("Ds "); }
        private String name = "set";
        private int n;
        { LOG.append("Di "); }
        Derived() { super(); LOG.append("Dc:" + name + n + " "); }
        @Override void init() { LOG.append("Dinit:" + name + n + " "); n = 5; }
    }

    public static String s1() {
        LOG.setLength(0);
        new Derived();
        LOG.append("| ");
        new Derived();
        return LOG.toString();
    }

    /** (2) numeric corners: compound assignment, char arithmetic, integer overflow, division and remainder signs, shifts, casts */
    public static String s2() {
        StringBuilder sb = new StringBuilder();
        byte b = 120;
        b += 10;
        char c = 'a';
        c += 2;
        short sh = (short) 40000;
        sb.append(b).append(' ').append(c).append(' ').append(c + 1).append(' ').append((char) (c + 1)).append(' ').append(sh).append(' ');
        sb.append(Integer.MAX_VALUE + 1).append(' ').append(Math.abs(Integer.MIN_VALUE)).append(' ');
        sb.append(-7 / 2).append(' ').append(-7 % 3).append(' ').append(Math.floorMod(-7, 3)).append(' ').append(7 % -3).append(' ');
        sb.append(-1 >>> 28).append(' ').append(-16 >> 2).append(' ').append(1 << 33).append(' ').append(1L << 33).append(' ');
        sb.append((int) 3.99).append(' ').append((int) -3.99).append(' ').append((int) 1e20).append(' ').append((int) Double.NaN).append(' ');
        sb.append(0.1 + 0.2 == 0.3).append(' ').append(0.1f + 0.2f == 0.3f).append(' ').append(1 / 2 * 2.0).append(' ').append(1 / 2.0 * 2);
        return sb.toString();
    }

    /** (3) equals/hashCode contract, String identity, Arrays.asList on a primitive array */
    static class Key {
        final int id;
        Key(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof Key k && k.id == id; }
    }

    static class Id {
        final int id;
        Id(int id) { this.id = id; }
        @Override public boolean equals(Object o) { return o instanceof Id k && k.id == id; }
        @Override public int hashCode() { return id % 2; }
    }

    public static String s3() {
        StringBuilder sb = new StringBuilder();
        Set<Key> noHash = new HashSet<>();
        noHash.add(new Key(1));
        noHash.add(new Key(1));
        Set<Id> weakHash = new HashSet<>();
        weakHash.add(new Id(1));
        weakHash.add(new Id(3));
        weakHash.add(new Id(1));
        String a = "ja" + "va", x = "ja", c = x + "va";
        sb.append(noHash.size()).append(' ').append(weakHash.size()).append(' ').append(weakHash.contains(new Id(3))).append(' ');
        sb.append(a == "java").append(' ').append(c == "java").append(' ').append(c.equals("java")).append(' ').append(c.intern() == "java").append(' ');
        sb.append(Arrays.asList(new int[] {1, 2, 3}).size()).append(' ').append(Arrays.asList(new Integer[] {1, 2, 3}).size()).append(' ');
        sb.append(new Key(1).equals(new Key(1))).append(' ').append(List.of(new Key(1)).contains(new Key(1))).append(' ');
        sb.append(new StringBuilder("a").equals(new StringBuilder("a"))).append(' ').append("a".contentEquals(new StringBuilder("a")));
        return sb.toString();
    }

    /** (4) collections: fixed-size and immutable lists, null handling, PriorityQueue order, view semantics */
    public static String s4() {
        StringBuilder sb = new StringBuilder();
        List<Integer> fixed = Arrays.asList(3, 1, 2);
        fixed.set(0, 9);
        try { fixed.add(4); } catch (UnsupportedOperationException e) { sb.append("UOE "); }
        List<Integer> src = new ArrayList<>(List.of(1, 2, 3));
        List<Integer> view = Collections.unmodifiableList(src);
        src.add(4);
        sb.append(view).append(' ').append(fixed).append(' ');
        try { List.of(1).contains(null); sb.append("ok "); } catch (NullPointerException e) { sb.append("NPE "); }
        try { new TreeMap<String, Integer>().put(null, 1); sb.append("ok "); } catch (NullPointerException e) { sb.append("NPE "); }
        Map<String, Integer> hm = new HashMap<>();
        hm.put(null, 1);
        hm.merge(null, 10, Integer::sum);
        sb.append(hm).append(' ');
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int v : new int[] {5, 1, 4, 2, 3}) pq.add(v);
        StringBuilder polled = new StringBuilder();
        while (!pq.isEmpty()) polled.append(pq.poll());
        sb.append(new PriorityQueue<>(List.of(5, 1, 4, 2, 3))).append(' ').append(polled).append(' ');
        Deque<Integer> dq = new ArrayDeque<>();
        dq.push(1); dq.addLast(2); dq.offerFirst(3); dq.add(4);
        sb.append(dq).append(' ').append(dq.pop()).append(dq.pollLast());
        return sb.toString();
    }

    /** (5) exceptions: finally, return values, which exception wins, catch order */
    @SuppressWarnings("finally")
    static int f1() { try { return 1; } finally { return 2; } }

    static int f2() {
        int x = 1;
        try { return x; } finally { x = 99; }
    }

    @SuppressWarnings("finally")
    static String f3() {
        try {
            try { throw new IllegalStateException("inner"); }
            finally { throw new IllegalArgumentException("from finally"); }
        } catch (IllegalStateException e) {
            return "ISE " + e.getMessage();
        } catch (RuntimeException e) {
            return "RE " + e.getMessage();
        }
    }

    static String f4() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            try {
                if (i == 1) continue;
                sb.append("t").append(i);
            } finally {
                sb.append("f").append(i);
            }
        }
        return sb.toString();
    }

    static String f5() {
        try {
            Object o = "str";
            Integer n = (Integer) o;
            return "no " + n;
        } catch (ClassCastException | NullPointerException e) {
            return e.getClass().getSimpleName();
        }
    }

    public static String s5() {
        return f1() + " " + f2() + " " + f3() + " " + f4() + " " + f5();
    }

    /** (6) stream laziness and evaluation order */
    public static String s6() {
        StringBuilder sb = new StringBuilder();
        List<Integer> out = Stream.of(1, 2, 3, 4, 5, 6)
                .peek(i -> sb.append("p").append(i).append(' '))
                .filter(i -> { sb.append("f").append(i).append(' '); return i % 2 == 0; })
                .map(i -> { sb.append("m").append(i).append(' '); return i * 10; })
                .limit(2)
                .toList();
        sb.append(out).append(" | ");
        Stream<Integer> lazy = Stream.of(1, 2, 3).peek(i -> sb.append("never"));
        sb.append("built | ");
        Optional<Integer> first = Stream.of(5, 6, 7).sorted(Comparator.reverseOrder()).peek(i -> sb.append("s").append(i).append(' ')).findFirst();
        sb.append(first.get()).append(" | ");
        sb.append(IntStream.iterate(1, i -> i * 2).limit(5).sum()).append(' ');
        sb.append(Stream.of("b", "a", "c").sorted().reduce("", String::concat)).append(' ');
        sb.append(Stream.of(1, 2, 3, 4).reduce(10, (a, b2) -> a - b2));
        return sb.toString();
    }
}
