package practice.d20261028.h1;

/**
 * Reasons (JLS):
 * 1. Class init (12.4): superclass static blocks, then subclass static blocks, ONCE. Per instance (12.5): super constructor first,
 *    and inside it field initialisers and instance blocks in textual order, then its body; only after super() returns do the
 *    subclass initialisers run. So Base's constructor calls the overridden show() while w == 0 and name == null.
 * 2. Compound assignment casts implicitly: c += 1 stays a char, b += 300 is (byte) 310 = 54. char + int is int ('c'+1 = 100),
 *    "" + 'a' + 'b' concatenates but 'a' + 'b' = 195. i++ + ++i = 5 + 7; j += j++ * 2 reads the OLD j (3) first, so 3 + 6;
 *    k = k++ discards the increment. Shift distance is masked to 5 bits for int (1 << 33 == 1 << 1), arithmetic >> keeps the
 *    sign, >>> does not. Integer division truncates toward zero, % takes the dividend's sign, floorMod is non-negative.
 *    1 / 2 * 2.0 is (0) * 2.0.
 * 3. remove(int) is by index (20 gone), remove(Object) by equality (1 gone). Arrays.asList(int[]) is a List<int[]> of size 1.
 *    Integer.valueOf caches -128..127 so 127 == 127 but 128 != 128. A mutated HashSet key is lost (stored under the old hash):
 *    contains(key) false, contains(List.of(1)) false (hash matches, equals does not), size stays 1. PriorityQueue's toString is
 *    the heap array, only poll() order is sorted. push() adds at the head, addLast at the tail.
 * 4. split drops trailing empty strings unless the limit is negative; a leading empty string stays. Compile-time constants are
 *    interned ("a" + "b" and fy + "b" with a final constant), a runtime concatenation is a new object unless intern()ed.
 *    compareTo is the first differing char difference, else the length difference. "" + null + 1 -> "null1". replaceAll takes a regex.
 * 5. Erasure: both lists share one class. The unchecked add(42) succeeds (heap pollution) and the CCE appears only where the
 *    compiler inserted a cast (the assignment to String). Arrays are reified and covariant, so a wrong store fails at runtime
 *    (ArrayStoreException). Collection factories return private classes (Arrays$ArrayList; List.of(1) and List.of(1, 2) share List12).
 * 6. Without break, switch falls through; default may appear anywhere (it is taken only if no case matches) and still falls
 *    into the next label. `continue outer` jumps to the next i, `break outer` leaves both loops. && and || short-circuit, & and | do not.
 *    x = x++ + 1 keeps the incremented value only through the +1: x goes 0 -> 1 -> 2 -> 3. Pattern variable n is in scope in the && and the true branch.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "Bs Ds Bf Bi Bc Dshow(0,null) Df Di Dc | Bf Bi Bc Dshow(0,null) Df Di Dc"; }
    public static String s2() { return "c100d 54 12 9 7 195ab 2 8589934592 -4 15 -3 -2 3 0.0 1.0"; }
    public static String s3() { return "[10, 30] 1 2 true false true false false 1 [1, 2, 4, 5, 3] 12345 [2, 1, 3] 23"; }
    public static String s4() { return "2 4 2 true false true true -1 1 -2 null1 4 2 true 1 a+b+c xxxxx"; }
    public static String s5() { return "true CCE 1 ASE Integer int[] true ArrayList true"; }
    public static String s6() { return "abbdcc midweek 00,01,10,11, 2falsetruefalsetrue 3 big42"; }
}
