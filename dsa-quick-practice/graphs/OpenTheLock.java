package graphs;

// Open the Lock  [Medium | BFS over 10^4 states]
// "0000".."9999"; one move turns one wheel by 1 (wraps). Fewest moves to target avoiding deadends, or -1.
//
// [0201,0101,0102,1212,2002], "0202" -> 6
// [8888], "0009"                     -> 1
// [0000], "8888"                     -> -1

public class OpenTheLock {

    static int openLock(String[] deadends, String target) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(openLock(new String[]{"0201", "0101", "0102", "1212", "2002"}, "0202")); // 6
        System.out.println(openLock(new String[]{"8888"}, "0009"));                               // 1
        System.out.println(openLock(new String[]{"0000"}, "8888"));                               // -1
    }
}
