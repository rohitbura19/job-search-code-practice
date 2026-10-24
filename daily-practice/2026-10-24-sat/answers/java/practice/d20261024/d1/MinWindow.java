package practice.d20261024.d1;

/**
 * Sliding window with a need[] table and a "missing" counter (number of characters of t still uncovered, counting
 * multiplicity). Grow r; when missing hits 0, shrink l while the window stays valid, recording the best (shortest, leftmost on
 * ties since only a strictly shorter window replaces it). need[c] > 0 means c is still wanted; a char leaves the window with
 * need[c] going back above 0 only if it was needed. ASCII input. O(|s| + |t|) time, O(1) space.
 */
public final class MinWindow {
    private MinWindow() {}

    public static String minWindow(String s, String t) {
        if (t.isEmpty() || s.length() < t.length()) return "";
        int[] need = new int[128];
        for (int i = 0; i < t.length(); i++) need[t.charAt(i)]++;
        int missing = t.length(), bestL = 0, bestLen = Integer.MAX_VALUE, l = 0;
        for (int r = 0; r < s.length(); r++) {
            if (need[s.charAt(r)]-- > 0) missing--;
            while (missing == 0) {
                if (r - l + 1 < bestLen) { bestLen = r - l + 1; bestL = l; }
                if (++need[s.charAt(l++)] > 0) missing++;
            }
        }
        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestL, bestL + bestLen);
    }
}
