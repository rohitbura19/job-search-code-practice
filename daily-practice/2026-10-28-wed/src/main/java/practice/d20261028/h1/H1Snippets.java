package practice.d20261028.h1;

import java.util.*;

/** The snippets. Do NOT run them before predicting. Each returns a String that H1Predictions must reproduce exactly. */
public final class H1Snippets {
    private H1Snippets() {}

    static final StringBuilder LOG = new StringBuilder();

    static class Base {
        static { LOG.append("Bs "); }
        int v = init("Bf ");
        { LOG.append("Bi "); }
        Base() { LOG.append("Bc "); show(); }
        void show() { LOG.append("Bshow "); }
        static int init(String s) { LOG.append(s); return 7; }
    }

    static class Derived extends Base {
        static { LOG.append("Ds "); }
        int w = init("Df ");
        String name = "set";
        { LOG.append("Di "); }
        Derived() { super(); LOG.append("Dc "); }
        @Override void show() { LOG.append("Dshow(" + w + "," + name + ") "); }
    }

    /** (1) initialization order: static blocks once, field initialisers and instance blocks in textual order, overridable call from a constructor */
    public static String s1() {
        LOG.setLength(0);
        new Derived();
        LOG.append("| ");
        new Derived();
        return LOG.toString().trim();
    }

    /** (2) char/int arithmetic, compound assignment, shifts, division and remainder */
    public static String s2() {
        char c = 'a';
        c += 1;
        c++;
        byte b = 10;
        b += 300;
        int i = 5;
        i = i++ + ++i;
        int j = 3;
        j += j++ * 2;
        int k = 7;
        k = k++;
        return "" + c + (c + 1) + (char) (c + 1) + " " + b + " " + i + " " + j + " " + k + " " + ('a' + 'b') + ("" + 'a' + 'b') + " "
                + (1 << 33) + " " + (1L << 33) + " " + (-16 >> 2) + " " + (-16 >>> 28) + " "
                + (-17 / 5) + " " + (-17 % 5) + " " + Math.floorMod(-17, 5) + " " + (1 / 2 * 2.0) + " " + (1 / 2.0 * 2);
    }

    /** (3) collections: remove(int) vs remove(Object), Arrays.asList on an int[], Integer cache, mutated HashSet key, PriorityQueue order */
    public static String s3() {
        List<Integer> list = new ArrayList<>(List.of(10, 20, 30, 1));
        list.remove(1);
        list.remove(Integer.valueOf(1));
        Integer a = 127, b = 127, c = 128, d = 128;
        Set<List<Integer>> set = new HashSet<>();
        List<Integer> key = new ArrayList<>(List.of(1));
        set.add(key);
        key.add(2);
        PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(5, 1, 4, 2, 3));
        StringBuilder polled = new StringBuilder();
        while (!pq.isEmpty()) polled.append(pq.poll());
        Deque<Integer> dq = new ArrayDeque<>();
        dq.push(1); dq.push(2); dq.addLast(3);
        return list + " " + Arrays.asList(new int[] {1, 2}).size() + " " + Arrays.asList(1, 2).size() + " " + (a == b) + " " + (c == d) + " "
                + c.equals(d) + " " + set.contains(key) + " " + set.contains(List.of(1)) + " " + set.size() + " "
                + new PriorityQueue<>(List.of(5, 1, 4, 2, 3)) + " " + polled + " " + dq + " " + dq.pop() + dq.pollLast();
    }

    /** (4) Strings: split, pool and compile-time constants, compareTo, null */
    public static String s4() {
        String x = "ab", y = "a";
        final String fy = "a";
        String n = null;
        return "a,b,,".split(",").length + " " + "a,b,,".split(",", -1).length + " " + ",a".split(",").length + " "
                + ("ab" == "a" + "b") + " " + (x == y + "b") + " " + (x == fy + "b") + " " + (x == (y + "b").intern()) + " "
                + "abc".compareTo("abd") + " " + "b".compareTo("abc") + " " + "ab".compareTo("abcd") + " "
                + n + 1 + " " + String.valueOf((Object) null).length() + " " + "  hi ".strip().length() + " " + "x".repeat(0).isEmpty() + " "
                + "Hello".chars().filter(Character::isUpperCase).count() + " " + "a-b-c".replace('-', '+') + " " + "a.b.c".replaceAll(".", "x");
    }

    /** (5) generics and arrays: erasure, covariance, heap pollution */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String s5() {
        StringBuilder sb = new StringBuilder();
        List<String> strings = new ArrayList<>();
        List<Integer> ints = new ArrayList<>();
        sb.append(strings.getClass() == ints.getClass()).append(' ');
        List raw = strings;
        raw.add(42);
        try {
            String s = strings.get(0);
            sb.append("no CCE ").append(s);
        } catch (ClassCastException e) {
            sb.append("CCE ");
        }
        sb.append(strings.size()).append(' ');
        Object[] objs = new Integer[2];
        try { objs[0] = "s"; sb.append("stored "); } catch (ArrayStoreException e) { sb.append("ASE "); }
        List<? extends Number> nums = List.of(1, 2.5);
        Number first = nums.get(0);
        sb.append(first.getClass().getSimpleName()).append(' ');
        sb.append(new int[0].getClass().getSimpleName()).append(' ').append(int[].class == new int[1].getClass()).append(' ');
        sb.append(Arrays.asList(1, 2, 3).getClass().getSimpleName()).append(' ').append(List.of(1).getClass() == List.of(1, 2).getClass());
        return sb.toString();
    }

    /** (6) control flow: switch fall-through, switch expression, labeled break/continue, short-circuit, i = i++ in a loop */
    public static String s6() {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < 4; n++) {
            switch (n) {
                case 0: sb.append('a');
                case 1: sb.append('b'); break;
                default: sb.append('d');
                case 3: sb.append('c');
            }
        }
        sb.append(' ');
        int day = 3;
        String kind = switch (day) { case 1, 7 -> "weekend"; case 3 -> { String t = "mid"; yield t + "week"; } default -> "other"; };
        sb.append(kind).append(' ');
        outer:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 2) continue outer;
                if (i == 2) break outer;
                sb.append(i).append(j).append(',');
            }
        }
        sb.append(' ');
        int calls = 0;
        boolean r1 = false && (calls++ > 0), r2 = true || (calls++ > 0), r3 = false & (calls++ > 0), r4 = true | (calls++ > 0);
        sb.append(calls).append(r1).append(r2).append(r3).append(r4).append(' ');
        int x = 0;
        for (int i = 0; i < 3; i++) x = x++ + 1;
        Object o = 42;
        String desc = o instanceof Integer n && n > 40 ? "big" + n : "small";
        sb.append(x).append(' ').append(desc);
        return sb.toString();
    }
}
