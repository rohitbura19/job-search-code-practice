package practice.d20261103.h1;

/**
 * s1: static init runs once per class, superclass first (JLS 12.4); then per object: super constructor, whose field initialisers and
 *     instance blocks run BEFORE its body; Base() calls the overridden show() while Derived's fields are still default (z == 0);
 *     Derived's initialisers run after super() returns. The second object skips the static part.
 * s2: a return in finally overrides the try's return and swallows the exception (f1, f3); the return value is copied before finally runs,
 *     so f2 sees 1 and f4 sees 11, but an object reference still sees later mutation (f5).
 * s3: Integer cache is -128..127 (JLS 5.1.7); compile-time constants are interned, runtime concatenation is not; Long.equals(Integer) is false;
 *     `true ? 1 : "s"` boxes to Integer but `true ? 1 : 2.0` is binary numeric promotion to double (1.0); compound assignment narrows
 *     implicitly (short 10 + 5.7 -> 15); char + int is int; String concatenation is left to right.
 * s4: the entry stays in the bucket of hash 1, so get(k) (hash 2) and get(new Key(1)) (hash 1, but equals fails) both miss; arrays use identity equals;
 *     List equals compares elements across implementations, Set vs List never equal; remove(int) is by index, remove(Object) by value;
 *     Arrays.asList is fixed-size but writable and writes through; List.of is immutable.
 * s5: generics are erased, so one Class for both lists and the heap pollution only fails at the checked cast; arrays are covariant and checked at runtime;
 *     h(null) picks the most specific (String); w(int literal) prefers widening to long over boxing (phase 1 before phase 2); float has no widening to long
 *     and boxes to Float -> Object.
 * s6: i = 0 + 2; j += uses the OLD j (5 + 10); x = x++ restores 10; shifts use the low 5 bits of the count for int; & binds tighter than ^ than |;
 *     Math.abs(MIN_VALUE) overflows; casts from double saturate.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "Bs Ds Ba Bi Bc Dshow0 Db Dc7 | Ba Bi Bc Dshow0 Db Dc7"; }
    public static String s2() { return "2,1,fin,11,ab"; }
    public static String s3() { return "true,false,true,true,false,true,false,false,true,1,1.0,b,99,c,15,ab,195,3345"; }
    public static String s4() { return "null/null/null/false/1 2/true/false true/true/true/false [3, 1, 0][3, 2, 0] UOE UOE [9, 2, 3]"; }
    public static String s5() { return "true CCE ASE SSO llllO truefalse int[] 1"; }
    public static String s6() { return "2,15,10,22,-4,15,2,8589934592,-64,5,-2147483648,-2147483648,2147483647,-2,-3,3"; }
}
