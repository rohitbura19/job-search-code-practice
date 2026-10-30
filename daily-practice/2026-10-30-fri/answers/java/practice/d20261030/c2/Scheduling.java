package practice.d20261030.c2;

import java.util.PriorityQueue;

/**
 * leastInterval: the most frequent task forces (max-1) frames of width n+1 plus a last row with every task tied at max:
 *   max((max-1)*(n+1) + tiedAtMax, tasks.length). Counting only, O(N + 26) time, O(1) space.
 * reorganize: greedy, always place the most frequent letter that differs from the previous one (max-heap on count);
 *   impossible iff maxCount > (len+1)/2. O(N log 26).
 */
public final class Scheduling {
    private Scheduling() {}

    public static int leastInterval(char[] tasks, int n) {
        int[] cnt = new int[26];
        for (char c : tasks) cnt[c - 'A']++;
        int max = 0, tied = 0;
        for (int c : cnt) {
            if (c > max) { max = c; tied = 1; } else if (c == max && c > 0) tied++;
        }
        if (max == 0) return 0;
        return Math.max(tasks.length, (max - 1) * (n + 1) + tied);
    }

    public static String reorganize(String s) {
        int[] cnt = new int[26];
        for (int i = 0; i < s.length(); i++) cnt[s.charAt(i) - 'a']++;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(b[1], a[1]));
        for (int i = 0; i < 26; i++) if (cnt[i] > 0) pq.add(new int[] {i, cnt[i]});
        StringBuilder sb = new StringBuilder(s.length());
        int[] held = null;
        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            sb.append((char) ('a' + top[0]));
            top[1]--;
            if (held != null) pq.add(held);
            held = top[1] > 0 ? top : null;
        }
        return held != null ? "" : sb.toString();
    }
}
