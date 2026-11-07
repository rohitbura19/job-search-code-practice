package practice.d20261107.h1;

/**
 * Why each result:
 * s1 (JLS 12.4/12.5): static blocks run once at class init (A then B); per object: A's instance init, then A(int) body, whose virtual
 *    init() runs B.init BEFORE B's field initialisers (v=0); then A() continues, then B's instance init (Bi), then B()'s body (v=7).
 * s2: Integer cache covers -128..127 (== true for 127, false for 128); Long.equals(Integer) is false; "a"+"b" and a final-variable
 *    concat are compile-time constants (interned, ==), a non-final variable concat is built at runtime (!=) until intern(); new String != literal.
 * s3 (JLS 14.20.2): a return inside finally wins (2); the return value is evaluated BEFORE finally, so the int stays 1 but the
 *    StringBuilder reference sees the append ("ab"); continue/break still run finally: i=0 t0f0, i=1 f1, i=2 t2f2, i=3 f3.
 * s4: after mutating a key its hash bucket is stale: lookups by old/new id fail (null), size stays 2, but a copy (rehash) finds it.
 * s5 (JLS 15.12.2): phase 1 (subtyping/widening) beats phase 2 (boxing) beats phase 3 (varargs): m(5), m(byte), m(char) -> m(long);
 *    Integer -> m(Integer); no args -> varargs; String -> m(Object); n(5) widens to double but n(Integer) picks Object (subtyping first);
 *    p(o) uses the STATIC type (Object); p(null) picks the most specific (String).
 * s6: subList writes through; a structural change of the parent invalidates the sublist (CME on any later use); unmodifiableList is a
 *    live view (shows 10); PriorityQueue toString is heap order, not sorted; Arrays.asList(int[]) is a one-element List<int[]>;
 *    access-ordered LinkedHashMap moves a get() key to the end, and putIfAbsent on an existing key counts as access (b moves after a);
 *    TreeSet gives sorted order.
 */
public class H1Predictions {
    public static String s1() { return "As Bs Ai A(int) B.init v=0 A() Bi B() v=7 Ai A(int) B.init v=0 A() Bi B() v=7"; }
    public static String s2() { return "true,false,true,false,true,true,false,true,true,false"; }
    public static String s3() { return "2,1,ab,t0f0f1t2f2f3"; }
    public static String s4() { return "one,null,null,null,2,true,true"; }
    public static String s5() { return "long,long,long,Integer,varargs,long,Object,double,Object,String,Object,String"; }
    public static String s6() { return "[1, 20, 3, 4, 5] CME 10 [1, 2, 4, 5, 3] 12345 1 [c, a, b] [apple, banana, cherry]"; }
}
