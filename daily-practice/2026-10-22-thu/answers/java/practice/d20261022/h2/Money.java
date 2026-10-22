package practice.d20261022.h2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * OOP points: final class + private constructor + final fields (immutability, safe to share and use as a map key),
 * a static factory that validates once, equals/hashCode/compareTo consistent with each other, and exact arithmetic (long, never double).
 * allocate keeps the invariant sum(parts) == total, the classic "lost cent" bug in splitting bills.
 * Ratio shares use BigInteger for total * ratio so that large totals cannot overflow before the division.
 * Complexity: O(parts) for allocate, O(1) for the rest.
 */
public final class Money implements Comparable<Money> {
    private final String currency;
    private final long minor;

    private Money(String currency, long minor) {
        this.currency = currency;
        this.minor = minor;
    }

    public static Money of(String currency, long minorUnits) {
        Objects.requireNonNull(currency, "currency");
        String c = currency.trim().toUpperCase(Locale.ROOT);
        if (c.isEmpty()) throw new IllegalArgumentException("blank currency");
        return new Money(c, minorUnits);
    }

    public String currency() { return currency; }

    public long minorUnits() { return minor; }

    public Money plus(Money other) {
        requireSame(other);
        return new Money(currency, Math.addExact(minor, other.minor));
    }

    public Money times(long factor) {
        return new Money(currency, Math.multiplyExact(minor, factor));
    }

    public List<Money> allocate(int parts) {
        if (parts < 1) throw new IllegalArgumentException("parts=" + parts);
        long q = minor / parts, r = minor % parts;
        long step = Long.signum(minor);
        List<Money> out = new ArrayList<>(parts);
        for (int i = 0; i < parts; i++) out.add(new Money(currency, q + (i < Math.abs(r) ? step : 0)));
        return out;
    }

    public List<Money> allocate(long... ratios) {
        if (ratios.length == 0) throw new IllegalArgumentException("no ratios");
        BigInteger sum = BigInteger.ZERO;
        for (long x : ratios) {
            if (x < 0) throw new IllegalArgumentException("negative ratio");
            sum = sum.add(BigInteger.valueOf(x));
        }
        if (sum.signum() == 0) throw new IllegalArgumentException("ratios sum to zero");
        long[] share = new long[ratios.length];
        long given = 0;
        for (int i = 0; i < ratios.length; i++) {
            share[i] = BigInteger.valueOf(minor).multiply(BigInteger.valueOf(ratios[i])).divide(sum).longValueExact();
            given += share[i];
        }
        long left = minor - given;                       // |left| < ratios.length
        long step = Long.signum(left);
        for (int i = 0; i < Math.abs(left); i++) share[i] += step;
        return Arrays.stream(share).mapToObj(s -> new Money(currency, s)).toList();
    }

    private void requireSame(Money o) {
        if (!currency.equals(o.currency)) throw new IllegalArgumentException(currency + " vs " + o.currency);
    }

    @Override public int compareTo(Money o) {
        requireSame(o);
        return Long.compare(minor, o.minor);
    }

    @Override public boolean equals(Object o) {
        return o instanceof Money m && m.minor == minor && m.currency.equals(currency);
    }

    @Override public int hashCode() { return Objects.hash(currency, minor); }

    @Override public String toString() { return currency + " " + minor; }
}
