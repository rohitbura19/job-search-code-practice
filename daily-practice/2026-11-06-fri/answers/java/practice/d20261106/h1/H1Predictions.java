package practice.d20261106.h1;

/**
 * s1: super constructor runs first and calls the OVERRIDDEN me(); C.name is still null; fields and static methods are bound to the static type (P).
 * s2: resources close in reverse order; close() exceptions are suppressed onto the primary one; catch runs after close, finally last.
 * s3: streams are lazy and element-at-a-time; limit(2) short-circuits after the second even number (4), so 5 and 6 are never pulled.
 * s4: 1 : 2.0 -> binary numeric promotion to double; Integer : String -> stays an Integer (reference); `false ? 1 : n` unboxes n -> NPE;
 *     char : constant 0 that fits char -> char ('a'); char : int variable -> int (97).
 * s5: char arithmetic is int; "" + 'a' + 1 concatenates left to right; 1/2 is 0; abs(MIN_VALUE) overflows; % keeps the dividend's sign,
 *     / truncates; (byte)200 = -56; (int) of an out-of-range double saturates; 5>>1=2, -5>>>31=1, -5>>31=-1.
 * s6: removing the second-to-last element during foreach ends the loop silently (hasNext sees cursor == size); removing any other throws CME at next();
 *     Arrays.asList is fixed-size but writable; List.of(...).contains(null) throws NPE; a TreeMap comparator defines key equality ("bb" replaces the value of "aa", key stays "aa").
 */
public class H1Predictions {
    public static String s1() { return "Cm:null|P|Ps|Cm:C|C"; }
    public static String s2() { return "openA openB body closeB closeA caught:body suppressed:cB,cA, finally"; }
    public static String s3() { return "p1p2f2p3p4f4|[2, 4]"; }
    public static String s4() { return "1.0|1|NPE|a|97"; }
    public static String s5() { return "98,b,a1,98,0.0,-2147483648,-1,-3,2,false,-56,2147483647,21-1"; }
    public static String s6() { return "[1, 2, 4] CME [1, 3, 4] UOE NPE [9, 6, 7] {c=3, aa=2}"; }
}
