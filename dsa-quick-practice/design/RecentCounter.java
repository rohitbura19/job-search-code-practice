package design;

// Number of Recent Calls  [Easy | Queue]
// ping(t): t strictly increases. Count pings in [t - 3000, t].
//
// ping(1)=1, ping(100)=2, ping(3001)=3, ping(3002)=3

public class RecentCounter {

    public RecentCounter() {
        // TODO
    }

    int ping(int t) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        RecentCounter r = new RecentCounter();
        System.out.println(r.ping(1));    // 1
        System.out.println(r.ping(100));  // 2
        System.out.println(r.ping(3001)); // 3
        System.out.println(r.ping(3002)); // 3
    }
}
