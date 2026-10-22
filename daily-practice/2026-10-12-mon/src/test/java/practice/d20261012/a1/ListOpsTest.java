package practice.d20261012.a1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ListOpsTest {

    static List<Integer> l(ListNode n) { return ListNode.toList(n); }
    static int[] range(int n) { int[] a = new int[n]; for (int i = 0; i < n; i++) a[i] = i + 1; return a; }

    @Test @DisplayName("1->2->3->4->5 becomes 5->4->3->2->1 (iterative and recursive)")
    void example() {
        assertEquals(List.of(5, 4, 3, 2, 1), l(ListOps.reverseList(ListNode.of(1, 2, 3, 4, 5))));
        assertEquals(List.of(5, 4, 3, 2, 1), l(ListOps.reverseRec(ListNode.of(1, 2, 3, 4, 5))));
    }

    @Test @DisplayName("empty, single and two nodes")
    void edges() {
        assertNull(ListOps.reverseList(null));
        assertNull(ListOps.reverseRec(null));
        assertEquals(List.of(7), l(ListOps.reverseList(ListNode.of(7))));
        assertEquals(List.of(7), l(ListOps.reverseRec(ListNode.of(7))));
        assertEquals(List.of(2, 1), l(ListOps.reverseList(ListNode.of(1, 2))));
        assertEquals(List.of(2, 1), l(ListOps.reverseRec(ListNode.of(1, 2))));
    }

    @Test @DisplayName("the recursive version leaves no cycle (the old head's next must be null)")
    void noCycleAfterRecursive() {
        ListNode head = ListNode.of(1, 2, 3);
        ListNode newHead = ListOps.reverseRec(head);
        assertNull(head.next);
        assertEquals(List.of(3, 2, 1), l(newHead));
    }

    @Test @DisplayName("1,000,000 nodes reverse fast (iterative, no stack growth)")
    void bigIterative() {
        ListNode head = ListNode.of(range(1_000_000));
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            ListNode r = ListOps.reverseList(head);
            assertEquals(1_000_000, r.val);
        });
    }

    @Test @DisplayName("reverseBetween examples")
    void betweenExamples() {
        assertEquals(List.of(1, 4, 3, 2, 5), l(ListOps.reverseBetween(ListNode.of(1, 2, 3, 4, 5), 2, 4)));
        assertEquals(List.of(5, 4, 3, 2, 1), l(ListOps.reverseBetween(ListNode.of(1, 2, 3, 4, 5), 1, 5)));
        assertEquals(List.of(1, 2, 3), l(ListOps.reverseBetween(ListNode.of(1, 2, 3), 2, 2)));
        assertEquals(List.of(2, 1), l(ListOps.reverseBetween(ListNode.of(1, 2), 1, 2)));
        assertEquals(List.of(9), l(ListOps.reverseBetween(ListNode.of(9), 1, 1)));
    }

    @Test @DisplayName("reverseKGroup examples")
    void groupExamples() {
        assertEquals(List.of(2, 1, 4, 3, 5), l(ListOps.reverseKGroup(ListNode.of(1, 2, 3, 4, 5), 2)));
        assertEquals(List.of(3, 2, 1, 4, 5), l(ListOps.reverseKGroup(ListNode.of(1, 2, 3, 4, 5), 3)));
        assertEquals(List.of(5, 4, 3, 2, 1), l(ListOps.reverseKGroup(ListNode.of(1, 2, 3, 4, 5), 5)));
        assertEquals(List.of(1, 2, 3), l(ListOps.reverseKGroup(ListNode.of(1, 2, 3), 4)));
        assertEquals(List.of(1, 2, 3), l(ListOps.reverseKGroup(ListNode.of(1, 2, 3), 1)));
        assertNull(ListOps.reverseKGroup(null, 3));
    }

    @Test @DisplayName("random lists match a list-based oracle for reverseBetween and reverseKGroup")
    void randomOracle() {
        Random rnd = new Random(12);
        for (int t = 0; t < 1500; t++) {
            int n = rnd.nextInt(12) + 1;
            List<Integer> vals = new ArrayList<>();
            for (int i = 0; i < n; i++) vals.add(rnd.nextInt(100));
            int m = rnd.nextInt(n) + 1, e = m + rnd.nextInt(n - m + 1);
            List<Integer> exp = new ArrayList<>(vals); Collections.reverse(exp.subList(m - 1, e));
            assertEquals(exp, l(ListOps.reverseBetween(ListNode.of(vals.stream().mapToInt(x -> x).toArray()), m, e)), vals + " m=" + m + " n=" + e);
            int k = rnd.nextInt(5) + 1;
            List<Integer> expK = new ArrayList<>(vals);
            for (int i = 0; i + k <= expK.size(); i += k) Collections.reverse(expK.subList(i, i + k));
            assertEquals(expK, l(ListOps.reverseKGroup(ListNode.of(vals.stream().mapToInt(x -> x).toArray()), k)), vals + " k=" + k);
        }
    }

    @Test @DisplayName("hasCycle")
    void cycle() {
        assertFalse(ListOps.hasCycle(null));
        assertFalse(ListOps.hasCycle(ListNode.of(1)));
        assertFalse(ListOps.hasCycle(ListNode.of(1, 2, 3, 4)));
        ListNode one = ListNode.of(1); one.next = one;
        assertTrue(ListOps.hasCycle(one));
        ListNode head = ListNode.of(1, 2, 3, 4, 5);
        ListNode tail = head; while (tail.next != null) tail = tail.next;
        tail.next = head.next;                       // 5 -> 2
        assertTrue(ListOps.hasCycle(head));
        ListNode big = ListNode.of(range(1_000_000));
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertFalse(ListOps.hasCycle(big)));
    }
}
