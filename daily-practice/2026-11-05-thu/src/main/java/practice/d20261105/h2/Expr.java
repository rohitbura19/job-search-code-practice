package practice.d20261105.h2;

/** Given. A tiny expression language as a sealed hierarchy of records (Java 21). */
public sealed interface Expr {
    record Num(long value) implements Expr {}
    record Var(String name) implements Expr {}
    record Add(Expr left, Expr right) implements Expr {}
    record Mul(Expr left, Expr right) implements Expr {}
    record Neg(Expr operand) implements Expr {}
}
