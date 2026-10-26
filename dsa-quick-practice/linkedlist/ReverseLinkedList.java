package linkedlist;

// Reverse Linked List  [Easy | Pointers]
// Reverse a singly linked list, return the new head. Try iterative and recursive.
//
// 1->2->3->4->5 -> 5->4->3->2->1

public class ReverseLinkedList {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static ListNode reverseList(ListNode head) {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        System.out.println(str(reverseList(of(1, 2, 3, 4, 5)))); // 5 4 3 2 1
    }

    static ListNode of(int... vals) {
        ListNode dummy = new ListNode(0), t = dummy;
        for (int v : vals) t = t.next = new ListNode(v);
        return dummy.next;
    }

    static String str(ListNode h) {
        StringBuilder sb = new StringBuilder();
        for (; h != null; h = h.next) sb.append(h.val).append(' ');
        return sb.toString().trim();
    }
}
