package practice.d20261030.h1;

/**
 * Why, per snippet:
 * s1  JLS 12.4/12.5: static initialisers run once, superclass first; per instance: super constructor (after super's instance
 *     initialisers) then the subclass instance initialisers and field initialisers in text order. The overridden init() runs from
 *     Base's constructor BEFORE Derived's field initialisers: name is still null there; n = 5 is assigned and survives because
 *     `private int n;` has no initialiser (an explicit `= 0` would have reset it).
 * s2  compound assignment casts implicitly: (byte)130 = -126; char + int is int (100) but (char) gives 'd'; (short)40000 = -25536;
 *     int overflow wraps, Math.abs(MIN_VALUE) stays negative; division truncates toward zero and % takes the dividend's sign;
 *     shift distance is masked (1 << 33 == 1 << 1); (int) of a too-large double saturates, of NaN is 0; float 0.1f+0.2f rounds
 *     to 0.3f but the double sum does not; 1 / 2 is int division.
 * s3  equals without hashCode: two equal Keys land in different buckets so the set holds 2 (not guaranteed, but identity hashes
 *     differ in practice); a weak hashCode is still correct (Id(1) and Id(3) collide, equals separates them, the duplicate Id(1) is
 *     dropped -> size 2). "ja"+"va" is a compile-time constant (pooled), x + "va" is built at run time (new object) until intern().
 *     Arrays.asList(int[]) is a List<int[]> of one element. List.contains uses equals, StringBuilder has none.
 * s4  Arrays.asList: set ok, add -> UOE; unmodifiableList is a VIEW (sees the 4); List.of(...).contains(null) throws NPE; TreeMap
 *     rejects a null key, HashMap accepts one; PriorityQueue.toString shows heap order [1, 2, 4, 5, 3] (heapify of a collection),
 *     only poll() is sorted; Deque push = addFirst, offerFirst puts 3 in front.
 * s5  finally's return replaces the earlier one (2); the value to return is captured before finally runs (1); an exception thrown
 *     from finally replaces the one in flight, so the IllegalArgumentException reaches the RuntimeException handler; continue still
 *     runs finally.
 * s6  streams are pulled one element at a time through the whole pipeline; limit(2) cancels after the second result (no p5);
 *     a pipeline with no terminal operation does nothing; sorted() is a barrier but findFirst then only pulls one element past it.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "Bs Ds Bi Bc Dinit:null0 Di Dc:set5 | Bi Bc Dinit:null0 Di Dc:set5 "; }
    public static String s2() { return "-126 c 100 d -25536 -2147483648 -2147483648 -3 -1 2 1 15 -4 2 8589934592 3 -3 2147483647 0 false true 0.0 1.0"; }
    public static String s3() { return "2 2 true true false true true 1 3 true true false true"; }
    public static String s4() { return "UOE [1, 2, 3, 4] [9, 1, 2] NPE NPE {null=11} [1, 2, 4, 5, 3] 12345 [3, 1, 2, 4] 34"; }
    public static String s5() { return "2 1 RE from finally t0f0f1t2f2 ClassCastException"; }
    public static String s6() { return "p1 f1 p2 f2 m2 p3 f3 p4 f4 m4 [20, 40] | built | s7 7 | 31 abc 0"; }
}
