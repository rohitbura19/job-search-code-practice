# H2. OOP: algebraic data types with sealed interfaces (20 min)

Files `h2/Expr.java` (given: sealed interface + records `Num`, `Var`, `Add`, `Mul`, `Neg`) and `h2/Exprs.java`. Test: `ExprsTest`.

Implement with pattern-matching `switch` and **no `default` branch**: `eval` (exact arithmetic, unbound variable error), `vars` (sorted), `simplify` (constant folding, identities, double negation; must keep the value and be idempotent) and `show` (minimal parentheses, rules in the Javadoc).

Say out loud: what does the compiler do if you add a `Sub` record? How does this compare with the Visitor pattern (open set of operations vs open set of types)?
