package practice.d20261026.h1;

/**
 * Why each result:
 * s1: Integer.valueOf caches -128..127 so 127==127 but 128!=128 (equals is true). Long.equals(Integer) is false (type is part of
 *     equals). "a"+"b" is a compile-time constant -> same interned literal; half+"b" is built at run time (a new object) until
 *     intern(); a FINAL String initialised with a constant is a constant variable, so fhalf+"b" is folded too.
 * s2: class init runs once, parent first (static blocks); `Base.LOG.setLength(0)` itself triggers Base's static init, so "Bs" is
 *     cleared before Derived adds "Ds". Per object: parent field initialisers and instance blocks in textual order, parent
 *     constructor body, THEN child fields. The overridden hook() runs from the Base constructor while Derived.m is still 0.
 * s3: the entry was stored under hash(1); after the key's id becomes 2 get(k) probes bucket 2 and finds nothing, get(new Key(1))
 *     finds the bucket but equals fails. A class with equals but no hashCode puts equal objects in different buckets (size 2
 *     usually; shown here as 2 because identity hashes differ), List.contains uses equals only. remove(int index) vs
 *     remove(Object): index 1 removes 20, then the Integer 1 is removed.
 * s4: c += 1 and c++ keep the char type ('c'); `+` with a String on the left concatenates left to right, with two ints first it
 *     adds them; compound assignment casts back (s = (short)(10 + 5.7) = 15); (byte)200 = -56; >> keeps the sign, >>> does not;
 *     integer division truncates toward zero, % takes the sign of the dividend, floorMod does not.
 * s5: arrays are covariant (store check at run time); Arrays.asList(int[]) is a List<int[]> of size 1; clone() is shallow so the
 *     inner arrays are shared until the slot is replaced; count(null-array) / count((Object) null) / count(int[]) differ by
 *     which parameter the argument binds to; arrays use identity equals; Arrays.hashCode([1,2]) = 31*(31*1+1)+2 = 994.
 * s6: the compact constructor normalises x; records have value equals; the switch is ordered (guarded case first, null handled
 *     explicitly, the sealed Shape case catches the Square after Circle); `flag ? 1 : boxed` has type int, so the null Integer is
 *     unboxed -> NPE, while `flag ? Integer.valueOf(1) : boxed` is Integer and stays null.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "true false true false true true false true true true"; }
    public static String s2() { return "Ds Bf Bi Bc Dh(m=0) Df Di Dc | Bf Bi Bc Dh(m=0) Df Di Dc"; }
    public static String s3() { return "null null null true 1 2 true [10, 30]"; }
    public static String s4() { return "c 98 d 12 3 ab b 15 -56 -4 15 2 -2 -2 1"; }
    public static String s5() { return "ASE 13 993 0-11212 falsetrue994"; }
    public static String s6() { return "Point[x=0, y=2] true big42int7circle3shapestr3nullother NPE null"; }
}
