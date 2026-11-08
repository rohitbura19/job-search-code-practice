package practice.d20261108.a2;

import java.util.NoSuchElementException;

public final class StackBasics {
    private StackBasics() {}

    /**
     * Evaluate a Reverse Polish Notation expression with tokens "+", "-", "*", "/" and integers (may be negative).
     * Division truncates toward zero. Input is always valid and fits in int.
     */
    public static int evalRpn(String[] tokens) { throw new UnsupportedOperationException("TODO"); }

    /** Stack with O(1) push, pop, top and getMin. pop/top/getMin on an empty stack throw NoSuchElementException. */
    public static class MinStack {
        public void push(int x) { throw new UnsupportedOperationException("TODO"); }
        public int pop() { throw new UnsupportedOperationException("TODO"); }
        public int top() { throw new UnsupportedOperationException("TODO"); }
        public int getMin() { throw new UnsupportedOperationException("TODO"); }
        public int size() { throw new UnsupportedOperationException("TODO"); }
    }

    /** FIFO queue built from two stacks, amortised O(1). pop/peek on an empty queue throw NoSuchElementException. */
    public static class QueueViaStacks {
        public void push(int x) { throw new UnsupportedOperationException("TODO"); }
        public int pop() { throw new UnsupportedOperationException("TODO"); }
        public int peek() { throw new UnsupportedOperationException("TODO"); }
        public boolean isEmpty() { throw new UnsupportedOperationException("TODO"); }
    }
}
