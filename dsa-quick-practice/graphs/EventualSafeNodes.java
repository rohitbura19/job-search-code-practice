package graphs;

import java.util.List;

// Find Eventual Safe States  [Medium | Reverse graph + Kahn]
// A node is safe if every path from it ends at a terminal node. Return safe nodes ascending.
//
// [[1,2],[2,3],[5],[0],[5],[],[]] -> [2,4,5,6]

public class EventualSafeNodes {

    static List<Integer> eventualSafeNodes(int[][] graph) {
        // TODO
        return List.of();
    }

    public static void main(String[] args) {
        System.out.println(eventualSafeNodes(new int[][]{{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}})); // [2, 4, 5, 6]
    }
}
