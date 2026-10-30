package practice.d20261030.c2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class SchedulingTest {
    @Test void leastIntervalExamples() {
        assertEquals(8, Scheduling.leastInterval("AAABBB".toCharArray(), 2));
        assertEquals(6, Scheduling.leastInterval("AAABBB".toCharArray(), 0));
        assertEquals(7, Scheduling.leastInterval("AAAAB".toCharArray(), 1));
        assertEquals(0, Scheduling.leastInterval(new char[0], 5));
        assertEquals(1, Scheduling.leastInterval("A".toCharArray(), 100));
        assertEquals(16, Scheduling.leastInterval("AAAAAABCDEFG".toCharArray(), 2));
        assertEquals(12, Scheduling.leastInterval("ABCDEFGHIJKL".toCharArray(), 50));
    }

    /** BFS over (remaining counts, cooldown left per type): the shortest schedule, idle allowed. */
    static int bruteLeastInterval(int[] counts, int n) {
        int k = counts.length;
        ArrayDeque<int[]> q = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        int[] start = new int[2 * k];
        System.arraycopy(counts, 0, start, 0, k);
        q.add(start);
        seen.add(Arrays.toString(start));
        int time = 0;
        while (!q.isEmpty()) {
            for (int sz = q.size(); sz > 0; sz--) {
                int[] s = q.poll();
                boolean done = true;
                for (int i = 0; i < k; i++) done &= s[i] == 0;
                if (done) return time;
                for (int pick = -1; pick < k; pick++) {
                    if (pick >= 0 && (s[pick] == 0 || s[k + pick] > 0)) continue;
                    int[] t = s.clone();
                    for (int i = 0; i < k; i++) if (t[k + i] > 0) t[k + i]--;
                    if (pick >= 0) { t[pick]--; t[k + pick] = n; }
                    if (seen.add(Arrays.toString(t))) q.add(t);
                }
            }
            time++;
        }
        throw new IllegalStateException();
    }

    @Test void leastIntervalRandomAgainstBfs() {
        Random r = new Random(11);
        for (int it = 0; it < 300; it++) {
            int types = 1 + r.nextInt(3), n = r.nextInt(4);
            int[] counts = new int[types];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < types; i++) {
                counts[i] = 1 + r.nextInt(4);
                sb.append(String.valueOf((char) ('A' + i)).repeat(counts[i]));
            }
            assertEquals(bruteLeastInterval(counts, n), Scheduling.leastInterval(sb.toString().toCharArray(), n), sb + " n=" + n);
        }
    }

    @Test @Timeout(2) void leastIntervalLarge() {
        char[] t = new char[1_000_000];
        for (int i = 0; i < t.length; i++) t[i] = (char) ('A' + i % 26);
        assertEquals(1_000_000, Scheduling.leastInterval(t, 25));
        assertEquals(1_000_000, Scheduling.leastInterval(t, 3));
        char[] same = new char[1_000_000];
        Arrays.fill(same, 'Z');
        assertEquals(999_999 * 1001 + 1, Scheduling.leastInterval(same, 1000));
    }

    static void assertValid(String in, String out) {
        char[] a = in.toCharArray(), b = out.toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        assertTrue(Arrays.equals(a, b), () -> "not a permutation: " + out);
        for (int i = 1; i < out.length(); i++) {
            if (out.charAt(i) == out.charAt(i - 1)) throw new AssertionError("adjacent equal at " + i + " in " + out);
        }
    }

    @Test void reorganizeExamples() {
        assertValid("aab", Scheduling.reorganize("aab"));
        assertEquals("", Scheduling.reorganize("aaab"));
        assertEquals("", Scheduling.reorganize(""));
        assertEquals("a", Scheduling.reorganize("a"));
        assertEquals("", Scheduling.reorganize("aa"));
        assertValid("aabb", Scheduling.reorganize("aabb"));
        assertValid("vvvlo", Scheduling.reorganize("vvvlo"));
    }

    @Test void reorganizeRandomFeasibility() {
        Random r = new Random(12);
        for (int it = 0; it < 2000; it++) {
            int len = r.nextInt(12), letters = 1 + r.nextInt(4);
            StringBuilder sb = new StringBuilder();
            int[] c = new int[26];
            for (int i = 0; i < len; i++) { char ch = (char) ('a' + r.nextInt(letters)); sb.append(ch); c[ch - 'a']++; }
            int max = Arrays.stream(c).max().getAsInt();
            String out = Scheduling.reorganize(sb.toString());
            if (max > (len + 1) / 2) assertEquals("", out, sb.toString());
            else assertValid(sb.toString(), out);
        }
    }

    @Test @Timeout(2) void reorganizeLarge() {
        String s = "a".repeat(50_000) + "b".repeat(30_000) + "c".repeat(20_000);
        assertValid(s, Scheduling.reorganize(s));
    }
}
