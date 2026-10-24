package practice.d20261024.h2;

import static org.junit.jupiter.api.Assertions.*;
import static practice.d20261024.h2.Expr.Op.*;

import java.util.Map;
import org.junit.jupiter.api.Test;
import practice.d20261024.h2.Expr.*;

class ExprsTest {
    static Expr n(long v) { return new Num(v); }
    static Expr v(String s) { return new Var(s); }
    static Expr bin(Expr.Op op, Expr l, Expr r) { return new Bin(op, l, r); }

    // (x + 2) * -(y - 1)
    static final Expr SAMPLE = bin(MUL, bin(ADD, v("x"), n(2)), new Neg(bin(SUB, v("y"), n(1))));

    @Test void evalWithEnvironment() {
        assertEquals(-12, Exprs.eval(SAMPLE, Map.of("x", 2L, "y", 4L)));
        assertEquals(7, Exprs.eval(n(7), Map.of()));
        assertEquals(-5, Exprs.eval(new Neg(n(5)), Map.of()));
        assertEquals(-3, Exprs.eval(bin(DIV, n(-7), n(2)), Map.of()), "truncates toward zero");
    }

    @Test void evalErrors() {
        assertThrows(IllegalArgumentException.class, () -> Exprs.eval(v("z"), Map.of()));
        assertThrows(ArithmeticException.class, () -> Exprs.eval(bin(DIV, n(1), n(0)), Map.of()));
        assertThrows(ArithmeticException.class, () -> Exprs.eval(bin(ADD, n(Long.MAX_VALUE), n(1)), Map.of()));
        assertThrows(ArithmeticException.class, () -> Exprs.eval(bin(MUL, n(Long.MAX_VALUE), n(2)), Map.of()));
        assertThrows(ArithmeticException.class, () -> Exprs.eval(new Neg(n(Long.MIN_VALUE)), Map.of()));
    }

    @Test void showUsesMinimalParentheses() {
        assertEquals("(x + 2) * -(y - 1)", Exprs.show(SAMPLE));
        assertEquals("a + b * c", Exprs.show(bin(ADD, v("a"), bin(MUL, v("b"), v("c")))));
        assertEquals("(a + b) * c", Exprs.show(bin(MUL, bin(ADD, v("a"), v("b")), v("c"))));
        assertEquals("a - b - c", Exprs.show(bin(SUB, bin(SUB, v("a"), v("b")), v("c"))));
        assertEquals("a - (b - c)", Exprs.show(bin(SUB, v("a"), bin(SUB, v("b"), v("c")))));
        assertEquals("a / (b * c)", Exprs.show(bin(DIV, v("a"), bin(MUL, v("b"), v("c")))));
        assertEquals("a * b / c", Exprs.show(bin(DIV, bin(MUL, v("a"), v("b")), v("c"))));
        assertEquals("-x", Exprs.show(new Neg(v("x"))));
        assertEquals("-(-x)", Exprs.show(new Neg(new Neg(v("x")))));
        assertEquals("-(-3)", Exprs.show(new Neg(n(-3))));
        assertEquals("2 * -3", Exprs.show(bin(MUL, n(2), n(-3))));
        assertEquals("-3 + x", Exprs.show(bin(ADD, n(-3), v("x"))));
    }

    @Test void simplifyFoldsConstants() {
        assertEquals(n(14), Exprs.simplify(bin(MUL, bin(ADD, n(3), n(4)), n(2))));
        assertEquals(n(-3), Exprs.simplify(new Neg(n(3))));
        assertEquals(n(3), Exprs.simplify(new Neg(n(-3))));
        assertEquals(n(-3), Exprs.simplify(bin(DIV, n(-7), n(2))));
        Expr div0 = bin(DIV, n(1), n(0));
        assertEquals(div0, Exprs.simplify(div0), "never fold a division by zero");
    }

    @Test void simplifyAppliesIdentities() {
        assertEquals(v("x"), Exprs.simplify(bin(ADD, v("x"), n(0))));
        assertEquals(v("x"), Exprs.simplify(bin(ADD, n(0), v("x"))));
        assertEquals(v("x"), Exprs.simplify(bin(SUB, v("x"), n(0))));
        assertEquals(v("x"), Exprs.simplify(bin(MUL, n(1), v("x"))));
        assertEquals(v("x"), Exprs.simplify(bin(MUL, v("x"), n(1))));
        assertEquals(n(0), Exprs.simplify(bin(MUL, v("x"), n(0))));
        assertEquals(n(0), Exprs.simplify(bin(MUL, n(0), bin(ADD, v("x"), v("y")))));
        assertEquals(v("x"), Exprs.simplify(bin(DIV, v("x"), n(1))));
        assertEquals(v("x"), Exprs.simplify(new Neg(new Neg(v("x")))));
        assertEquals(bin(SUB, n(0), v("x")), Exprs.simplify(bin(SUB, n(0), v("x"))), "0 - x is not x");
    }

    @Test void simplifyWorksBottomUp() {
        // ((x * 1) + (2 - 2)) * (3 + 4) -> x * 7
        Expr e = bin(MUL, bin(ADD, bin(MUL, v("x"), n(1)), bin(SUB, n(2), n(2))), bin(ADD, n(3), n(4)));
        assertEquals(bin(MUL, v("x"), n(7)), Exprs.simplify(e));
        assertEquals(n(0), Exprs.simplify(bin(MUL, bin(SUB, n(5), n(5)), v("y"))));
    }

    @Test void simplifyKeepsTheValue() {
        Expr e = bin(ADD, bin(MUL, new Neg(new Neg(v("x"))), bin(ADD, n(1), n(0))), bin(DIV, v("y"), n(1)));
        Map<String, Long> env = Map.of("x", 6L, "y", -9L);
        assertEquals(Exprs.eval(e, env), Exprs.eval(Exprs.simplify(e), env));
        assertEquals(bin(ADD, v("x"), v("y")), Exprs.simplify(e));
    }

    @Test void deepTreeDoesNotNeedMuchStack() {
        Expr e = n(0);
        for (int i = 0; i < 2000; i++) e = bin(ADD, e, n(1));
        assertEquals(2000, Exprs.eval(e, Map.of()));
        assertEquals(n(2000), Exprs.simplify(e));
    }
}
