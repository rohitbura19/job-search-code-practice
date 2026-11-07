package practice.d20261107.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class LinkedListsTest {
    private static List<Integer> l(ListNode n) { return ListNode.toList(n); }

    @Test void mergeExamples() {
        assertEquals(List.of(1, 1, 2, 3, 4, 4), l(LinkedLists.mergeTwoLists(ListNode.of(1, 2, 4), ListNode.of(1, 3, 4))));
        assertNull(LinkedLists.mergeTwoLists(null, null));
        assertEquals(List.of(0), l(LinkedLists.mergeTwoLists(null, ListNode.of(0))));
        assertEquals(List.of(5), l(LinkedLists.mergeTwoLists(ListNode.of(5), null)));
        assertEquals(List.of(-3, -1, 7, 7, 9), l(LinkedLists.mergeTwoLists(ListNode.of(-3, 7, 9), ListNode.of(-1, 7))));
    }

    @Test void mergeReusesNodesAndIsStable() {
        ListNode a = ListNode.of(1), b = ListNode.of(1);
        ListNode m = LinkedLists.mergeTwoLists(a, b);
        assertSame(a, m);
        assertSame(b, m.next);
    }

    @Test void mergeRandom() {
        Random r = new Random(3);
        for (int t = 0; t < 1000; t++) {
            int[] x = r.ints(r.nextInt(8), -5, 5).sorted().toArray();
            int[] y = r.ints(r.nextInt(8), -5, 5).sorted().toArray();
            List<Integer> exp = new ArrayList<>();
            for (int v : x) exp.add(v);
            for (int v : y) exp.add(v);
            Collections.sort(exp);
            assertEquals(exp, l(LinkedLists.mergeTwoLists(ListNode.of(x), ListNode.of(y))));
        }
    }

    @Test void removeNth() {
        assertEquals(List.of(1, 2, 3, 5), l(LinkedLists.removeNthFromEnd(ListNode.of(1, 2, 3, 4, 5), 2)));
        assertNull(LinkedLists.removeNthFromEnd(ListNode.of(1), 1));
        assertEquals(List.of(1), l(LinkedLists.removeNthFromEnd(ListNode.of(1, 2), 1)));
        assertEquals(List.of(2), l(LinkedLists.removeNthFromEnd(ListNode.of(1, 2), 2)));
        assertEquals(List.of(2, 2, 3), l(LinkedLists.removeNthFromEnd(ListNode.of(2, 2, 2, 3), 3)));
    }

    @Test void removeNthRandom() {
        Random r = new Random(4);
        for (int t = 0; t < 1000; t++) {
            int[] x = r.ints(1 + r.nextInt(9), 0, 4).toArray();
            int n = 1 + r.nextInt(x.length);
            List<Integer> exp = new ArrayList<>();
            for (int v : x) exp.add(v);
            exp.remove(x.length - n);
            assertEquals(exp, l(LinkedLists.removeNthFromEnd(ListNode.of(x), n)));
        }
    }

    @Test void middle() {
        assertEquals(3, LinkedLists.middleNode(ListNode.of(1, 2, 3, 4, 5)).val);
        assertEquals(4, LinkedLists.middleNode(ListNode.of(1, 2, 3, 4, 5, 6)).val);
        assertEquals(7, LinkedLists.middleNode(ListNode.of(7)).val);
        assertEquals(2, LinkedLists.middleNode(ListNode.of(1, 2)).val);
    }

    @Test void largeIsIterative() {
        int[] big = new int[2_000_000];
        for (int i = 0; i < big.length; i++) big[i] = i;
        ListNode h = ListNode.of(big);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            assertEquals(1_000_000, LinkedLists.middleNode(h).val);
            ListNode m = LinkedLists.mergeTwoLists(ListNode.of(big), ListNode.of(big));
            assertEquals(0, m.val);
            ListNode r = LinkedLists.removeNthFromEnd(h, 1);
            int c = 0;
            for (ListNode p = r; p != null; p = p.next) c++;
            assertEquals(big.length - 1, c);
        });
    }
}
