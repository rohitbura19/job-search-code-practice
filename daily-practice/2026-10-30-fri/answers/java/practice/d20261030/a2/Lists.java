package practice.d20261030.a2;

/**
 * reverse: three pointers (prev, cur, next). O(n) time, O(1) space.
 * reverseBetween: walk to the node before `left` (dummy head handles left == 1), then repeatedly move the node after `tail` to just after
 *   `before` (head insertion), right-left times. One pass.
 * isPalindrome: slow/fast to the middle, reverse the second half, compare, (restore it). O(n) / O(1).
 */
public final class Lists {
    private Lists() {}

    public static ListNode reverse(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }

    public static ListNode reverseBetween(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode before = dummy;
        for (int i = 1; i < left; i++) before = before.next;
        ListNode tail = before.next;
        for (int i = left; i < right; i++) {
            ListNode move = tail.next;
            tail.next = move.next;
            move.next = before.next;
            before.next = move;
        }
        return dummy.next;
    }

    public static boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) return true;
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) { slow = slow.next; fast = fast.next.next; }
        ListNode second = reverse(slow.next);
        boolean ok = true;
        for (ListNode a = head, b = second; b != null; a = a.next, b = b.next) {
            if (a.val != b.val) { ok = false; break; }
        }
        slow.next = reverse(second);
        return ok;
    }
}
