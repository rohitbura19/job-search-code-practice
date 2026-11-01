package practice.d20261101.h2;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Final + private constructor + only immutable fields (a List.copyOf of identifiers) = immutable, so it is safe as a hash key and to share.
 * equals/hashCode use the same fields compareTo uses (build metadata is not stored), so compareTo == 0 <=> equals.
 * Numeric identifiers are compared by length then text: no parse, no overflow. parse O(len), compare O(identifiers).
 */
public final class Semver implements Comparable<Semver> {
    private static final Pattern NUM = Pattern.compile("0|[1-9][0-9]*");
    private static final Pattern ID = Pattern.compile("[0-9A-Za-z-]+");

    private final int major, minor, patch;
    private final List<String> pre;

    private Semver(int major, int minor, int patch, List<String> pre) {
        this.major = major; this.minor = minor; this.patch = patch; this.pre = List.copyOf(pre);
    }

    public static Semver parse(String text) {
        if (text == null) throw new IllegalArgumentException("null");
        String rest = text;
        int plus = rest.indexOf('+');
        if (plus >= 0) {
            for (String id : rest.substring(plus + 1).split("\\.", -1)) if (!ID.matcher(id).matches()) throw bad(text);
            rest = rest.substring(0, plus);
        }
        List<String> pre = List.of();
        int dash = rest.indexOf('-');
        if (dash >= 0) {
            String[] ids = rest.substring(dash + 1).split("\\.", -1);
            for (String id : ids) {
                if (!ID.matcher(id).matches()) throw bad(text);
                if (id.chars().allMatch(Character::isDigit) && !NUM.matcher(id).matches()) throw bad(text);
            }
            pre = Arrays.asList(ids);
            rest = rest.substring(0, dash);
        }
        String[] p = rest.split("\\.", -1);
        if (p.length != 3) throw bad(text);
        try {
            for (String s : p) if (!NUM.matcher(s).matches()) throw bad(text);
            return new Semver(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]), pre);
        } catch (NumberFormatException e) {
            throw bad(text);
        }
    }

    private static IllegalArgumentException bad(String t) { return new IllegalArgumentException("bad version: " + t); }

    public int major() { return major; }
    public int minor() { return minor; }
    public int patch() { return patch; }
    public String preRelease() { return String.join(".", pre); }

    public Semver bumpMajor() { return new Semver(Math.addExact(major, 1), 0, 0, List.of()); }
    public Semver bumpMinor() { return new Semver(major, Math.addExact(minor, 1), 0, List.of()); }
    public Semver bumpPatch() { return new Semver(major, minor, Math.addExact(patch, 1), List.of()); }

    @Override public int compareTo(Semver o) {
        int c = Integer.compare(major, o.major);
        if (c == 0) c = Integer.compare(minor, o.minor);
        if (c == 0) c = Integer.compare(patch, o.patch);
        if (c != 0) return c;
        if (pre.isEmpty() || o.pre.isEmpty()) return Boolean.compare(pre.isEmpty(), o.pre.isEmpty());
        for (int i = 0; i < Math.min(pre.size(), o.pre.size()); i++) {
            c = compareId(pre.get(i), o.pre.get(i));
            if (c != 0) return c;
        }
        return Integer.compare(pre.size(), o.pre.size());
    }

    private static int compareId(String a, String b) {
        boolean na = a.chars().allMatch(Character::isDigit), nb = b.chars().allMatch(Character::isDigit);
        if (na && nb) return a.length() != b.length() ? Integer.compare(a.length(), b.length()) : a.compareTo(b);
        if (na) return -1;
        if (nb) return 1;
        return a.compareTo(b);
    }

    @Override public boolean equals(Object o) {
        return o instanceof Semver s && major == s.major && minor == s.minor && patch == s.patch && pre.equals(s.pre);
    }

    @Override public int hashCode() { return Objects.hash(major, minor, patch, pre); }

    @Override public String toString() {
        return major + "." + minor + "." + patch + (pre.isEmpty() ? "" : "-" + preRelease());
    }
}
