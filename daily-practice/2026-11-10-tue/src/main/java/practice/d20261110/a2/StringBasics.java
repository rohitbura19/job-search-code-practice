package practice.d20261110.a2;

public class StringBasics {
    /**
     * Run-length compress in place: each run of a char becomes the char followed by the run length if the run is longer than 1
     * (length written as decimal digits, e.g. 12 -> '1','2'). Returns the new length; chars[0..len) holds the result. O(1) extra space.
     * ['a','a','b','b','c','c','c'] -> "a2b2c3", returns 6. 12 times 'x' -> "x12".
     */
    public static int compress(char[] chars) { throw new UnsupportedOperationException("TODO"); }

    /** Valid dotted IPv4: exactly 4 parts, each decimal 0..255, no leading zeros ("0" is fine, "01" is not), digits only, no empty parts. */
    public static boolean isValidIPv4(String s) { throw new UnsupportedOperationException("TODO"); }

    /** Sum of two non-negative decimal integers given as strings (no leading zeros except "0"). No BigInteger / Integer.parseInt on the whole string. */
    public static String addStrings(String a, String b) { throw new UnsupportedOperationException("TODO"); }
}
