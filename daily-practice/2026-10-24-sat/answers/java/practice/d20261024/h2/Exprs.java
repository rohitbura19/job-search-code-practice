package practice.d20261024.h2;

import java.util.Map;
import practice.d20261024.h2.Expr.*;

/**
 * Pattern matching over a sealed hierarchy: adding a new Expr subtype makes every switch here a compile error until it is handled
 * (a visitor gives the same with more ceremony; an interface method scatters the logic across classes). eval uses the *Exact
 * methods so overflow throws instead of wrapping. simplify works bottom-up: simplify the children, then apply local rules. show
 * parenthesises by precedence with left associativity (so a - (b - c) and a + (b + c) keep their parentheses).
 */
public final class Exprs {
    private Exprs() {}

    public static long eval(Expr e, Map<String, Long> env) {
        return switch (e) {
            case Num n -> n.value();
            case Var v -> {
                Long x = env.get(v.name());
                if (x == null) throw new IllegalArgumentException("unbound " + v.name());
                yield x;
            }
            case Neg n -> Math.negateExact(eval(n.operand(), env));
            case Bin(Op op, Expr l, Expr r) -> {
                long a = eval(l, env), b = eval(r, env);
                yield switch (op) {
                    case ADD -> Math.addExact(a, b);
                    case SUB -> Math.subtractExact(a, b);
                    case MUL -> Math.multiplyExact(a, b);
                    case DIV -> a / b;
                };
            }
        };
    }

    public static Expr simplify(Expr e) {
        return switch (e) {
            case Num n -> n;
            case Var v -> v;
            case Neg n -> switch (simplify(n.operand())) {
                case Num(long v) -> new Num(-v);
                case Neg(Expr inner) -> inner;
                case Expr other -> new Neg(other);
            };
            case Bin(Op op, Expr l, Expr r) -> simplifyBin(op, simplify(l), simplify(r));
        };
    }

    private static Expr simplifyBin(Op op, Expr l, Expr r) {
        if (l instanceof Num(long a) && r instanceof Num(long b)) {
            if (op == Op.DIV && b == 0) return new Bin(op, l, r);
            return new Num(switch (op) {
                case ADD -> a + b;
                case SUB -> a - b;
                case MUL -> a * b;
                case DIV -> a / b;
            });
        }
        boolean l0 = l instanceof Num(long a) && a == 0, r0 = r instanceof Num(long b) && b == 0;
        boolean l1 = l instanceof Num(long a) && a == 1, r1 = r instanceof Num(long b) && b == 1;
        return switch (op) {
            case ADD -> l0 ? r : r0 ? l : new Bin(op, l, r);
            case SUB -> r0 ? l : new Bin(op, l, r);
            case MUL -> (l0 || r0) ? new Num(0) : l1 ? r : r1 ? l : new Bin(op, l, r);
            case DIV -> r1 ? l : new Bin(op, l, r);
        };
    }

    public static String show(Expr e) {
        return switch (e) {
            case Num n -> Long.toString(n.value());
            case Var v -> v.name();
            case Neg n -> "-" + wrap(n.operand(), prec(n.operand()) <= 3);
            case Bin(Op op, Expr l, Expr r) ->
                    wrap(l, prec(l) < op.precedence) + " " + op.symbol + " " + wrap(r, prec(r) <= op.precedence);
        };
    }

    private static String wrap(Expr e, boolean parens) {
        return parens ? "(" + show(e) + ")" : show(e);
    }

    private static int prec(Expr e) {
        return switch (e) {
            case Bin b -> b.op().precedence;
            case Neg n -> 3;
            case Num n -> n.value() < 0 ? 3 : 4;
            case Var v -> 4;
        };
    }
}
