package practice.d20261023.h1;

/**
 * 1. Resources close in REVERSE order (b, a) before catch runs; the body exception is primary and both close() failures are attached
 *    as suppressed in closing order (b first). catch runs before finally.
 * 2. `true ? 1 : 2.0` is binary numeric promotion -> 1.0; with an Object cast no promotion -> 1. `false ? 1 : n` has types int and
 *    Integer -> unboxes n -> NPE. `true ? c : 0`: 0 is a constant int that fits char -> type char -> 'a'; with the variable i the type is
 *    int -> 97.
 * 3. The set stored k under hash([1]); after the mutation k's hash is hash([1,2]) so contains() looks in the wrong bucket (false for
 *    both [1,2] lookups); List.of(1) hashes right but is not equal to [1,2]. The element is still there (size 1, found by a scan).
 * 4. Streams pull one element at a time through the whole pipeline; limit(2) stops the source after the second kept element (3), so 4
 *    and 5 are never touched. 2 is peeked but filtered out.
 * 5. Fields are hidden, not overridden: they resolve on the STATIC type (p.name = P, c.name = C, ((P)c).name = P); who() is declared in P
 *    so it reads P.name. Static methods are hidden too: p.s() = Ps (static type P), c.s() = Cs. i() is virtual -> Ci.
 * 6. subList is a view: set/remove write through to base ([1,20,4,5]); Arrays.asList writes through to the array ([9,1,2]) but cannot
 *    grow; List.of is fully immutable.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "open a|open b|body|close b|close a|catch body-ex 2 close-b close-a|finally"; }
    public static String s2() { return "1.0,1,NPE,a,97"; }
    public static String s3() { return "false,false,false,1,true"; }
    public static String s4() { return "[10, 30] [p1, m1, p2, p3, m3]"; }
    public static String s5() { return "P,C,P,P,Ps,Cs,Ci"; }
    public static String s6() { return "[1, 20, 4, 5] [20, 4] [9, 1, 2] UOE UOE"; }
}
