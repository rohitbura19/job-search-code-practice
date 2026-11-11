package practice.d20261111.h1;

/** Reference predictions, produced by running H1Snippets (JDK 21). Rules are in the README review. */
public class H1Predictions {
    public static String s1() { return "Derived.static | Base.init | Base() sees Derived.describe name=null size=7 tag=base-field | Derived.init name=derived-field | Derived() sees Derived.describe name=derived-field size=7 tag=base-field | Base.init | Base() sees Derived.describe name=null size=7 tag=base-field | Derived.init name=derived-field | Derived() sees Derived.describe name=derived-field size=7 tag=base-field"; }
    public static String s2() { return "true false true false true true 1.0 1 b 98 NPE [2]"; }
    public static String s3() { return "2 1 ab IllegalArgumentException:finally:0 fell through"; }
    public static String s4() { return "int int int int long Integer Object int... int... | String Object Object StringBuilder String | char double double double long,int"; }
    public static String s5() { return "false false false 1 true false true [a, bb, ddd] true [z, x, y] [1, 2, 3] -4 30817 30817 true"; }
    public static String s6() { return "onetwo,two,three,dfltthree int5,big50,str3,null!,other 4 6 2 1 a-b-c ----- null true 1 -1 1 text xxx p-q 386"; }
}
