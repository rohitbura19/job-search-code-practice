package stack;

// Min Stack  [Medium | Design]
// push, pop, top and getMin, all O(1).
//
// push -2, 0, -3 -> getMin()=-3; pop(); top()=0; getMin()=-2

public class MinStack {

    public MinStack() {
        // TODO
    }

    void push(int x) {
        // TODO
    }

    void pop() {
        // TODO
    }

    int top() {
        // TODO
        return 0;
    }

    int getMin() {
        // TODO
        return 0;
    }

    public static void main(String[] args) {
        MinStack s = new MinStack();
        s.push(-2);
        s.push(0);
        s.push(-3);
        System.out.println(s.getMin()); // -3
        s.pop();
        System.out.println(s.top());    // 0
        System.out.println(s.getMin()); // -2
    }
}
