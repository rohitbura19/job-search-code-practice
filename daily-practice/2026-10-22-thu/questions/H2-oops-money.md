> Code: `h2/Money.java` (the contract is in its Javadoc). Test: `MoneyTest`. **30 min.**

# H2. OOP: an immutable `Money` value object with exact allocation

Implement `Money` (currency + minor units as `long`): factory with validation, `plus`, `times`, `compareTo`, `equals`/`hashCode`/`toString`, and two `allocate` overloads that split an amount without losing or inventing a unit (`100` over 3 parts is `34, 33, 33`; `-100` is `-34, -33, -33`; by ratios `3:2` of `100` is `60, 40`).

Say out loud: why `final` class, private constructor and no setters make it safe as a map key and across threads; why `double` is wrong for money and what `BigDecimal` would add (scale, rounding mode); the `equals`/`hashCode`/`compareTo` consistency contract (`BigDecimal` `2.0` vs `2.00` is the classic break); why `allocate` returns the remainder to the first parts and how a bank would choose who gets it; what changes with a sealed `Currency` type or a Java `record` (what you lose: the validating factory, unless a compact constructor does it); overflow with `Math.addExact`.
