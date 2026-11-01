package practice.d20261101.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BacktrackingTest {
    private static Set<List<Integer>> bruteSubsets(int[] a) {
        Set<List<Integer>> set = new HashSet<>();
        for (int mask = 0; mask < (1 << a.length); mask++) {
            List<Integer> s = new ArrayList<>();
            for (int i = 0; i < a.length; i++) if ((mask >> i & 1) == 1) s.add(a[i]);
            s.sort(null);
            set.add(s);
        }
        return set;
    }

    @Test void subsetsExamples() {
        var r = Backtracking.subsetsWithDup(new int[] {1, 2, 2});
        assertEquals(6, r.size());
        assertEquals(Set.of(List.of(), List.of(1), List.of(2), List.of(1, 2), List.of(2, 2), List.of(1, 2, 2)), new HashSet<>(r));
        assertEquals(List.of(List.of()), Backtracking.subsetsWithDup(new int[0]));
        assertEquals(2, Backtracking.subsetsWithDup(new int[] {0}).size());
    }

    @Test void subsetsDoNotMutateInputAndHandleUnsortedInput() {
        int[] in = {2, 1, 2};
        var r = Backtracking.subsetsWithDup(in);
        assertArrayEquals(new int[] {2, 1, 2}, in);
        assertEquals(6, r.size());
    }

    @Test void subsetsRandomAgainstBruteForce() {
        Random rnd = new Random(11);
        for (int t = 0; t < 200; t++) {
            int[] a = rnd.ints(rnd.nextInt(9), -2, 3).toArray();
            var got = Backtracking.subsetsWithDup(a);
            assertEquals(got.size(), new HashSet<>(got).size(), "duplicates for " + Arrays.toString(a));
            assertEquals(bruteSubsets(a), new HashSet<>(got), Arrays.toString(a));
        }
    }

    @Test void parenthesesSmall() {
        assertEquals(List.of(""), Backtracking.generateParenthesis(0));
        assertEquals(List.of("()"), Backtracking.generateParenthesis(1));
        var three = new ArrayList<>(Backtracking.generateParenthesis(3));
        three.sort(null);
        assertEquals(List.of("((()))", "(()())", "(())()", "()(())", "()()()"), three);
    }

    @Test void parenthesesCatalanAndValid() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            var r = Backtracking.generateParenthesis(12);
            assertEquals(208_012, r.size());
            assertEquals(208_012, new HashSet<>(r).size());
            for (String s : r.subList(0, 2000)) {
                int bal = 0;
                for (char c : s.toCharArray()) { bal += c == '(' ? 1 : -1; assertTrue(bal >= 0); }
                assertEquals(0, bal);
            }
        });
    }

    @Test void queens() {
        int[] expected = {1, 1, 0, 0, 2, 10, 4, 40, 92, 352, 724};
        for (int n = 1; n <= 10; n++) assertEquals(expected[n], Backtracking.totalNQueens(n), "n=" + n);
    }

    @Test void queens12Timeout() {
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> assertEquals(14_200, Backtracking.totalNQueens(12)));
    }
}
