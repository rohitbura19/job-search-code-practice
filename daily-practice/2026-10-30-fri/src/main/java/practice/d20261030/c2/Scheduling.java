package practice.d20261030.c2;

public final class Scheduling {
    private Scheduling() {}

    /**
     * CPU tasks 'A'..'Z', one per unit of time; two runs of the same task need at least n idle/other units between them.
     * Return the minimum total time (idle units count). Order of tasks is free.
     * [A,A,A,B,B,B], n=2 -> 8; same with n=0 -> 6; [A,A,A,A,B], n=1 -> 7. Empty -> 0. Must handle 10^6 tasks.
     */
    public static int leastInterval(char[] tasks, int n) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Rearrange the lowercase letters of s so that no two adjacent characters are equal; return any such string,
     * or "" if impossible. "aab" -> "aba"; "aaab" -> ""; "" -> "". Must handle 10^5 characters.
     */
    public static String reorganize(String s) { throw new UnsupportedOperationException("TODO"); }
}
