package stack;

// Implement Queue using Stacks  [Easy | In-stack + out-stack, amortised O(1)]
//
// push 1, push 2, peek -> 1, pop -> 1, empty -> false

public class QueueUsingStacks {

    public QueueUsingStacks() {
        // TODO
    }

    void push(int x) {
        // TODO
    }

    int pop() {
        // TODO
        return 0;
    }

    int peek() {
        // TODO
        return 0;
    }

    boolean empty() {
        // TODO
        return true;
    }

    public static void main(String[] args) {
        QueueUsingStacks q = new QueueUsingStacks();
        q.push(1);
        q.push(2);
        System.out.println(q.peek());  // 1
        System.out.println(q.pop());   // 1
        System.out.println(q.empty()); // false
    }
}
