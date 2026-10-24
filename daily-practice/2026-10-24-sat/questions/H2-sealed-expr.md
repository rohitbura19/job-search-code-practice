> Code: `src/main/java/practice/d20261024/h2/` (`Expr` is given; implement `Exprs`). Test: `ExprsTest`.

# H2. OOP: a sealed expression tree with records and pattern matching (30 min)

`Expr` is a sealed interface with records `Num`, `Var`, `Neg` and `Bin(Op, left, right)`. Implement in `Exprs` (rules in the Javadoc, all with a `switch` over the sealed type and record patterns, no `default`):
1. `eval(expr, env)` - exact arithmetic, unbound variable and division by zero are errors.
2. `simplify(expr)` - bottom-up constant folding and identities (`x + 0`, `x * 1`, `x * 0`, `--x`), never folding a division by zero, never rewriting `0 - x`.
3. `show(expr)` - infix with minimal parentheses for left-associative `+ - * /`.

Say out loud: what `sealed` buys you over an interface with a visitor (exhaustiveness checked by the compiler, no default branch that hides a new case) and what it costs (adding a new operation is easy, adding a new subtype touches every switch: the expression problem); why records suit a tree (value equality makes the tests one-liners); what breaks with a 1,000,000-deep tree (stack) and how to fix it (explicit stack); why `a - (b - c)` must keep its parentheses but `a + (b * c)` must not have any.
