package linkedlist;

// Linked List Cycle II  [Medium | Floyd slow/fast]
// Return the node where the cycle starts, or null. O(1) extra space.
//
// 1->2->3->4->(back to 2) -> node 2

public class LinkedListCycleII {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static ListNode detectCycle(ListNode head) {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        ListNode a = new ListNode(1), b = new ListNode(2), c = new ListNode(3), d = new ListNode(4);
        a.next = b; b.next = c; c.next = d; d.next = b;
        ListNode start = detectCycle(a);
        System.out.println(start == null ? null : start.val); // 2
    }
}
