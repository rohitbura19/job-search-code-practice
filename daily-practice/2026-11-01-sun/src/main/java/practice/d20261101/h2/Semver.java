package practice.d20261101.h2;

/**
 * Immutable semantic version: MAJOR.MINOR.PATCH[-prerelease][+build]. Comparable, usable as a key in HashSet and TreeSet.
 * Rules:
 *  - parse: numeric parts are non-negative ints without leading zeros ("01.2.3" invalid, "0.2.3" ok); exactly three parts;
 *    pre-release = dot-separated identifiers [0-9A-Za-z-]+, numeric identifiers have no leading zeros ("1.0.0-01" invalid);
 *    build metadata after '+' is validated the same way (leading zeros allowed) and DISCARDED. Anything else -> IllegalArgumentException (also null).
 *  - compareTo: major, minor, patch; a version WITHOUT pre-release is greater than the same one with it; otherwise compare identifiers left to right:
 *    numeric < alphanumeric, numeric compare by value (they may exceed long: compare by length then text), alphanumeric compare in ASCII order,
 *    when all shared identifiers are equal the one with fewer identifiers is smaller.
 *  - equals/hashCode are consistent with compareTo (so "1.0.0+a" equals "1.0.0+b"). toString is canonical: "1.2.3" or "1.2.3-alpha.1".
 *  - bumpMajor/bumpMinor/bumpPatch return new versions with lower parts zeroed and pre-release cleared.
 */
public final class Semver implements Comparable<Semver> {
    public static Semver parse(String text) { throw new UnsupportedOperationException("TODO"); }

    public int major() { throw new UnsupportedOperationException("TODO"); }
    public int minor() { throw new UnsupportedOperationException("TODO"); }
    public int patch() { throw new UnsupportedOperationException("TODO"); }
    /** "" when there is none. */
    public String preRelease() { throw new UnsupportedOperationException("TODO"); }

    public Semver bumpMajor() { throw new UnsupportedOperationException("TODO"); }
    public Semver bumpMinor() { throw new UnsupportedOperationException("TODO"); }
    public Semver bumpPatch() { throw new UnsupportedOperationException("TODO"); }

    @Override public int compareTo(Semver o) { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean equals(Object o) { throw new UnsupportedOperationException("TODO"); }
    @Override public int hashCode() { throw new UnsupportedOperationException("TODO"); }
    @Override public String toString() { throw new UnsupportedOperationException("TODO"); }
}
