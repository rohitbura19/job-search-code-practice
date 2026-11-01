package practice.d20261101.h1;

/**
 * s1: static initialisers run once, parent first (As, Bs); every `new B()` runs parent instance init + ctor, then child (Ai Ac Bi Bc). JLS 12.4, 12.5.
 * s2: return evaluates the value first. int x: copy (1). `return` in finally discards the exception (3). Returned String is already built ("ab");
 *     the array reference is returned, so the later write shows (9).
 * s3: b += 300 is (byte)(10+300) = 54. c += 1 is 'b'. i++ + ++i = 5 + 7 = 12. 1+2 is int 3 before "x". 'a'+1 = 98 (int) before "x".
 *     MAX+1 and abs(MIN) overflow to MIN. 0.1+0.2 != 0.3. 1<<33 shifts by 33 & 31 = 1 -> 2; 1L<<33 = 8589934592. -7/2 truncates to -3, -7%3 = -1.
 *     (int)3.99e10 saturates at Integer.MAX_VALUE (JLS 5.1.3).
 * s4: Integer cache covers -128..127 only. Long.equals(Integer) is false (different class). `true ? 1 : 2.0` is promoted to double: 1.0.
 * s5: array covariance -> ArrayStoreException at runtime. Arrays.asList is fixed-size but set() writes through, and it is a VIEW of arr.
 *     A mutated HashSet key stays in its old bucket: contains false, size 1. remove(1) is by index (removes 2), remove(Integer) by value.
 *     List.of(...).contains(null) throws NPE (immutable collections reject null queries).
 * s6: replaceAll takes a regex ("." = any char); split drops TRAILING empty strings only ("a,b,,"->2, ",a"->2), "".split gives [""] (1).
 *     Compile-time constants are interned (lit == folded); runtime concatenation creates a new String (false) until intern().
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "As Bs Ai Ac Bi Bc Ai Ac Bi Bc"; }
    public static String s2() { return "1,3,ab,9"; }
    public static String s3() { return "54,b,12,3x12,98x,-2147483648,-2147483648,false,2,8589934592,-3,-1,2147483647"; }
    public static String s4() { return "true,false,true,false,true,1.0"; }
    public static String s5() { return "ASE,[9, 2, 3],UOE,7,false,1,[3],NPE"; }
    public static String s6() { return "xxx,axb,2,2,1,null,-1,true,false,true"; }
}
