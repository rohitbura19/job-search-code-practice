package greedy;

import java.util.List;

// Partition Labels  [Medium | Last occurrence + extend end]
// Cut into as many parts as possible so each letter is in one part. Return part sizes.
//
// "ababcbacadefegdehijhklij" -> [9,7,8]
// "eccbbbbdec"               -> [10]

public class PartitionLabels {

    static List<Integer> partitionLabels(String s) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(partitionLabels("ababcbacadefegdehijhklij")); // [9, 7, 8]
        System.out.println(partitionLabels("eccbbbbdec"));               // [10]
    }
}
