package heap;

import java.util.List;

// Top K Frequent Words  [Medium | HashMap + heap of size k]
// k most frequent words, by frequency desc; ties alphabetical.
//
// ["i","love","leetcode","i","love","coding"], k=2 -> [i, love]

public class TopKFrequentWords {

    static List<String> topKFrequent(String[] words, int k) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(topKFrequent(new String[]{"i", "love", "leetcode", "i", "love", "coding"}, 2)); // [i, love]
        System.out.println(topKFrequent(new String[]{"the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"}, 4)); // [the, is, sunny, day]
    }
}
