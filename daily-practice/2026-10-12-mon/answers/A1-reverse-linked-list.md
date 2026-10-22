# Answers: A1-reverse-linked-list

(Spoilers. Open only after your attempt.)

**Reference**
```java
public ListNode reverseList(ListNode head) {
    ListNode prev = null, cur = head;
    while (cur != null) {
        ListNode next = cur.next;   // save
        cur.next = prev;            // flip
        prev = cur;                 // advance both
        cur = next;
    }
    return prev;
}
// recursive: O(n) stack
public ListNode reverseRec(ListNode h) {
    if (h == null || h.next == null) return h;
    ListNode newHead = reverseRec(h.next);
    h.next.next = h; h.next = null;
    return newHead;
}
```
O(n) time, O(1) space iterative. Trap: forgetting to save `next` before overwriting, or leaving `h.next` set in the recursive version (creates a cycle). Cycle check: Floyd slow/fast pointers.

Extra (the follow-ups, verified by `ListOpsTest`): `reverseBetween` uses a dummy node, walks `pre` to position m-1, then repeatedly moves the node after `cur` to the front of the segment (`nxt = cur.next; cur.next = nxt.next; nxt.next = pre.next; pre.next = nxt`), n-m times. `reverseKGroup` finds the k-th node ahead of `groupPrev`; if there is none, stop (the short tail stays); otherwise reverse the group with `prev` starting at `groupNext`, then link `groupPrev.next = kth` and move `groupPrev` to the old group head.
