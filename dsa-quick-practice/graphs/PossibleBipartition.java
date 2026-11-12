package graphs;

// Possible Bipartition  [Medium | Build graph, 2-colour]
// People 1..n; dislikes[i] = {a, b} can't share a group. Split into two groups?
//
// n=4, [[1,2],[1,3],[2,4]] -> true
// n=3, [[1,2],[1,3],[2,3]] -> false

public class PossibleBipartition {

    static boolean possibleBipartition(int n, int[][] dislikes) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(possibleBipartition(4, new int[][]{{1, 2}, {1, 3}, {2, 4}})); // true
        System.out.println(possibleBipartition(3, new int[][]{{1, 2}, {1, 3}, {2, 3}})); // false
    }
}
