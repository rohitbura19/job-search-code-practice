package linkedlist;

// Middle of the Linked List  [Easy | Slow/fast pointers]
// Even length: return the SECOND middle.
//
// [1,2,3,4,5]   -> 3
// [1,2,3,4,5,6] -> 4

public class MiddleOfList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static ListNode middleNode(ListNode head) {
        // TODO
        return head;
    }

    public static void main(String[] args) {
        System.out.println(middleNode(of(1, 2, 3, 4, 5)).val);    // 3
        System.out.println(middleNode(of(1, 2, 3, 4, 5, 6)).val); // 4
    }

    static ListNode of(int... vals) {
        ListNode dummy = new ListNode(0), t = dummy;
        for (int v : vals) t = t.next = new ListNode(v);
        return dummy.next;
    }
}
