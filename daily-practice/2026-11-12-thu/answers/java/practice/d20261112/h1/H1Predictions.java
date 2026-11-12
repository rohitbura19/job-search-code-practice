package practice.d20261112.h1;

/** Values produced by running the snippets on JDK 21. Why: see the H1 bullet in README.md. */
public class H1Predictions {
    public static String s1() { return "lazy [Config.init] 5 [Config.init, Lazy.init] 10 20"; }
    public static String s2() { return "true false true false true true 12 3 195c cab"; }
    public static String s3() { return "ASE java.lang.Integer 1 CCE true Integer Double"; }
    public static String s4() { return "[10, 30, 2] UOE [z, b, c] [1, 99, 3, 4, 55] CME [1, 3] NPE true null 8 13"; }
    public static String s5() { return "built,p1,p2,m2,first=20,compute-eager,vv0142"; }
    public static String s6() { return "open A | open B | body | close B | close A | body-fail suppressed=[close-B, close-A] | finally | open C | body2 | close C | close-C suppressed=0"; }
}
