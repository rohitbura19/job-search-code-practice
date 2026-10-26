package graphs;

import java.util.List;

// Accounts Merge  [Medium | Union-Find]
// Each account is [name, emails...]. Accounts sharing an email are the same person.
// Return [name, sorted emails...] per person.
//
// [John,a,b] [John,a,c] [Mary,m] [John,j] -> [John,a,b,c] [Mary,m] [John,j]  (any order)

public class AccountsMerge {

    static List<List<String>> accountsMerge(List<List<String>> accounts) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        List<List<String>> accounts = List.of(
                List.of("John", "a", "b"),
                List.of("John", "a", "c"),
                List.of("Mary", "m"),
                List.of("John", "j"));
        System.out.println(accountsMerge(accounts)); // [[John, a, b, c], [Mary, m], [John, j]]
    }
}
