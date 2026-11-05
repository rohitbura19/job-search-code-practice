package practice.d20261105.h2;

import java.util.Map;
import java.util.SortedSet;

/** Use pattern-matching switch over the sealed Expr (no default branch: the compiler proves it exhaustive). */
public final class Exprs {
    private Exprs() {}

    /** Value of e under env. Unknown variable -> IllegalArgumentException("unbound: x"). Overflow -> ArithmeticException (Math.addExact etc.). */
    public static long eval(Expr e, Map<String, Long> env) { throw new UnsupportedOperationException("TODO"); }

    /** Names of all variables in e, alphabetical. */
    public static SortedSet<String> vars(Expr e) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Bottom-up simplification, same exact arithmetic as eval: Num op Num folds; x + 0, 0 + x -> x; x * 1, 1 * x -> x; x * 0, 0 * x -> 0;
     * -Num(n) -> Num(-n); -(-x) -> x. Everything else is rebuilt from the simplified children. Must not change the value of e.
     */
    public static Expr simplify(Expr e) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Minimal parentheses. Precedence: Add 1 < Mul 2 < prefix minus 3 (a negative Num counts as 3) < Var / non-negative Num 4.
     * Binary operators are left associative: the left child needs parentheses if its precedence is LOWER, the right child if LOWER OR EQUAL.
     * Neg(x) prints "-" + x, with x parenthesised unless its precedence is 4. Spaces around + and *.
     * show(Add(a, Add(b, c))) = "a + (b + c)"; show(Mul(Add(a, b), c)) = "(a + b) * c"; show(Neg(Neg(x))) = "-(-x)"; show(Add(Num(1), Num(-2))) = "1 + -2".
     */
    public static String show(Expr e) { throw new UnsupportedOperationException("TODO"); }
}
