package practice.d20261027.h1;

/**
 * Reasons (JLS):
 * 1. return evaluates its expression first, so a() returns 1 although x becomes 2; a `return` in finally replaces the
 *    exception (b = 3, 14.20.2); c() returns the same StringBuilder reference that finally mutates; `continue` still runs
 *    finally: 0f, f, 2f.
 * 2. abs(MIN_VALUE) and MAX_VALUE + 1 overflow silently (two's complement); narrowing double to int saturates, NaN -> 0 (5.1.3),
 *    truncates toward zero; 0.1 + 0.2 != 0.3 in binary floating point; (byte) 128 = -128; 1.0/0 is Infinity; 0.0 == -0.0 but
 *    Double.equals compares bits; NaN != NaN; (long) big * 2 casts first, big * 2 overflows in int; & binds tighter than ^ than |.
 * 3. List.of is immutable, Arrays.asList is fixed-size but writable (and write-through); subList is a view and any structural
 *    change of the parent invalidates it; removing the second-to-last element in a for-each ends the loop silently
 *    (hasNext() sees cursor == size) while removing another element fails on the next next(); TreeSet uses the comparator,
 *    not equals, so "bb" is a duplicate of "aa".
 * 4. Phase 1 (no boxing, no varargs) finds p(long) by widening for int, short and char; p(Integer) needs boxing, so for an int
 *    argument it loses to widening; a double fits only Object (boxing); no arguments -> varargs. Static methods are chosen by
 *    the compile-time type (hiding), instance methods by the runtime type (overriding): P.call() always uses P.who().
 * 5. Resources close in reverse order of opening; an exception from the body is primary and close() failures are suppressed
 *    (in closing order); with no body exception the first close() failure becomes primary and later ones are suppressed.
 * 6. `true ? 1 : 2.0` is binary numeric promotion -> double 1.0; a constant int that fits char keeps the type char ('x'), a
 *    non-constant int promotes to int (120); `true ? nul : 0` has type int, so null is unboxed -> NPE; a mixed
 *    reference/null operand `true ? null : 0` is Integer -> null (no unboxing).
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "1 3 ab 0ff2f"; }
    public static String s2() { return "-2147483648 -2147483648 2147483647 0 -2 false -128 Infinity true false false 4294967294 -2 A65 5"; }
    public static String s3() { return "UOE UOE2 [9, 2, 3] [1, 20, 3, 4] CME [1, 3] CME2 [c, aa]"; }
    public static String s4() { return "long Integer long long Object varargs Object PC P C PC"; }
    public static String s5() { return "open-a open-b body close-b close-a body suppressed=2 cb ca | open-a open-b body2 close-b close-a cb suppressed=1 ca"; }
    public static String s6() { return "1 1.0 x 120 b NPE Integer Double null"; }
}
