package strings;

// Excel Sheet Column Title  [Easy | Bijective base 26]
// 1 -> A, 26 -> Z, 27 -> AA, 28 -> AB, 701 -> ZY, 703 -> AAA.
// Bonus: write the inverse, number("AB") -> 28.

public class ExcelColumnTitle {

    static String title(int n) {
        // TODO
        return "";
    }

    public static void main(String[] args) {
        System.out.println(title(1));   // A
        System.out.println(title(26));  // Z
        System.out.println(title(28));  // AB
        System.out.println(title(701)); // ZY
        System.out.println(title(703)); // AAA
    }
}
