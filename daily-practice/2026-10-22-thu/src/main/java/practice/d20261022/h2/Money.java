package practice.d20261022.h2;

import java.util.List;

/**
 * Immutable value object: amount in minor units (paise, cents) + ISO currency code (stored upper-case).
 * Rules: null currency -> NullPointerException; blank currency -> IllegalArgumentException;
 * mixing currencies in plus / compareTo -> IllegalArgumentException; overflow -> ArithmeticException.
 * equals/hashCode on (currency, minorUnits); compareTo by amount; toString "INR 1234" (currency, space, minor units).
 * allocate(parts): split as evenly as possible, the remainder goes one unit at a time to the FIRST parts, the sum is exact;
 *   100 / 3 -> 34, 33, 33; -100 / 3 -> -34, -33, -33; parts < 1 -> IllegalArgumentException.
 * allocate(ratios): proportional split (ratios >= 0, sum > 0, else IllegalArgumentException); each share is rounded toward zero,
 *   then the leftover units go one at a time to the first shares, so the sum is exact.
 */
public final class Money implements Comparable<Money> {
    public static Money of(String currency, long minorUnits) { throw new UnsupportedOperationException("TODO"); }

    public String currency() { throw new UnsupportedOperationException("TODO"); }

    public long minorUnits() { throw new UnsupportedOperationException("TODO"); }

    public Money plus(Money other) { throw new UnsupportedOperationException("TODO"); }

    public Money times(long factor) { throw new UnsupportedOperationException("TODO"); }

    public List<Money> allocate(int parts) { throw new UnsupportedOperationException("TODO"); }

    public List<Money> allocate(long... ratios) { throw new UnsupportedOperationException("TODO"); }

    @Override public int compareTo(Money o) { throw new UnsupportedOperationException("TODO"); }

    @Override public boolean equals(Object o) { throw new UnsupportedOperationException("TODO"); }

    @Override public int hashCode() { throw new UnsupportedOperationException("TODO"); }

    @Override public String toString() { throw new UnsupportedOperationException("TODO"); }
}
