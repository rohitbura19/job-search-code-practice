package strings;

// String Compression  [Medium | Read/write pointers, in place]
// Each run -> char + count (if > 1). Return new length. O(1) extra space.
//
// [a,a,b,b,c,c,c]         -> 6, "a2b2c3"
// [a,b,b,b,b,b,b,b,b,b,b,b,b] -> 4, "ab12"

public class StringCompression {

    static int compress(char[] chars) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        char[] a = "aabbccc".toCharArray();
        int n = compress(a);
        System.out.println(n + " " + new String(a, 0, n)); // 6 a2b2c3
        char[] b = "abbbbbbbbbbbb".toCharArray();
        int m = compress(b);
        System.out.println(m + " " + new String(b, 0, m)); // 4 ab12
    }
}
