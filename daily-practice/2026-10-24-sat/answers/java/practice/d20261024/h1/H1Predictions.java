package practice.d20261024.h1;

/**
 * Why, per snippet:
 * 1. Integer.valueOf caches -128..127 (JLS 5.1.7), so 127 == 127 but 128 != 128; equals compares type AND value, so Long(127).equals(Integer 127)
 *    is false, while `l == 127` unboxes (numeric promotion). `new String` is a fresh object; intern() returns the pooled literal.
 * 2. remove(int) is chosen for an int argument (index), remove(Object) needs a boxed value. The enhanced for loop checks hasNext()
 *    (cursor != size) BEFORE the modCount check in next(): removing the second-to-last element makes hasNext() false, so no CME;
 *    removing any other element throws on the next call to next(). Use Iterator.remove or removeIf.
 * 3. Static initialisers run once, parent first (JLS 12.4); per instance: parent instance initialisers and constructor, THEN the
 *    child's field initialisers and instance blocks in source order. The overridden describe() runs while Derived's fields are still
 *    default (tag=null), but the final constant `fixed = 7` is a compile-time constant and is inlined.
 * 4. Compound assignment casts implicitly (120+10 wraps to -126, 'a' += 1 stays a char); char + int is int; big + 1 overflows in int
 *    BEFORE widening; abs(MIN_VALUE) is MIN_VALUE; the double -> int cast saturates; 0.1 + 0.2 != 0.3; / truncates toward zero, % takes
 *    the dividend's sign; 1 / 2 is integer division done first.
 * 5. split drops trailing empty strings (not leading ones) unless the limit is negative; "".split gives [""]; replaceAll takes a regex
 *    where "." matches every char; compareTo is the difference of the first differing chars; + is left-associative so "a" + 1 + 2 is
 *    "a12" but 1 + 2 + "a" is "3a".
 * 6. Without hashCode the default identity hash puts equal keys in different buckets, so the lookup misses even though equals is true;
 *    with a legal but terrible hashCode (id % 2) the map is still correct, only slow (buckets become trees when keys are Comparable,
 *    otherwise long chains); a HashSet of two equal-but-not-hashing objects keeps both.
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "true,false,true,false,true,false,true"; }
    public static String s2() { return "[10, 30, 2] [2, 3] no-throw [a, c] CME [b, c]"; }
    public static String s3() {
        return "Base.static|Derived.static|Base.init|Base() sees derived tag=null fixed=7|Derived.init|Derived() tag=set"
                + "|Base.init|Base() sees derived tag=null fixed=7|Derived.init|Derived() tag=set";
    }
    public static String s4() { return "-126,b,99,c,2147483648,-2147483648,-2147483648,2147483647,false,-3,-1,2,0.0"; }
    public static String s5() { return "2,4,4,1,xxx,axb,true,1,1,null,a12,3a"; }
    public static String s6() { return "null,false,true,6,again,null,2"; }
}
