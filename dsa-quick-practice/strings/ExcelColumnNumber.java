package strings;

// Excel Sheet Column Number  [Easy | Base 26]
// Inverse of ExcelColumnTitle.
//
// "A"  -> 1
// "AB" -> 28
// "ZY" -> 701

public class ExcelColumnNumber {

    static int titleToNumber(String title) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(titleToNumber("A"));  // 1
        System.out.println(titleToNumber("AB")); // 28
        System.out.println(titleToNumber("ZY")); // 701
    }
}
