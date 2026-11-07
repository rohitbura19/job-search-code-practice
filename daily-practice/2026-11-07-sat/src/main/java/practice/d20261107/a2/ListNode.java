package practice.d20261107.a2;

import java.util.ArrayList;
import java.util.List;

/** Singly linked list node (data class, given). */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int val) { this.val = val; }

    public static ListNode of(int... vals) {
        ListNode dummy = new ListNode(0), t = dummy;
        for (int v : vals) { t.next = new ListNode(v); t = t.next; }
        return dummy.next;
    }

    public static List<Integer> toList(ListNode h) {
        List<Integer> out = new ArrayList<>();
        for (; h != null; h = h.next) out.add(h.val);
        return out;
    }
}
