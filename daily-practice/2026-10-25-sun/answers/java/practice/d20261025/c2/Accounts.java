package practice.d20261025.c2;

import java.util.*;

/**
 * Union-Find over account indexes: the first account that owns an email claims it; a later account with the same email is unioned with the
 * owner. Then group emails by root, sort, prepend the name. O(E alpha(A) + E log E) time, O(E) space (E = total emails).
 * redundantLinks counts unions that find both accounts already connected (each one closes a cycle).
 */
public final class Accounts {
    private Accounts() {}

    private static int find(int[] p, int x) {
        while (p[x] != x) { p[x] = p[p[x]]; x = p[x]; }
        return x;
    }

    private static int[] link(List<List<String>> accounts, int[] redundant) {
        int n = accounts.size();
        int[] p = new int[n], size = new int[n];
        for (int i = 0; i < n; i++) { p[i] = i; size[i] = 1; }
        Map<String, Integer> owner = new HashMap<>();
        for (int i = 0; i < n; i++) {
            List<String> acc = accounts.get(i);
            for (int j = 1; j < acc.size(); j++) {
                Integer o = owner.putIfAbsent(acc.get(j), i);
                if (o == null) continue;
                int a = find(p, o), b = find(p, i);
                if (a == b) { redundant[0]++; continue; }
                if (size[a] < size[b]) { int t = a; a = b; b = t; }
                p[b] = a;
                size[a] += size[b];
            }
        }
        for (int i = 0; i < n; i++) p[i] = find(p, i);
        return p;
    }

    public static List<List<String>> merge(List<List<String>> accounts) {
        int[] root = link(accounts, new int[1]);
        Map<Integer, TreeSet<String>> emails = new HashMap<>();
        for (int i = 0; i < accounts.size(); i++) {
            TreeSet<String> set = emails.computeIfAbsent(root[i], k -> new TreeSet<>());
            set.addAll(accounts.get(i).subList(1, accounts.get(i).size()));
        }
        List<List<String>> out = new ArrayList<>();
        for (var e : emails.entrySet()) {
            List<String> row = new ArrayList<>();
            row.add(accounts.get(e.getKey()).get(0));
            row.addAll(e.getValue());
            out.add(row);
        }
        // a person with no email sorts first (no key); otherwise by the smallest email
        out.sort(Comparator.comparing((List<String> r) -> r.size() > 1 ? r.get(1) : "").thenComparing(r -> r.get(0)));
        return out;
    }

    public static int redundantLinks(List<List<String>> accounts) {
        int[] redundant = new int[1];
        link(accounts, redundant);
        return redundant[0];
    }
}
