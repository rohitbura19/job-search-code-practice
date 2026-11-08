package practice.d20261108.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;
import practice.d20261108.a2.StackBasics.MinStack;
import practice.d20261108.a2.StackBasics.QueueViaStacks;

class StackBasicsTest {
    @Test void rpnExamples() {
        assertEquals(9, StackBasics.evalRpn(new String[]{"2", "1", "+", "3", "*"}));
        assertEquals(6, StackBasics.evalRpn(new String[]{"4", "13", "5", "/", "+"}));
        assertEquals(22, StackBasics.evalRpn("10 6 9 3 + -11 * / * 17 + 5 +".split(" ")));
    }

    @Test void rpnEdges() {
        assertEquals(-5, StackBasics.evalRpn(new String[]{"-5"}));
        assertEquals(-3, StackBasics.evalRpn(new String[]{"-7", "2", "/"})); // truncates toward zero
        assertEquals(-2, StackBasics.evalRpn(new String[]{"3", "5", "-"}));
        assertEquals(Integer.MIN_VALUE, StackBasics.evalRpn(new String[]{"2147483647", "1", "+"})); // int overflow wraps
        assertEquals(0, StackBasics.evalRpn(new String[]{"0", "5", "*"}));
    }

    @Test void minStack() {
        MinStack s = new MinStack();
        s.push(5); s.push(3); s.push(3); s.push(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, s.getMin());
        assertEquals(Integer.MIN_VALUE, s.pop());
        assertEquals(3, s.getMin());
        s.pop();
        assertEquals(3, s.getMin());
        assertEquals(3, s.top());
        s.pop();
        assertEquals(5, s.getMin());
        assertEquals(1, s.size());
        s.pop();
        assertThrows(NoSuchElementException.class, s::pop);
        assertThrows(NoSuchElementException.class, s::top);
        assertThrows(NoSuchElementException.class, s::getMin);
    }

    @Test void queueViaStacks() {
        QueueViaStacks q = new QueueViaStacks();
        assertTrue(q.isEmpty());
        assertThrows(NoSuchElementException.class, q::pop);
        assertThrows(NoSuchElementException.class, q::peek);
        q.push(1); q.push(2);
        assertEquals(1, q.peek());
        q.push(3);
        assertEquals(1, q.pop());
        q.push(4);
        assertEquals(2, q.pop());
        assertEquals(3, q.pop());
        assertEquals(4, q.pop());
        assertTrue(q.isEmpty());
    }

    @Test void randomVsJdk() {
        Random r = new Random(5);
        MinStack ms = new MinStack();
        QueueViaStacks q = new QueueViaStacks();
        Deque<Integer> ref = new ArrayDeque<>(), refQ = new ArrayDeque<>();
        for (int i = 0; i < 20_000; i++) {
            int op = r.nextInt(3), x = r.nextInt(21) - 10;
            if (op < 2 || ref.isEmpty()) {
                ms.push(x); ref.push(x); q.push(x); refQ.addLast(x);
            } else {
                assertEquals(ref.pop(), ms.pop());
                assertEquals(refQ.pollFirst(), q.pop());
            }
            if (!ref.isEmpty()) {
                assertEquals(Collections.min(ref), ms.getMin());
                assertEquals(refQ.peekFirst(), q.peek());
            }
        }
    }

    @Test void largeAmortised() {
        QueueViaStacks q = new QueueViaStacks();
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            for (int i = 0; i < 1_000_000; i++) { q.push(i); if (i % 3 == 0) q.pop(); }
        });
    }
}
