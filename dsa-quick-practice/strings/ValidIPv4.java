package strings;

// Validate IPv4 Address  [Easy | Parse parts]
// Exactly 4 parts, each 0..255, digits only, no leading zeros ("0" ok, "01" not).
//
// "172.16.254.1" -> true
// "256.0.0.1"    -> false
// "01.1.1.1"     -> false
// "1.1.1"        -> false

public class ValidIPv4 {

    static boolean isValidIPv4(String s) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isValidIPv4("172.16.254.1")); // true
        System.out.println(isValidIPv4("256.0.0.1"));    // false
        System.out.println(isValidIPv4("01.1.1.1"));     // false
        System.out.println(isValidIPv4("1.1.1"));        // false
    }
}
