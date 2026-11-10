package practice.d20261110.h2;

/**
 * Immutable value object: an amount in MINOR units (cents) of a currency. Final class, private constructor, static factories.
 * Currency minor-unit digits come from java.util.Currency (USD 2, JPY 0, KWD 3).
 *
 * Contract (tests check each line):
 *  - of(long minor, String currencyCode): code is upper-case ISO 4217; unknown -> IllegalArgumentException. zero(code) is of(0, code).
 *  - parse("12.34 USD"): decimal amount, one space, code. More fraction digits than the currency allows (e.g. "1.234 USD") -> IllegalArgumentException;
 *    fewer is fine ("5 USD", "5.5 USD"); negative allowed ("-0.05 USD"); malformed -> IllegalArgumentException. Use BigDecimal for the parse, no double.
 *  - toString(): fixed fraction digits, "12.34 USD", "-0.05 USD", "500 JPY". minor() and currencyCode() accessors.
 *  - plus / minus: same currency required (IllegalArgumentException otherwise); overflow -> ArithmeticException (Math.addExact etc.). negate(), isNegative().
 *  - times(long): exact, overflow -> ArithmeticException. times(BigDecimal factor, RoundingMode): rounds the resulting minor units.
 *  - allocate(long... ratios): split into ratios.length parts proportional to the ratios, parts sum EXACTLY to this amount; leftover minor units are handed out one by one
 *    from the first part onwards (largest-remainder is not needed). All ratios >= 0 and the total > 0, else IllegalArgumentException. Works for negative amounts too (leftover is -1 each).
 *  - split(int n): allocate into n equal ratios (n >= 1).
 *  - compareTo: same currency required. equals/hashCode: same minor and same currency.
 */
public final class Money implements Comparable<Money> {
    private Money() { throw new UnsupportedOperationException("TODO"); }

    public static Money of(long minor, String currencyCode) { throw new UnsupportedOperationException("TODO"); }
    public static Money zero(String currencyCode) { throw new UnsupportedOperationException("TODO"); }
    public static Money parse(String text) { throw new UnsupportedOperationException("TODO"); }

    public long minor() { throw new UnsupportedOperationException("TODO"); }
    public String currencyCode() { throw new UnsupportedOperationException("TODO"); }
    public boolean isNegative() { throw new UnsupportedOperationException("TODO"); }

    public Money plus(Money other) { throw new UnsupportedOperationException("TODO"); }
    public Money minus(Money other) { throw new UnsupportedOperationException("TODO"); }
    public Money negate() { throw new UnsupportedOperationException("TODO"); }
    public Money times(long factor) { throw new UnsupportedOperationException("TODO"); }
    public Money times(java.math.BigDecimal factor, java.math.RoundingMode mode) { throw new UnsupportedOperationException("TODO"); }
    public java.util.List<Money> allocate(long... ratios) { throw new UnsupportedOperationException("TODO"); }
    public java.util.List<Money> split(int n) { throw new UnsupportedOperationException("TODO"); }

    @Override public int compareTo(Money o) { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean equals(Object o) { throw new UnsupportedOperationException("TODO"); }
    @Override public int hashCode() { throw new UnsupportedOperationException("TODO"); }
    @Override public String toString() { throw new UnsupportedOperationException("TODO"); }
}
