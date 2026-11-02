package practice.d20261102.a2;

/**
 * Monotonic stack: each index is pushed and popped at most once, so O(n).
 * dailyTemperatures: stack of indices with non-increasing temps; a warmer day pops and resolves them.
 * nextGreaterCircular: same, iterating 2n times (index i % n) so the wrap-around is covered.
 * removeDuplicates: stack of runs (char, count); equal char extends the top run, a run reaching k is popped,
 * and the next char may then merge with the new top run.
 */
public final class MonotonicStack {
    private MonotonicStack() {}

    public static int[] dailyTemperatures(int[] temps) {
        int n = temps.length;
        int[] out = new int[n], st = new int[n];
        int top = 0;
        for (int i = 0; i < n; i++) {
            while (top > 0 && temps[st[top - 1]] < temps[i]) {
                int j = st[--top];
                out[j] = i - j;
            }
            st[top++] = i;
        }
        return out;
    }

    public static int[] nextGreaterCircular(int[] a) {
        int n = a.length;
        int[] out = new int[n], st = new int[n];
        java.util.Arrays.fill(out, -1);
        int top = 0;
        for (int i = 0; i < 2 * n; i++) {
            int v = a[i % n];
            while (top > 0 && a[st[top - 1]] < v) out[st[--top]] = v;
            if (i < n) st[top++] = i;
        }
        return out;
    }

    public static String removeDuplicates(String s, int k) {
        int n = s.length();
        char[] ch = new char[n];
        int[] cnt = new int[n];
        int top = 0;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (top > 0 && ch[top - 1] == c) cnt[top - 1]++;
            else { ch[top] = c; cnt[top++] = 1; }
            if (cnt[top - 1] == k) top--;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < top; i++) for (int j = 0; j < cnt[i]; j++) sb.append(ch[i]);
        return sb.toString();
    }
}
