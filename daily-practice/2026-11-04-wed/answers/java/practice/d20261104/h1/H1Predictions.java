package practice.d20261104.h1;

/**
 * The expected outputs, with the rule behind each piece.
 * s1: fields and static methods are bound by the static type (JLS 8.3.3 hiding, 15.12.4); instance methods are virtual (me() -> Cm even inside P.call());
 *     overloads are picked at compile time from the static type: p.f("x") sees only f(Object).
 * s2: resources close in reverse order; an exception from close() while another is propagating is attached via addSuppressed (JLS 14.20.3); finally runs last;
 *     with no primary exception, the close() exception is thrown itself (suppressed count 0).
 * s3: split drops trailing empty strings (limit 0) but keeps them with -1; "".split(",") is [""]; replaceAll takes a regex ("." matches any char), replace is literal;
 *     compareTo returns the difference of the first differing chars, else of the lengths; 1 + 2 + "3" is "33" (left to right); indexOf("") is 0, lastIndexOf("") is length.
 * s4: removing the second-to-last element ends the loop silently (hasNext() is false), any other removal fails on the next next(); a subList is invalidated by a
 *     structural change of its parent; PriorityQueue(Collection) heapifies, toString is the array, not sorted order; TreeSet equality is the comparator's;
 *     int[] has identity equals; access-ordered LinkedHashMap moves a read key to the end; unmodifiableList is a view.
 * s5: elements flow one by one through the pipeline and limit short-circuits (no "fd"); a stream is single use; Stream.iterate peeks 1,2,4,8,16 before anyMatch stops;
 *     toMap without a merge function throws on a duplicate key; lambdas capture the effectively-final copy j.
 * s6: += has an implicit narrowing cast; char + int is int, char + char is int (195) but "" + 'a' + 'b' concatenates; narrowing casts keep the low bits (byte 200 -> -56,
 *     short 70000 -> 4464) while double -> int/long saturates; Math.round is floor(x + 0.5) (-2.5 -> -2, 2.5 -> 3, -3.5 -> -3); abs(MIN_VALUE) and MAX_VALUE + 1 overflow;
 *     / truncates toward zero, % takes the dividend's sign, floorMod the divisor's; >> keeps the sign, >>> does not; 0.1 + 0.2 != 0.3 as double but equal as float;
 *     0.0 == -0.0 yet Double.equals distinguishes them; NaN != NaN with == but NaN.equals(NaN); every comparison with NaN is false.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "P,C,P,PPsCm,Po,Cstr,Po"; }

    public static String s2() { return "openA openB body closeB closeA main suppressed=2 cB,cA fin | openC ok closeC caught-cC 0"; }

    public static String s3() { return "2|1|2|4|xxx|axb|-1|1|xnull|null|3345|03|14"; }

    public static String s4() {
        return "[1, 2, 3, 5] ; cme [1, 3, 5] ; sub cme [1, 9, 3, 4, 5, 6] ; [1, 2, 4, 5, 3] ; 12345 ; [a, b] ; 2 ; [b, c, a] ; [1, 2]";
    }

    public static String s5() {
        return "fa fb mb eB fc mc eC | stream has already been operated upon or closed | peeks=5 | Duplicate key 0 (attempted merging values 0 and 2) | 014";
    }

    public static String s6() {
        return "bb98,195ab,-56/4464/2147483647/-3/9223372036854775807,-23-3,-2147483648/-2147483648/true,-3-113-43,falsetrueInfinitytruefalse,falsetruefalsetrue";
    }
}
