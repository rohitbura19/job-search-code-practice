package practice.d20261022.h1;

/**
 * 1: return evaluates x BEFORE finally runs, so f1 is 1; a return inside finally swallows the exception (f2 = 5);
 *    the StringBuilder is a reference, so the appended "c" happens after toString() took "ab".
 * 2: static blocks once per class, parent first; per object: parent instance init and constructor, then the overridden hook runs
 *    BEFORE Derived's field initialisers (n is 0), but `final String tag = "t"` is a constant variable, inlined at compile time.
 * 3: phase 1 widening beats boxing beats varargs: 1, 'a' and (byte)1 widen to long; Integer picks m(Integer); (Object) null picks Object
 *    by the static type; 1.5 cannot become long, so it boxes to Double then widens to Object.
 * 4: Integer cache is -128..127; compile-time constants are interned and "a" + "b" folds to "ab", s + "b" is built at run time;
 *    k is a final constant so k + "b" also folds.
 * 5: (byte)200 wraps to -56; Integer.MAX_VALUE + 1 overflows in int BEFORE widening; 5/2 is 2 so 4.0; -7 % 3 = -1 (sign of dividend);
 *    abs(MIN_VALUE) stays negative; compound assignment hides a narrowing cast (sh = (short)(10 + 5.7) = 15); NaN != NaN.
 * 6: remove(int) vs remove(Object); removing the SECOND-LAST element in a for-each ends the loop silently because hasNext() sees
 *    cursor == size; removing the first element throws ConcurrentModificationException on the next next().
 */
public final class H1Predictions {
    private H1Predictions() {}

    public static String s1() { return "1,5,ab"; }
    public static String s2() { return "Bs Ds Bi Bc Dh(n=0,tag=t) Di Dc | Bi Bc Dh(n=0,tag=t) Di Dc"; }
    public static String s3() { return "long,long,long,Integer,String,Object,varargs,Object,Object"; }
    public static String s4() { return "true,false,true,true,false,true,true,false,true"; }
    public static String s5() { return "-56,-2147483648,2147483648,4.0,98,b,-1,-3,-2147483648,false,15,3,-3,Infinity,false,b98"; }
    public static String s6() { return "[30] [1, 3] CME [2, 3]"; }
}
