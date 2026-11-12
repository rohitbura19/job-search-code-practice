package linkedlist;

// Reverse Linked List II  [Medium | One pass, relink]
// Reverse positions left..right (1-based) in one pass.
//
// [1,2,3,4,5], left=2, right=4 -> [1,4,3,2,5]

public class ReverseLinkedListII {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static ListNode reverseBetween(ListNode head, int left, int right) {
        // TODO
        return head;
    }

    public static void main(String[] args) {
        System.out.println(str(reverseBetween(of(1, 2, 3, 4, 5), 2, 4))); // 1 4 3 2 5
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
