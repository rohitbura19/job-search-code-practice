package practice.d20261108.a2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

/**
 * RPN: operands on a stack; the right operand is popped first. MinStack: a parallel stack of running minimums
 * (no overflow tricks needed). Queue: "in" receives pushes; "out" is refilled only when empty, so each element moves once.
 */
public final class StackBasics {
    private StackBasics() {}

    public static int evalRpn(String[] tokens) {
        Deque<Integer> st = new ArrayDeque<>();
        for (String t : tokens) {
            switch (t) {
                case "+" -> st.push(st.pop() + st.pop());
                case "-" -> { int b = st.pop(), a = st.pop(); st.push(a - b); }
                case "*" -> st.push(st.pop() * st.pop());
                case "/" -> { int b = st.pop(), a = st.pop(); st.push(a / b); }
                default -> st.push(Integer.parseInt(t));
            }
        }
        return st.pop();
    }

    public static class MinStack {
        private final Deque<int[]> st = new ArrayDeque<>(); // {value, minSoFar}

        public void push(int x) { st.push(new int[]{x, st.isEmpty() ? x : Math.min(x, st.peek()[1])}); }
        public int pop() { return require().pop()[0]; }
        public int top() { return require().peek()[0]; }
        public int getMin() { return require().peek()[1]; }
        public int size() { return st.size(); }
        private Deque<int[]> require() { if (st.isEmpty()) throw new NoSuchElementException(); return st; }
    }

    public static class QueueViaStacks {
        private final Deque<Integer> in = new ArrayDeque<>(), out = new ArrayDeque<>();

        public void push(int x) { in.push(x); }
        public int pop() { shift(); return out.pop(); }
        public int peek() { shift(); return out.peek(); }
        public boolean isEmpty() { return in.isEmpty() && out.isEmpty(); }
        private void shift() {
            if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop());
            if (out.isEmpty()) throw new NoSuchElementException();
        }
    }
}
