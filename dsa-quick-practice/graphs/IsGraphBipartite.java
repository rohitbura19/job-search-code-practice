package graphs;

// Is Graph Bipartite?  [Medium | BFS 2-colouring]
// graph[u] lists neighbours (undirected, maybe disconnected). Can nodes be 2-coloured?
//
// [[1,2,3],[0,2],[0,1,3],[0,2]] -> false
// [[1,3],[0,2],[1,3],[0,2]]     -> true

public class IsGraphBipartite {

    static boolean isBipartite(int[][] graph) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        System.out.println(isBipartite(new int[][]{{1, 2, 3}, {0, 2}, {0, 1, 3}, {0, 2}})); // false
        System.out.println(isBipartite(new int[][]{{1, 3}, {0, 2}, {1, 3}, {0, 2}}));       // true
    }
}
