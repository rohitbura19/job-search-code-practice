package design;

// Moving Average from Data Stream  [Easy | Circular buffer + running sum]
// Average of the last `size` values. O(1) per call; sum must not overflow.
//
// size 3: next(1)=1.0, next(10)=5.5, next(3)=4.666..., next(5)=6.0

public class MovingAverage {

    public MovingAverage(int size) {
        // TODO
    }

    double next(int val) {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        MovingAverage m = new MovingAverage(3);
        System.out.println(m.next(1));  // 1.0
        System.out.println(m.next(10)); // 5.5
        System.out.println(m.next(3));  // 4.666666666666667
        System.out.println(m.next(5));  // 6.0
    }
}
