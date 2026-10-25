package practice.d20261025.h1;

/**
 * Why (JLS references in brackets):
 * s1: `true ? 1 : "x"` is a reference conditional, so 1 is boxed (prints 1). `1 : 2.0` -> binary numeric promotion to double (1.0) [15.25].
 *     `false ? 'a' : 98`: 98 is a constant int that fits char, so the type is char -> 'b'. `c : i` with a non-constant int -> int -> 97.
 *     switch falls through without break: k=0 A,B; k=1 B; k=2 C,D; k=3 D.
 * s2: Arrays.asList is a fixed-size write-through view of the array (set works both ways, add throws). subList is a view of the parent;
 *     remove(Object) on it edits the parent. unmodifiableList is a live read-only view (sees add), List.copyOf is a snapshot.
 *     A structural change of the parent (add) invalidates the subList: modCount mismatch -> CME on the next access.
 * s3: a return in finally overrides (f = 2); g returns the value already evaluated (1) even though x changes after.
 *     Resources close in reverse order (B, A); the body exception is primary and the two close exceptions are suppressed [14.20.3].
 * s4: phase 1 (no boxing, no varargs): m(5) widens int to long; m(Integer) matches by subtyping; the varargs method is the last resort.
 *     n(null): the most specific applicable overload is String. p(5), p(short), p(char), p(int) widen to double in phase 1; p(Integer)
 *     is applicable by subtyping to Object in phase 1, before unboxing is considered [15.12.2].
 * s5: 0.1+0.2 != 0.3 in double; in float the rounded sums happen to be equal. NaN != NaN but Double.equals(NaN) is true; 0.0 == -0.0 but
 *     Double.equals distinguishes them. (int) of a too-big double saturates; (int) -2.7 truncates; (byte) 200 = -56; (long) Float.MAX_VALUE
 *     saturates; Math.round(-2.5) = -2 (floor(x + 0.5)), round(2.5) = 3; Integer.MAX_VALUE * 2 overflows to -2.
 * s6: streams are lazy and process element by element; limit(2) short-circuits after the second odd element (3), so 2 and 1 are never
 *     visited. Nothing runs until the terminal operation (built before L1L2); anyMatch stops at 2. A stream cannot be reused: ISE.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "1|1.0|b|97|ABBCDD"; }
    public static String s2() { return "z UOE [z, y, c] [1, 20, 4, 5] 54 CME"; }
    public static String s3() { return "21 openA openB body closeB closeA body closeB closeA"; }
    public static String s4() { return "long long Integer varargs String Object double Object double double double"; }
    public static String s5() { return "false true false true true false 2147483647 -2 -56 B66 9223372036854775807 -2 3 2.5 Infinity -2 2147483648"; }
    public static String s6() { return "p5m5p4p3m3 [50, 30] built L1L2true ISE"; }
}
