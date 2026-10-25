package practice.d20261025.a2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.*;
import org.junit.jupiter.api.Test;

class MinStackTest {
    @Test void example() {
        MinStack s = new MinStack();
        s.push(-2);
        s.push(0);
        s.push(-3);
        assertEquals(-3, s.min());
        assertEquals(-3, s.pop());
        assertEquals(0, s.top());
        assertEquals(-2, s.min());
        assertEquals(2, s.size());
    }

    @Test void emptyThrows() {
        MinStack s = new MinStack();
        assertThrows(NoSuchElementException.class, s::pop);
        assertThrows(NoSuchElementException.class, s::top);
        assertThrows(NoSuchElementException.class, s::min);
        s.push(1);
        s.pop();
        assertThrows(NoSuchElementException.class, s::min);
        assertEquals(0, s.size());
    }

    @Test void duplicatesOfTheMinimumAndExtremes() {
        MinStack s = new MinStack();
        s.push(5);
        s.push(1);
        s.push(1);
        s.push(Integer.MIN_VALUE);
        s.push(Integer.MAX_VALUE);
        assertEquals(Integer.MIN_VALUE, s.min());
        s.pop();
        s.pop();
        assertEquals(1, s.min());
        s.pop();
        assertEquals(1, s.min(), "one copy of the minimum is still there");
        s.pop();
        assertEquals(5, s.min());
    }

    @Test void matchesBruteForce() {
        Random rnd = new Random(9);
        MinStack s = new MinStack();
        Deque<Integer> ref = new ArrayDeque<>();
        for (int i = 0; i < 20000; i++) {
            if (ref.isEmpty() || rnd.nextInt(3) != 0) {
                int x = rnd.nextInt(21) - 10;
                s.push(x);
                ref.push(x);
            } else {
                assertEquals(ref.pop(), s.pop());
            }
            if (!ref.isEmpty()) {
                assertEquals(ref.peek(), s.top());
                assertEquals(Collections.min(ref), s.min());
            }
            assertEquals(ref.size(), s.size());
        }
    }

    @Test void largeInput() {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            MinStack s = new MinStack();
            for (int i = 0; i < 2_000_000; i++) s.push(2_000_000 - i);
            assertEquals(1, s.min());
            for (int i = 0; i < 1_999_999; i++) s.pop();
            assertEquals(2_000_000, s.min());
        });
    }
}
