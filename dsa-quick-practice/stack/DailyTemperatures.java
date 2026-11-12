package stack;

import java.util.Arrays;

// Daily Temperatures  [Medium | Monotonic stack of indices]
// out[i] = days until a strictly warmer day, 0 if none. O(n).
//
// [73,74,75,71,69,72,76,73] -> [1,1,4,2,1,1,0,0]
// [30,40,50,60]             -> [1,1,1,0]

public class DailyTemperatures {

    static int[] dailyTemperatures(int[] temps) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}))); // [1, 1, 4, 2, 1, 1, 0, 0]
        System.out.println(Arrays.toString(dailyTemperatures(new int[]{30, 40, 50, 60})));                 // [1, 1, 1, 0]
    }
}
