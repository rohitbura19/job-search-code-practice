package practice.d20261106.c2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class GreedyProblemsTest {
    @Test void jumpsExamples() {
        assertEquals(2, GreedyProblems.minJumps(new int[]{2, 3, 1, 1, 4}));
        assertEquals(2, GreedyProblems.minJumps(new int[]{2, 3, 0, 1, 4}));
        assertEquals(0, GreedyProblems.minJumps(new int[]{0}));
        assertEquals(0, GreedyProblems.minJumps(new int[]{}));
        assertEquals(1, GreedyProblems.minJumps(new int[]{1, 0}));
        assertEquals(-1, GreedyProblems.minJumps(new int[]{1, 0, 1}));
        assertEquals(-1, GreedyProblems.minJumps(new int[]{0, 5}));
        assertEquals(1, GreedyProblems.minJumps(new int[]{100, 0, 0}));
    }

    @Test void jumpsRandomVsDp() {
        Random r = new Random(8);
        for (int t = 0; t < 1000; t++) {
            int[] a = r.ints(r.nextInt(10), 0, 4).toArray();
            assertEquals(dp(a), GreedyProblems.minJumps(a), Arrays.toString(a));
        }
    }

    @Test void jumpsLarge() {
        int[] a = new int[1_000_000];
        Arrays.fill(a, 1);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertEquals(999_999, GreedyProblems.minJumps(a)));
    }

    @Test void circuit() {
        assertEquals(3, GreedyProblems.canCompleteCircuit(new int[]{1, 2, 3, 4, 5}, new int[]{3, 4, 5, 1, 2}));
        assertEquals(-1, GreedyProblems.canCompleteCircuit(new int[]{2, 3, 4}, new int[]{3, 4, 3}));
        assertEquals(0, GreedyProblems.canCompleteCircuit(new int[]{5}, new int[]{5}));
        assertEquals(-1, GreedyProblems.canCompleteCircuit(new int[]{4}, new int[]{5}));
        assertEquals(-1, GreedyProblems.canCompleteCircuit(new int[]{}, new int[]{}));
        int m = Integer.MAX_VALUE;
        assertEquals(1, GreedyProblems.canCompleteCircuit(new int[]{0, m, m}, new int[]{m, m, 0}));
    }

    @Test void circuitRandomVsBrute() {
        Random r = new Random(9);
        for (int t = 0; t < 1000; t++) {
            int n = 1 + r.nextInt(6);
            int[] g = r.ints(n, 0, 6).toArray(), c = r.ints(n, 0, 6).toArray();
            int exp = -1;
            for (int s = 0; s < n && exp < 0; s++) {
                int tank = 0;
                boolean ok = true;
                for (int k = 0; k < n && ok; k++) {
                    int i = (s + k) % n;
                    tank += g[i] - c[i];
                    ok = tank >= 0;
                }
                if (ok) exp = s;
            }
            assertEquals(exp, GreedyProblems.canCompleteCircuit(g, c), Arrays.toString(g) + Arrays.toString(c));
        }
    }

    @Test void partitions() {
        assertEquals(List.of(9, 7, 8), GreedyProblems.partitionLabels("ababcbacadefegdehijhklij"));
        assertEquals(List.of(1, 1, 1), GreedyProblems.partitionLabels("abc"));
        assertEquals(List.of(4), GreedyProblems.partitionLabels("aaaa"));
        assertEquals(List.of(), GreedyProblems.partitionLabels(""));
        assertEquals(List.of(6), GreedyProblems.partitionLabels("abcabc"));
        assertEquals(List.of(1, 2, 1), GreedyProblems.partitionLabels("abbc"));
    }

    private static int dp(int[] a) {
        int n = a.length;
        if (n <= 1) return 0;
        int[] d = new int[n];
        Arrays.fill(d, Integer.MAX_VALUE);
        d[0] = 0;
        for (int i = 0; i < n; i++) {
            if (d[i] == Integer.MAX_VALUE) continue;
            for (int j = i + 1; j <= Math.min(n - 1, i + a[i]); j++) d[j] = Math.min(d[j], d[i] + 1);
        }
        return d[n - 1] == Integer.MAX_VALUE ? -1 : d[n - 1];
    }
}
