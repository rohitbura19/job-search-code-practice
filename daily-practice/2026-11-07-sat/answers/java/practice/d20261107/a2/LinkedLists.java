package practice.d20261107.a2;

/** All O(n) time, O(1) space. A dummy head removes the "delete the head" special case. */
public final class LinkedLists {
    private LinkedLists() {}

    public static ListNode mergeTwoLists(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0), t = dummy;
        while (a != null && b != null) {
            if (b.val < a.val) { t.next = b; b = b.next; } else { t.next = a; a = a.next; }
            t = t.next;
        }
        t.next = a != null ? a : b;
        return dummy.next;
    }

    /** Fast runs n+1 nodes ahead; when fast falls off, slow sits just before the victim. */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode fast = dummy, slow = dummy;
        for (int i = 0; i < n; i++) fast = fast.next;
        while (fast.next != null) { fast = fast.next; slow = slow.next; }
        slow.next = slow.next.next;
        return dummy.next;
    }

    public static ListNode middleNode(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) { slow = slow.next; fast = fast.next.next; }
        return slow;
    }
}
