package design;

// Kth Largest Element in a Stream  [Easy | Min-heap of size k]
// add(val) returns the current k-th largest. O(log k).
//
// k=3, [4,5,8,2]: add(3)=4, add(5)=5, add(10)=5, add(9)=8, add(4)=8

public class KthLargestInStream {

    public KthLargestInStream(int k, int[] nums) {
        // TODO
    }

    int add(int val) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        KthLargestInStream s = new KthLargestInStream(3, new int[]{4, 5, 8, 2});
        System.out.println(s.add(3));  // 4
        System.out.println(s.add(5));  // 5
        System.out.println(s.add(10)); // 5
        System.out.println(s.add(9));  // 8
        System.out.println(s.add(4));  // 8
    }
}
