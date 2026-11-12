package linkedlist;

// Remove Nth Node From End of List  [Medium | Two pointers n apart]
// One pass. 1 = last node.
//
// [1,2,3,4,5], n=2 -> [1,2,3,5]
// [1], n=1         -> []

public class RemoveNthFromEnd {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static ListNode removeNthFromEnd(ListNode head, int n) {
        // TODO
        return head;
    }

    public static void main(String[] args) {
        System.out.println(str(removeNthFromEnd(of(1, 2, 3, 4, 5), 2))); // 1 2 3 5
        System.out.println("[" + str(removeNthFromEnd(of(1), 1)) + "]");  // []
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
