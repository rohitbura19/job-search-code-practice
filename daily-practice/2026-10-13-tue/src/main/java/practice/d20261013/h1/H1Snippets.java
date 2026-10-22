package practice.d20261013.h1;

import java.util.HashSet;
import java.util.Set;

/**
 * GIVEN CODE - do not edit. Each method runs one puzzle from questions/H1 and returns what it "printed".
 * Read the code, predict the output, and write your prediction into {@link H1Predictions}.
 */
public final class H1Snippets {
    private H1Snippets() {}

    static int f(StringBuilder out) { try { return 1; } finally { out.append("fin "); } }
    static int g() { int x = 1; try { return x; } finally { x = 2; } }
    @SuppressWarnings("finally")
    static int h() { try { throw new RuntimeException("a"); } finally { return 3; } }

    /** Prints what f() prints, then " " and its return value. */
    public static String line1() { StringBuilder out = new StringBuilder(); int r = f(out); return out + "" + r; }
    public static String line2() { return String.valueOf(g()); }
    public static String line3() { return String.valueOf(h()); }

    @SuppressWarnings("StringEquality")
    public static String line4() {
        String a = "java";
        String b = new String("java");
        String c = b.intern();
        String ja = "ja";
        final String fja = "ja";
        return (a == b) + " " + (a == c) + " " + a.equals(b) + " " + ("ja" + "va" == a) + " " + ((ja + "va") == a) + " " + ((fja + "va") == a);
    }

    static final class R implements AutoCloseable {
        final String n; final StringBuilder out;
        R(String n, StringBuilder out) { this.n = n; this.out = out; out.append("open ").append(n).append('\n'); }
        @Override public void close() { out.append("close ").append(n).append('\n'); throw new IllegalStateException("close-" + n); }
    }

    public static String line5() {
        StringBuilder out = new StringBuilder();
        try (R r1 = new R("1", out); R r2 = new R("2", out)) {
            throw new RuntimeException("body");
        } catch (Exception e) {
            out.append(e.getMessage()).append(' ').append(java.util.Arrays.toString(e.getSuppressed()));
        }
        return out.toString();
    }

    /** equals/hashCode contract: hashCode only. */
    static final class OnlyHash {
        final int id;
        OnlyHash(int id) { this.id = id; }
        @Override public int hashCode() { return id; }
    }

    public static String line6() {
        Set<OnlyHash> set = new HashSet<>();
        set.add(new OnlyHash(1));
        set.add(new OnlyHash(1));
        return set.size() + " " + set.contains(new OnlyHash(1));
    }
}
