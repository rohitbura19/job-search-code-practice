package practice.d20261030.a2;

/** Given. */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int val) { this.val = val; }

    public static ListNode of(int... vals) {
        ListNode dummy = new ListNode(0), t = dummy;
        for (int v : vals) { t.next = new ListNode(v); t = t.next; }
        return dummy.next;
    }

    public static int[] toArray(ListNode head) {
        int n = 0;
        for (ListNode p = head; p != null; p = p.next) n++;
        int[] a = new int[n];
        int i = 0;
        for (ListNode p = head; p != null; p = p.next) a[i++] = p.val;
        return a;
    }
}
