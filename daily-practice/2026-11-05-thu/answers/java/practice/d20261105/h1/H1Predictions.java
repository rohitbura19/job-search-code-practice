package practice.d20261105.h1;

/**
 * s1: class init runs once (static blocks "Bs Ds"), then per object: super instance init in textual order (block, field), super ctor body, which calls the
 *     OVERRIDDEN hook while Derived's fields are still 0 (y=0), then Derived field init, block, ctor. JLS 12.4, 12.5.
 * s2: a return in finally replaces the pending return (2); the return value is evaluated BEFORE finally (1 stays 1, but a mutated array is not read again);
 *     continue/throw still run finally. JLS 14.20.2.
 * s3: Integer cache -128..127 (JLS 5.1.7) so 127 == 127 but 128 != 128; Long.equals(Integer) is false; ch += 1 compiles (implicit narrowing); (byte)200 = -56;
 *     sh += 70000 wraps: (short)(10 + 70000) = 4474; ternary 1 : "s" is Object 1; 1 : 2.0 binary-promotes to 1.0; false ? 'x' : 120 -> constant int fits char -> 'x';
 *     'a' + 'b' = 195 but "" + 'a' + 'b' = "ab"; 1 + 2 + "3" + 4 + 5 = "3345".
 * s4: remove(int) is an index, remove(Object) is a value; Arrays.asList writes through to the array, is fixed-size; unmodifiableList is a VIEW (4 elements), List.copyOf a copy (3);
 *     mutating a key after insertion changes its hash so contains() fails but the set still has 1 element.
 * s5: constant expressions are interned (a, c true), runtime concat and new String are not (b, d false), intern() returns the pooled one; StringBuilder has no equals override;
 *     String.valueOf((Object) null) is "null"; "b".compareTo("abc") = 'b' - 'a' = 1; "abc" vs "abd" = -1; trailing empty strings are dropped by split; a leading one is kept.
 * s6: phase 1 (no boxing, widening allowed): n(1) -> n(long); n(short) -> long; m(1) boxes to Integer; m(o) uses the STATIC type Object; p(1) widens to double, p(char) widens to double;
 *     p("s") only fits Object; n() and n(1, 2) need varargs (phase 3); erasure makes both ArrayList classes identical.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "Bs Ds Bi Bx Bc Dh(y=0) Dy Di Dc | Bi Bx Bc Dh(y=0) Dy Di Dc"; }
    public static String s2() { return "2,1,t0f0f1t2cf2,1"; }
    public static String s3() { return "true,false,true,false,true,b,99,c,-56,4474,1,1.0,x,195,ab,3345"; }
    public static String s4() { return "[10, 30, 2]|[z, y, c][z, y, c]|UOE|43|false|1"; }
    public static String s5() { return "true,false,true,false,true,false,4,1,-1,2,2,true,0"; }
    public static String s6() { return "String,Object,Integer,long,long,varargs,varargs,double,double,double,Object,true"; }
}
