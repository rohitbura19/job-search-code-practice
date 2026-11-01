# H2. OOP: an immutable, Comparable value object (20 min)

`h2/Semver.java`: implement a semantic version (`MAJOR.MINOR.PATCH[-pre][+build]`). The class Javadoc is the spec: parsing rules, ordering rules (pre-release identifiers), build metadata ignored, `equals`/`hashCode` consistent with `compareTo`, bumps, immutability.
Paper: what goes wrong in a `TreeSet`/`HashSet` if `compareTo` and `equals` disagree (give `BigDecimal("1.0")` vs `BigDecimal("1.00")`)? Why `final` class + private constructor + `List.copyOf`? Why compare numeric identifiers by length then text?
