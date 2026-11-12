package practice.d20261112.c1;

import java.util.*;

/** Union-find with path halving + union by size: near O(1) amortised (inverse Ackermann) per operation, O(n) space. */
public class UnionFindProblems {
    static final class DSU {
        final int[] parent, size;
        int components;
        DSU(int n) {
            parent = new int[n]; size = new int[n]; components = n;
            for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
        }
        int find(int x) {
            while (parent[x] != x) { parent[x] = parent[parent[x]]; x = parent[x]; }
            return x;
        }
        boolean union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return false;
            if (size[ra] < size[rb]) { int t = ra; ra = rb; rb = t; }
            parent[rb] = ra; size[ra] += size[rb]; components--;
            return true;
        }
    }

    public static int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        DSU d = new DSU(n);
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) if (isConnected[i][j] == 1) d.union(i, j);
        return d.components;
    }

    public static int[] findRedundantConnection(int[][] edges) {
        DSU d = new DSU(edges.length + 1);
        for (int[] e : edges) if (!d.union(e[0], e[1])) return e;
        return new int[0];
    }

    public static List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Integer> owner = new HashMap<>();
        DSU d = new DSU(accounts.size());
        for (int i = 0; i < accounts.size(); i++) {
            for (String email : accounts.get(i).subList(1, accounts.get(i).size())) {
                Integer prev = owner.putIfAbsent(email, i);
                if (prev != null) d.union(prev, i);
            }
        }
        Map<Integer, TreeSet<String>> groups = new HashMap<>();
        owner.forEach((email, i) -> groups.computeIfAbsent(d.find(i), k -> new TreeSet<>()).add(email));
        List<List<String>> out = new ArrayList<>();
        groups.forEach((root, emails) -> {
            List<String> row = new ArrayList<>();
            row.add(accounts.get(root).get(0));
            row.addAll(emails);
            out.add(row);
        });
        out.sort(Comparator.<List<String>, String>comparing(r -> r.get(0)).thenComparing(r -> r.get(1)));
        return out;
    }
}
