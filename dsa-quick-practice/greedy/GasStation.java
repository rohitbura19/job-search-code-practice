package greedy;

// Gas Station  [Medium | Reset start when tank goes negative]
// Circular route. Start index that completes the loop, or -1. O(n).
//
// gas [1,2,3,4,5], cost [3,4,5,1,2] -> 3
// gas [2,3,4],     cost [3,4,3]     -> -1

public class GasStation {

    static int canCompleteCircuit(int[] gas, int[] cost) {
        // TODO
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(canCompleteCircuit(new int[]{1, 2, 3, 4, 5}, new int[]{3, 4, 5, 1, 2})); // 3
        System.out.println(canCompleteCircuit(new int[]{2, 3, 4}, new int[]{3, 4, 3}));             // -1
    }
}
