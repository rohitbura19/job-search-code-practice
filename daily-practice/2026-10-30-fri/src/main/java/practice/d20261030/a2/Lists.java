package practice.d20261030.a2;

public final class Lists {
    private Lists() {}

    /** Reverse a singly linked list in place, iteratively (a 10^6-node list must not overflow the stack). Returns the new head; null -> null. */
    public static ListNode reverse(ListNode head) { throw new UnsupportedOperationException("TODO"); }

    /**
     * Reverse the nodes from position left to position right (1-based, inclusive) in ONE pass, relink in place, return the head.
     * 1 <= left <= right <= length. [1,2,3,4,5], left 2, right 4 -> [1,4,3,2,5]. left == right changes nothing. left == 1 changes the head.
     */
    public static ListNode reverseBetween(ListNode head, int left, int right) { throw new UnsupportedOperationException("TODO"); }

    /** True if the values read the same forwards and backwards. O(n) time, O(1) extra space (no array, no stack); null and one node are palindromes. */
    public static boolean isPalindrome(ListNode head) { throw new UnsupportedOperationException("TODO"); }
}
