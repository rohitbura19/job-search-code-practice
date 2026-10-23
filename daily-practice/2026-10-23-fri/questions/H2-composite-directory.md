> Code: `src/main/java/practice/d20261023/h2/` (`Entry`, `FileEntry` given; implement `DirEntry`). Test: `DirEntryTest`.

# H2. OOP: a composite directory tree with invariants (30 min)

Implement `DirEntry` so that a directory and a file are used through the same `Entry` interface (Composite). The rules are in the class Javadoc and are all tested: name validation, no duplicate names, no cycles, live recursive `size()`, a sorted unmodifiable `children()` snapshot, sorted `paths()`, `largestFile()` with a deterministic tie-break.

Say out loud: why the interface is `sealed` and what pattern matching on it buys you; where the encapsulation boundary is (why `children()` copies); why a parent pointer would make the cycle check cheaper but the model more fragile; the recursion-depth risk for a 100k-deep tree and the iterative alternative; thread safety (what you would lock, or why you would make the tree immutable with a builder).
