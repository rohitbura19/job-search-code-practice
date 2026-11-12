package linkedlist;

// Merge Two Sorted Lists  [Easy | Dummy head, splice nodes]
//
// [1,2,4] + [1,3,4] -> [1,1,2,3,4,4]
// [] + [0]          -> [0]

public class MergeTwoSortedLists {

    static class ListNode {
        int val;
        ListNode next;
        ListNode(int val) { this.val = val; }
    }

    static ListNode mergeTwoLists(ListNode a, ListNode b) {
        // TODO
        return null;
    }

    public static void main(String[] args) {
        System.out.println(str(mergeTwoLists(of(1, 2, 4), of(1, 3, 4)))); // 1 1 2 3 4 4
        System.out.println(str(mergeTwoLists(of(), of(0))));              // 0
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
