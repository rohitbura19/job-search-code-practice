package heap;

// Find Median from Data Stream  [Hard | Two heaps]
// addNum in O(log n), findMedian in O(1).
//
// add 2, 3 -> 2.5;  add 4 -> 3.0

public class MedianFinder {

    public MedianFinder() {
        // TODO
    }

    void addNum(int num) {
        // TODO
    }

    double findMedian() {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        MedianFinder m = new MedianFinder();
        m.addNum(2);
        m.addNum(3);
        System.out.println(m.findMedian()); // 2.5
        m.addNum(4);
        System.out.println(m.findMedian()); // 3.0
    }
}
