package practice.d20261024.h2;

import java.util.Map;

public final class Exprs {
    private Exprs() {}

    /**
     * Evaluate with variables from env. Use a switch over the sealed Expr with record patterns (no default). Unbound variable ->
     * IllegalArgumentException; division by zero and long overflow -> ArithmeticException (use the *Exact methods). Division
     * truncates toward zero.
     */
    public static long eval(Expr e, Map<String, Long> env) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Bottom-up: fold constant operations (never a division by zero), x+0, 0+x, x-0, x*1, 1*x, x/1 -> x; x*0, 0*x -> 0;
     * Neg(Num n) -> Num(-n); Neg(Neg x) -> x. Nothing else (0 - x stays).
     */
    public static Expr simplify(Expr e) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Infix with single spaces around binary operators and MINIMAL parentheses. + - have precedence 1, * / have 2, unary minus and
     * negative literals 3, atoms 4. Operators are left-associative: a left operand needs parentheses iff its precedence is lower, a
     * right operand iff it is lower OR EQUAL (a - (b - c)). The operand of Neg is parenthesised unless it is an atom: -x, -(a + b),
     * -(-x), -(-3).
     */
    public static String show(Expr e) { throw new UnsupportedOperationException("TODO"); }
}
