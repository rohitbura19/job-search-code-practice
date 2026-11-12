package design;

// Time Based Key-Value Store  [Medium | Map of key -> sorted versions, binary search]
// get(key, t): value with the largest timestamp <= t, "" if none. O(log k).
//
// set(foo,bar,1); get(foo,1)=bar; get(foo,3)=bar; set(foo,bar2,4); get(foo,4)=bar2; get(foo,5)=bar2; get(foo,0)=""

public class TimeMap {

    public TimeMap() {
        // TODO
    }

    void set(String key, String value, int timestamp) {
        // TODO
    }

    String get(String key, int timestamp) {
        // TODO
        return "";
    }

    public static void main(String[] args) {
        TimeMap m = new TimeMap();
        m.set("foo", "bar", 1);
        System.out.println(m.get("foo", 1)); // bar
        System.out.println(m.get("foo", 3)); // bar
        m.set("foo", "bar2", 4);
        System.out.println(m.get("foo", 4)); // bar2
        System.out.println(m.get("foo", 5)); // bar2
        System.out.println("[" + m.get("foo", 0) + "]"); // []
    }
}
