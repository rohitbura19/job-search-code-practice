package practice.d20261031.h1;

/**
 * s1: Integer.valueOf caches -128..127, so 127 == 127 but 128 != 128 (compare boxed values with equals). A ternary whose operands are
 *     int and double promotes to double (1.0); int vs String has no common numeric type, so the result is the boxed Integer 1;
 *     (Object) 1 on one side also blocks promotion. `flag ? nothing : 0` has type int, so null is unboxed: NPE (JLS 15.25).
 * s2: phase 1 (no boxing, no varargs): widening int -> long wins for m(1), m(short), m(char). Integer.valueOf(1) matches m(Integer) exactly.
 *     "s" and 1.5 (boxed to Double) only fit Object. m() has only the varargs candidate (phase 3). n(null): the most specific type, String.
 * s3: fields are not virtual: x.name uses the static type P. Methods are: who() dispatches to C. static methods are hidden, not overridden:
 *     x.s() uses the static type; show() is compiled in P, so name and s() resolve in P while who() is virtual.
 * s4: "a" + "b" and a constant `final String` + "b" are compile-time constants, interned, so == is true; part + "b" is computed at run time
 *     (a new String) until intern(). char + int in append(1 + 'a') is int 98; (char) 'b' prints b. 1 + 2 + "x" is "3x" then "12"; 'a' + 'b' is 195 before "c".
 * s5: remove(1) is remove(int index) (removes 20); remove(Integer.valueOf(1)) removes the value. Removing the SECOND-TO-LAST element in a for-each
 *     skips the CME because hasNext() sees cursor == size. A list used as a HashSet key and then mutated is lost: contains is false either way.
 *     Removing the first of three throws on the next next(): CME.
 * s6: resources close in reverse order; an exception from close() is attached with addSuppressed to the primary one (getSuppressed order = close order);
 *     the catch runs after the resources are closed, finally last.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "true,false,true,1,1.0,1,NPE"; }
    public static String s2() { return "long,Integer,Object,varargs,long,long,Object,String,Object"; }
    public static String s3() { return "P,C.who,P.s,P/C.who/P.s,C,P/C.who/P.s"; }
    public static String s4() { return "true,false,false,true,true,1a98b,3x12,195c"; }
    public static String s5() { return "[10, 30, 2][10, 2],false,false,1,CME"; }
    public static String s6() { return "openA openB body closeB closeA boom suppressed=cB;cA; fin"; }
}
