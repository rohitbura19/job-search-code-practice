package practice.d20261108.h1;

/**
 * Reference predictions. Why: s1 resources close in reverse order, close failures are suppressed onto the body exception,
 * finally runs last. s2 binary numeric promotion makes (true ? 1 : 2.0) a double; char += narrows implicitly; abs(MIN) overflows;
 * NaN != NaN but Double.equals(NaN) is true; casts saturate. s3 remove(int) is by index, unmodifiableList is a live view,
 * Arrays.asList is fixed-size, List.of rejects null even in contains, subList is invalidated by a structural change of its parent.
 * s4 fields are hidden not overridden, statics and private methods bind to the static type, overloads: widening, then boxing, then varargs.
 * s5 split drops trailing empty strings (but "".split gives [""]), replaceAll takes a regex. s6 streams are lazy and element-at-a-time,
 * sorted() is a barrier, a stream can be consumed once.
 */
public class H1Predictions {
    public static String s1() { return "open a open b body close b close a caught body-ex suppressed=[close-b, close-a] finally"; }
    public static String s2() { return "1.0,1,98,b,b,-56,4464,-2147483648,-3,-1,1,false,false,true,Infinity,2147483647,0,-2147483648,4.0,195,ab"; }
    public static String s3() { return "[10, 30, 2, 99] 4 [9, 2, 3] UOE NPE 1 [1, 20, 3, 4, 5, 6] CME"; }
    public static String s4() { return "P,Q Q.who/P/P.st,P.priv,Q.priv,P.st String,Object,String,long,long,long,Integer,varargs,long,Object"; }
    public static String s5() { return "4|2|1|4|a-b|---|el0|bYXa1cd|1|2|386|truefalse"; }
    public static String s6() { return "p5 f5 m5 =10 a3 a1 a2 b1 [16, 32] ISE {false=4, true=6}"; }
}
