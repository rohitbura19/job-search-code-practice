package practice.d20261110.h2;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * Value object: final, immutable, validated in the factory, equals/hashCode over (minor, currency). Money is a long of minor units so + - * are exact
 * (Math.xxxExact makes overflow loud). Allocation: floor(amount * ratio / total) per part with 128-bit-safe BigInteger arithmetic, then the leftover
 * (|remainder| < parts) is handed out one unit at a time from the first part, so the parts always sum to the original. Parsing goes through BigDecimal
 * with setScale(digits, UNNECESSARY) to reject silently-lost precision.
 */
public final class Money implements Comparable<Money> {
    private final long minor;
    private final Currency currency;

    private Money(long minor, Currency currency) { this.minor = minor; this.currency = currency; }

    private static Currency cur(String code) {
        if (code == null || !code.equals(code.toUpperCase()) || code.length() != 3) throw new IllegalArgumentException("bad currency " + code);
        try { return Currency.getInstance(code); } catch (IllegalArgumentException e) { throw new IllegalArgumentException("unknown currency " + code); }
    }

    public static Money of(long minor, String currencyCode) { return new Money(minor, cur(currencyCode)); }
    public static Money zero(String currencyCode) { return of(0, currencyCode); }

    public static Money parse(String text) {
        if (text == null) throw new IllegalArgumentException("null");
        String[] p = text.split(" ", -1);
        if (p.length != 2) throw new IllegalArgumentException("bad money " + text);
        Currency c = cur(p[1]);
        try {
            BigDecimal d = new BigDecimal(p[0]);
            BigDecimal scaled = d.setScale(c.getDefaultFractionDigits(), RoundingMode.UNNECESSARY);
            return new Money(scaled.movePointRight(c.getDefaultFractionDigits()).longValueExact(), c);
        } catch (ArithmeticException | NumberFormatException e) {
            throw new IllegalArgumentException("bad money " + text);
        }
    }

    public long minor() { return minor; }
    public String currencyCode() { return currency.getCurrencyCode(); }
    public boolean isNegative() { return minor < 0; }

    private void same(Money o) {
        if (!currency.equals(o.currency)) throw new IllegalArgumentException("currency mismatch " + currency + " vs " + o.currency);
    }

    public Money plus(Money o) { same(o); return new Money(Math.addExact(minor, o.minor), currency); }
    public Money minus(Money o) { same(o); return new Money(Math.subtractExact(minor, o.minor), currency); }
    public Money negate() { return new Money(Math.negateExact(minor), currency); }
    public Money times(long f) { return new Money(Math.multiplyExact(minor, f), currency); }
    public Money times(BigDecimal f, RoundingMode mode) {
        return new Money(BigDecimal.valueOf(minor).multiply(f).setScale(0, mode).longValueExact(), currency);
    }

    public List<Money> allocate(long... ratios) {
        if (ratios.length == 0) throw new IllegalArgumentException("no ratios");
        BigInteger total = BigInteger.ZERO;
        for (long r : ratios) {
            if (r < 0) throw new IllegalArgumentException("negative ratio");
            total = total.add(BigInteger.valueOf(r));
        }
        if (total.signum() == 0) throw new IllegalArgumentException("zero total");
        long[] part = new long[ratios.length];
        long assigned = 0;
        for (int i = 0; i < ratios.length; i++) {
            part[i] = BigInteger.valueOf(minor).multiply(BigInteger.valueOf(ratios[i])).divide(total).longValueExact();   // truncates toward zero
            assigned += part[i];
        }
        long left = minor - assigned;               // same sign as minor, |left| < number of parts
        int step = left < 0 ? -1 : 1;
        for (int i = 0; left != 0; i = (i + 1) % part.length) {
            if (ratios[i] == 0) continue;           // never give anything to a zero-ratio part
            part[i] += step;
            left -= step;
        }
        List<Money> out = new ArrayList<>();
        for (long v : part) out.add(new Money(v, currency));
        return List.copyOf(out);
    }

    public List<Money> split(int n) {
        if (n < 1) throw new IllegalArgumentException("n < 1");
        long[] r = new long[n];
        java.util.Arrays.fill(r, 1);
        return allocate(r);
    }

    @Override public int compareTo(Money o) { same(o); return Long.compare(minor, o.minor); }
    @Override public boolean equals(Object o) { return o instanceof Money m && m.minor == minor && m.currency.equals(currency); }
    @Override public int hashCode() { return 31 * Long.hashCode(minor) + currency.hashCode(); }

    @Override public String toString() {
        int d = currency.getDefaultFractionDigits();
        if (d <= 0) return minor + " " + currency;
        BigDecimal v = BigDecimal.valueOf(minor, d);
        return v.toPlainString() + " " + currency;
    }
}
