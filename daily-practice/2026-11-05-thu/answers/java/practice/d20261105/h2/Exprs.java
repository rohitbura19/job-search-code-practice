package practice.d20261105.h2;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import practice.d20261105.h2.Expr.Add;
import practice.d20261105.h2.Expr.Mul;
import practice.d20261105.h2.Expr.Neg;
import practice.d20261105.h2.Expr.Num;
import practice.d20261105.h2.Expr.Var;

/**
 * Sealed interface + records + pattern-matching switch: a closed set of cases (an algebraic data type). With no default branch, adding a new
 * record makes every switch fail to compile, which is the point (compare with the Visitor pattern: same guarantee, far less ceremony, but the
 * set of OPERATIONS is open and the set of TYPES is closed). Records give structural equals/hashCode, so tests compare trees directly.
 * simplify is one bottom-up pass: children first, then local rules; local rules never create a new simplifiable pattern in the result.
 * eval/vars/simplify/show are O(size of the tree); recursion depth = tree height (use an explicit stack for very deep trees).
 */
public final class Exprs {
    private Exprs() {}

    public static long eval(Expr e, Map<String, Long> env) {
        return switch (e) {
            case Num n -> n.value();
            case Var v -> {
                Long x = env.get(v.name());
                if (x == null) throw new IllegalArgumentException("unbound: " + v.name());
                yield x;
            }
            case Add a -> Math.addExact(eval(a.left(), env), eval(a.right(), env));
            case Mul m -> Math.multiplyExact(eval(m.left(), env), eval(m.right(), env));
            case Neg n -> Math.negateExact(eval(n.operand(), env));
        };
    }

    public static SortedSet<String> vars(Expr e) {
        SortedSet<String> out = new TreeSet<>();
        collect(e, out);
        return out;
    }

    private static void collect(Expr e, SortedSet<String> out) {
        switch (e) {
            case Num n -> { }
            case Var v -> out.add(v.name());
            case Add a -> { collect(a.left(), out); collect(a.right(), out); }
            case Mul m -> { collect(m.left(), out); collect(m.right(), out); }
            case Neg n -> collect(n.operand(), out);
        }
    }

    public static Expr simplify(Expr e) {
        return switch (e) {
            case Num n -> n;
            case Var v -> v;
            case Neg n -> switch (simplify(n.operand())) {
                case Num x -> new Num(Math.negateExact(x.value()));
                case Neg inner -> inner.operand();
                case Expr other -> new Neg(other);
            };
            case Add a -> {
                Expr l = simplify(a.left()), r = simplify(a.right());
                if (l instanceof Num x && r instanceof Num y) yield new Num(Math.addExact(x.value(), y.value()));
                if (l instanceof Num x && x.value() == 0) yield r;
                if (r instanceof Num y && y.value() == 0) yield l;
                yield new Add(l, r);
            }
            case Mul m -> {
                Expr l = simplify(m.left()), r = simplify(m.right());
                if (l instanceof Num x && r instanceof Num y) yield new Num(Math.multiplyExact(x.value(), y.value()));
                if ((l instanceof Num x && x.value() == 0) || (r instanceof Num y && y.value() == 0)) yield new Num(0);
                if (l instanceof Num x && x.value() == 1) yield r;
                if (r instanceof Num y && y.value() == 1) yield l;
                yield new Mul(l, r);
            }
        };
    }

    private static int prec(Expr e) {
        return switch (e) {
            case Add a -> 1;
            case Mul m -> 2;
            case Neg n -> 3;
            case Num n -> n.value() < 0 ? 3 : 4;
            case Var v -> 4;
        };
    }

    private static String child(Expr e, int parentPrec, boolean right) {
        boolean parens = prec(e) < parentPrec || (right && prec(e) == parentPrec);
        return parens ? "(" + show(e) + ")" : show(e);
    }

    public static String show(Expr e) {
        return switch (e) {
            case Num n -> Long.toString(n.value());
            case Var v -> v.name();
            case Add a -> child(a.left(), 1, false) + " + " + child(a.right(), 1, true);
            case Mul m -> child(m.left(), 2, false) + " * " + child(m.right(), 2, true);
            case Neg n -> "-" + (prec(n.operand()) == 4 ? show(n.operand()) : "(" + show(n.operand()) + ")");
        };
    }
}
