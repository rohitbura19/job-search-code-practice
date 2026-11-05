package practice.d20261105.h2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;
import practice.d20261105.h2.Expr.Add;
import practice.d20261105.h2.Expr.Mul;
import practice.d20261105.h2.Expr.Neg;
import practice.d20261105.h2.Expr.Num;
import practice.d20261105.h2.Expr.Var;

class ExprsTest {
    static Expr n(long v) { return new Num(v); }
    static Expr v(String s) { return new Var(s); }
    static Expr add(Expr a, Expr b) { return new Add(a, b); }
    static Expr mul(Expr a, Expr b) { return new Mul(a, b); }
    static Expr neg(Expr a) { return new Neg(a); }

    @Test void evalBasics() {
        Expr e = add(mul(n(3), v("x")), neg(v("y")));
        assertEquals(3 * 5 - 2, Exprs.eval(e, Map.of("x", 5L, "y", 2L)));
        assertEquals(7, Exprs.eval(n(7), Map.of()));
        assertEquals(-7, Exprs.eval(neg(n(7)), Map.of()));
    }

    @Test void evalErrors() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> Exprs.eval(add(v("a"), v("b")), Map.of("a", 1L)));
        assertEquals("unbound: b", ex.getMessage());
        assertThrows(ArithmeticException.class, () -> Exprs.eval(add(n(Long.MAX_VALUE), n(1)), Map.of()));
        assertThrows(ArithmeticException.class, () -> Exprs.eval(mul(n(Long.MAX_VALUE), n(2)), Map.of()));
        assertThrows(ArithmeticException.class, () -> Exprs.eval(neg(n(Long.MIN_VALUE)), Map.of()));
    }

    @Test void varsAreSortedAndDistinct() {
        Expr e = add(mul(v("z"), v("a")), add(v("m"), neg(v("a"))));
        assertEquals(List.of("a", "m", "z"), List.copyOf(Exprs.vars(e)));
        assertEquals(List.of(), List.copyOf(Exprs.vars(add(n(1), n(2)))));
    }

    @Test void simplifyRules() {
        assertEquals(v("x"), Exprs.simplify(add(v("x"), n(0))));
        assertEquals(v("x"), Exprs.simplify(add(n(0), v("x"))));
        assertEquals(v("x"), Exprs.simplify(mul(n(1), v("x"))));
        assertEquals(v("x"), Exprs.simplify(mul(v("x"), n(1))));
        assertEquals(n(0), Exprs.simplify(mul(v("x"), n(0))));
        assertEquals(n(0), Exprs.simplify(mul(n(0), add(v("x"), v("y")))));
        assertEquals(n(7), Exprs.simplify(add(n(3), n(4))));
        assertEquals(n(12), Exprs.simplify(mul(add(n(1), n(2)), n(4))));
        assertEquals(n(-5), Exprs.simplify(neg(n(5))));
        assertEquals(v("x"), Exprs.simplify(neg(neg(v("x")))));
        assertEquals(neg(v("x")), Exprs.simplify(neg(v("x"))));
        assertEquals(v("x"), Exprs.simplify(add(neg(neg(v("x"))), mul(n(0), v("y")))));
        assertEquals(add(v("x"), v("y")), Exprs.simplify(add(mul(n(1), v("x")), add(v("y"), n(0)))));
        assertEquals(n(0), Exprs.simplify(n(0)));
    }

    @Test void simplifyKeepsTheRest() {
        Expr e = add(v("x"), mul(v("y"), v("z")));
        assertEquals(e, Exprs.simplify(e));
        Expr partial = add(v("x"), add(n(1), n(2)));
        assertEquals(add(v("x"), n(3)), Exprs.simplify(partial));
    }

    @Test void showPrecedenceAndAssociativity() {
        assertEquals("a + b + c", Exprs.show(add(add(v("a"), v("b")), v("c"))));
        assertEquals("a + (b + c)", Exprs.show(add(v("a"), add(v("b"), v("c")))));
        assertEquals("(a + b) * c", Exprs.show(mul(add(v("a"), v("b")), v("c"))));
        assertEquals("a + b * c", Exprs.show(add(v("a"), mul(v("b"), v("c")))));
        assertEquals("a * b * c", Exprs.show(mul(mul(v("a"), v("b")), v("c"))));
        assertEquals("a * (b * c)", Exprs.show(mul(v("a"), mul(v("b"), v("c")))));
        assertEquals("-x", Exprs.show(neg(v("x"))));
        assertEquals("-(-x)", Exprs.show(neg(neg(v("x")))));
        assertEquals("-(a + b)", Exprs.show(neg(add(v("a"), v("b")))));
        assertEquals("-(a * b)", Exprs.show(neg(mul(v("a"), v("b")))));
        assertEquals("1 + -2", Exprs.show(add(n(1), n(-2))));
        assertEquals("2 * -3", Exprs.show(mul(n(2), n(-3))));
        assertEquals("-(-3)", Exprs.show(neg(n(-3))));
        assertEquals("-3", Exprs.show(n(-3)));
        assertEquals("-x * y", Exprs.show(mul(neg(v("x")), v("y"))));
        assertEquals("(a + b) * (c + d)", Exprs.show(mul(add(v("a"), v("b")), add(v("c"), v("d")))));
    }

    static Expr random(Random r, int depth) {
        if (depth == 0 || r.nextInt(5) == 0) return r.nextBoolean() ? n(r.nextInt(7) - 3) : v(r.nextBoolean() ? "x" : "y");
        return switch (r.nextInt(3)) {
            case 0 -> add(random(r, depth - 1), random(r, depth - 1));
            case 1 -> mul(random(r, depth - 1), random(r, depth - 1));
            default -> neg(random(r, depth - 1));
        };
    }

    @Test void simplifyPreservesValueOnRandomTrees() {
        Random r = new Random(11);
        for (int t = 0; t < 2000; t++) {
            Expr e = random(r, 4);
            Map<String, Long> env = Map.of("x", (long) (r.nextInt(9) - 4), "y", (long) (r.nextInt(9) - 4));
            Expr s = Exprs.simplify(e);
            assertEquals(Exprs.eval(e, env), Exprs.eval(s, env), Exprs.show(e) + "  =>  " + Exprs.show(s));
            assertEquals(s, Exprs.simplify(s), "simplify must be idempotent: " + Exprs.show(s));
            assertEquals(true, Exprs.vars(e).containsAll(Exprs.vars(s)));
        }
    }
}
