package hashing;

import java.util.Arrays;

// Top K Frequent Elements  [Medium | Count + bucket sort]
// The k most frequent values (any order). Better than O(n log n).
//
// [1,1,1,2,2,3], k=2 -> [1,2]
// [1], k=1           -> [1]

public class TopKFrequentElements {

    static int[] topKFrequent(int[] nums, int k) {
        // TODO
        return new int[0];
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2))); // [1, 2]
        System.out.println(Arrays.toString(topKFrequent(new int[]{1}, 1)));                // [1]
    }
}
