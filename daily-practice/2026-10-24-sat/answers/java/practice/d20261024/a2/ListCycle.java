package practice.d20261024.a2;

/**
 * Floyd. Fast moves 2, slow moves 1; if they meet there is a cycle. Let the head be a steps from the cycle entry, the meeting
 * point b steps past the entry, cycle length c. Fast has gone twice as far: 2(a+b) = a+b+k*c, so a = k*c - b: a pointer from the
 * head and one from the meeting point, both moving 1 step, meet exactly at the entry. O(n) time, O(1) space.
 */
public final class ListCycle {
    private ListCycle() {}

    public static boolean hasCycle(ListNode head) {
        return meeting(head) != null;
    }

    public static ListNode cycleStart(ListNode head) {
        ListNode m = meeting(head);
        if (m == null) return null;
        ListNode p = head;
        while (p != m) {
            p = p.next;
            m = m.next;
        }
        return p;
    }

    private static ListNode meeting(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return slow;
        }
        return null;
    }
}
