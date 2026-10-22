package practice.d20261012.a1;

import java.util.ArrayList;
import java.util.List;

/** GIVEN - singly linked list node plus helpers for building and printing lists. */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int val) { this.val = val; }

    /** Builds 1->2->3 from of(1, 2, 3); of() returns null. */
    public static ListNode of(int... vals) {
        ListNode dummy = new ListNode(0), tail = dummy;
        for (int v : vals) { tail.next = new ListNode(v); tail = tail.next; }
        return dummy.next;
    }

    /** Values as a List; null gives an empty list; throws IllegalStateException if the list has a cycle. */
    public static List<Integer> toList(ListNode head) {
        List<Integer> out = new ArrayList<>();
        for (ListNode c = head; c != null; c = c.next) {
            out.add(c.val);
            if (out.size() > 5_000_000) throw new IllegalStateException("cycle?");
        }
        return out;
    }
}
