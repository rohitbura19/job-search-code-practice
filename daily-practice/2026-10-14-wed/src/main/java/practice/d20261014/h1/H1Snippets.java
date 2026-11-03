package practice.d20261014.h1;

import java.util.HashMap;
import java.util.Map;

/**
 * GIVEN CODE - do not edit. Each method runs one puzzle from questions/H1 and returns what it "printed".
 * Read the code, predict the output, and write your prediction into {@link H1Predictions}.
 */
public final class H1Snippets {
    private H1Snippets() {}

    public static String line1() {
        Integer i1 = 127, i2 = 127, i3 = 128, i4 = 128;
        return (i1 == i2) + " " + (i3 == i4) + " " + i3.equals(i4);
    }

    public static String line2() {
        Long L = 127L;
        return L.equals(127) + " " + L.equals(127L) + " " + (L == 127);
    }

    /** Returns "NPE" if the statement throws a NullPointerException, otherwise the value of r. */
    public static String line3() {
        boolean flag = true; Integer nul = null;
        try {
            int r = flag ? nul : 0;
            return String.valueOf(r);
        } catch (NullPointerException e) {
            return "NPE";
        }
    }

    public static String line4() {
        boolean flag = true; Integer nul = null;
        Integer r2 = flag ? nul : Integer.valueOf(0);
        return String.valueOf(r2);
    }

    public static String line5() {
        char ch = 'a'; int zero = 0;
        return (true ? ch : 0) + " " + (true ? ch : zero);
    }

    public static String line6() {
        Object o2 = true ? 1 : 2.0;
        return String.valueOf(o2);
    }

    public static String line7() {
        return String.valueOf(new java.util.ArrayList<String>().getClass() == new java.util.ArrayList<Integer>().getClass());
    }

    /** Only the half that compiles: sh += 1. (Why would "sh = sh + 1" not compile? Answer on paper.) */
    public static String line8() {
        short sh = 1; sh += 1;
        return String.valueOf(sh);
    }

    public static String line9() {
        return (0.1 + 0.2 == 0.3) + " " + Math.abs(Integer.MIN_VALUE) + " " + (Integer.MAX_VALUE + 1);
    }

    public static String line10() {
        return (-7 / 2) + " " + (-7 % 2) + " " + Math.floorMod(-7, 2) + " " + (-7 >> 1) + " " + (-7 >>> 28);
    }

    /** Item 4 and 5: a Map<Integer,String> and a Map<Long,String> queried with the "wrong" boxed type. */
    public static String line11() {
        Map<Integer, String> mi = new HashMap<>(); mi.put(5, "five");
        Map<Long, String> ml = new HashMap<>(); ml.put(5L, "five");
        return mi.get(5L) + " " + mi.get(5) + " " + ml.get(5) + " " + ml.get(5L);
    }
}
