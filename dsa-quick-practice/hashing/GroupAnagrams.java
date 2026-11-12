package hashing;

import java.util.List;

// Group Anagrams  [Medium | HashMap with sorted-key or count-key]
// Group words that are anagrams of each other (any order).
//
// [eat,tea,tan,ate,nat,bat] -> [[eat,tea,ate],[tan,nat],[bat]]

public class GroupAnagrams {

    static List<List<String>> groupAnagrams(String[] words) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
        // [[eat, tea, ate], [tan, nat], [bat]]  (any order)
    }
}
