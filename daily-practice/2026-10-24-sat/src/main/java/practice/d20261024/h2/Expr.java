package practice.d20261024.h2;

/** A closed expression tree: the compiler knows every case, so a switch over it needs no default. */
public sealed interface Expr {
    enum Op {
        ADD("+", 1), SUB("-", 1), MUL("*", 2), DIV("/", 2);

        final String symbol;
        final int precedence;

        Op(String symbol, int precedence) { this.symbol = symbol; this.precedence = precedence; }
    }

    record Num(long value) implements Expr {}

    record Var(String name) implements Expr {}

    record Neg(Expr operand) implements Expr {}

    record Bin(Op op, Expr left, Expr right) implements Expr {}
}
