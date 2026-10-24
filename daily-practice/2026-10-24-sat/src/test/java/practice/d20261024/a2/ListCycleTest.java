package practice.d20261024.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class ListCycleTest {
    /** Builds 0 -> 1 -> ... -> n-1; if tail >= 0 the last node points back to node `tail`. Returns the nodes. */
    static ListNode[] build(int n, int tail) {
        ListNode[] nodes = new ListNode[n];
        for (int i = n - 1; i >= 0; i--) nodes[i] = new ListNode(i, i + 1 < n ? nodes[i + 1] : null);
        if (n > 0 && tail >= 0) nodes[n - 1].next = nodes[tail];
        return nodes;
    }

    @Test void emptyAndSingle() {
        assertFalse(ListCycle.hasCycle(null));
        assertNull(ListCycle.cycleStart(null));
        ListNode one = new ListNode(1);
        assertFalse(ListCycle.hasCycle(one));
        assertNull(ListCycle.cycleStart(one));
        one.next = one;
        assertTrue(ListCycle.hasCycle(one));
        assertSame(one, ListCycle.cycleStart(one));
    }

    @Test void noCycle() {
        ListNode[] n = build(10, -1);
        assertFalse(ListCycle.hasCycle(n[0]));
        assertNull(ListCycle.cycleStart(n[0]));
    }

    @Test void everyEntryPointOfEverySize() {
        for (int len = 2; len <= 12; len++) {
            for (int tail = 0; tail < len; tail++) {
                ListNode[] n = build(len, tail);
                assertTrue(ListCycle.hasCycle(n[0]), len + "/" + tail);
                assertSame(n[tail], ListCycle.cycleStart(n[0]), len + "/" + tail);
            }
        }
    }

    @Test void duplicateValuesDoNotConfuseIdentity() {
        ListNode[] n = build(6, 2);
        for (ListNode x : n) x.val = 5;
        assertSame(n[2], ListCycle.cycleStart(n[0]));
    }

    @Test void largeList() {
        ListNode[] n = build(2_000_000, 1_234_567);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertSame(n[1_234_567], ListCycle.cycleStart(n[0])));
        ListNode[] plain = build(2_000_000, -1);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> assertFalse(ListCycle.hasCycle(plain[0])));
    }
}
