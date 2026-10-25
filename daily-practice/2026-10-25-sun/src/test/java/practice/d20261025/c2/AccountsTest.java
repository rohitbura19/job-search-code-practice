package practice.d20261025.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class AccountsTest {
    static List<String> acc(String... s) { return List.of(s); }

    @Test void example() {
        List<List<String>> in = List.of(
                acc("John", "johnsmith@mail.com", "john_newyork@mail.com"),
                acc("John", "johnsmith@mail.com", "john00@mail.com"),
                acc("Mary", "mary@mail.com"),
                acc("John", "johnnybravo@mail.com"));
        assertEquals(List.of(
                acc("John", "john00@mail.com", "john_newyork@mail.com", "johnsmith@mail.com"),
                acc("John", "johnnybravo@mail.com"),
                acc("Mary", "mary@mail.com")), Accounts.merge(in));
    }

    @Test void transitiveChainAndDuplicatesInsideAnAccount() {
        List<List<String>> in = List.of(acc("A", "a@x", "b@x"), acc("A", "c@x", "d@x"), acc("A", "b@x", "c@x", "c@x"));
        assertEquals(List.of(acc("A", "a@x", "b@x", "c@x", "d@x")), Accounts.merge(in));
    }

    @Test void edgeCases() {
        assertEquals(List.of(), Accounts.merge(List.of()));
        assertEquals(List.of(acc("Solo")), Accounts.merge(List.of(acc("Solo"))));
        assertEquals(List.of(acc("A", "a@x"), acc("B", "b@x")), Accounts.merge(List.of(acc("B", "b@x"), acc("A", "a@x"))));
        List<List<String>> in = new ArrayList<>(List.of(acc("A", "b@x", "a@x")));
        Accounts.merge(in);
        assertEquals(acc("A", "b@x", "a@x"), in.get(0), "input untouched");
    }

    @Test void redundantLinks() {
        assertEquals(0, Accounts.redundantLinks(List.of(acc("A", "a@x"), acc("B", "b@x"))));
        assertEquals(0, Accounts.redundantLinks(List.of(acc("A", "a@x"), acc("A", "a@x"))));
        // three accounts all sharing x and y: the second shared email of the third account closes a cycle
        assertEquals(1, Accounts.redundantLinks(List.of(acc("A", "x", "y"), acc("A", "x"), acc("A", "x", "y"))));
        assertEquals(1, Accounts.redundantLinks(List.of(acc("A", "x", "y"), acc("A", "x", "y"))));
    }

    @Test void matchesGraphSearchOnRandomInput() {
        Random rnd = new Random(6);
        for (int it = 0; it < 500; it++) {
            int n = 1 + rnd.nextInt(8);
            List<List<String>> in = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                List<String> a = new ArrayList<>(List.of("Pat"));
                for (int k = rnd.nextInt(4); k > 0; k--) a.add("e" + rnd.nextInt(10));
                in.add(a);
            }
            assertEquals(reference(in), Accounts.merge(in), in.toString());
        }
    }

    /** Repeated merging of overlapping sets until nothing changes. */
    static List<List<String>> reference(List<List<String>> in) {
        List<String> names = new ArrayList<>();
        List<TreeSet<String>> sets = new ArrayList<>();
        for (List<String> a : in) {
            names.add(a.get(0));
            sets.add(new TreeSet<>(a.subList(1, a.size())));
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            outer:
            for (int i = 0; i < sets.size(); i++)
                for (int j = i + 1; j < sets.size(); j++)
                    if (!Collections.disjoint(sets.get(i), sets.get(j))) {
                        sets.get(i).addAll(sets.get(j));
                        sets.remove(j);
                        names.remove(j);
                        changed = true;
                        break outer;
                    }
        }
        List<List<String>> out = new ArrayList<>();
        for (int i = 0; i < sets.size(); i++) {
            List<String> r = new ArrayList<>();
            r.add(names.get(i));
            r.addAll(sets.get(i));
            out.add(r);
        }
        out.sort(Comparator.comparing((List<String> r) -> r.size() > 1 ? r.get(1) : "").thenComparing(r -> r.get(0)));
        return out;
    }

    @Test void largeInput() {
        int n = 200_000;
        List<List<String>> in = new ArrayList<>(n);
        for (int i = 0; i < n; i++) in.add(acc("U", "e" + i, "e" + (i + 1)));   // one long chain
        assertTimeoutPreemptively(Duration.ofSeconds(4), () -> {
            List<List<String>> out = Accounts.merge(in);
            assertEquals(1, out.size());
            assertEquals(n + 2, out.get(0).size());
        });
    }
}
