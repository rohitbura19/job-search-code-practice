package linkedlist;

// Linked List Cycle  [Easy | Floyd slow/fast]
// True if the list has a cycle. O(1) extra space.
//
// 1->2->3->4->(back to 2) -> true
// 1->2->3                 -> false

public class LinkedListCycle {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static boolean hasCycle(ListNode head) {
        // TODO
        return false;
    }

    public static void main(String[] args) {
        ListNode a = new ListNode(1), b = new ListNode(2), c = new ListNode(3), d = new ListNode(4);
        a.next = b; b.next = c; c.next = d; d.next = b;
        System.out.println(hasCycle(a)); // true

        ListNode x = new ListNode(1);
        x.next = new ListNode(2);
        x.next.next = new ListNode(3);
        System.out.println(hasCycle(x)); // false
    }
}
