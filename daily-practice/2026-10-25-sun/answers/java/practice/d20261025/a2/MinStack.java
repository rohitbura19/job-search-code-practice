package practice.d20261025.a2;

import java.util.NoSuchElementException;

/**
 * Two parallel int arrays: values and the running minimum at each depth. Every op O(1) (amortised for the array growth), O(n) space.
 * Alternative with one array: store x - min (as long) and decode; saves memory but is harder to get right around overflow.
 */
public class MinStack {
    private int[] vals = new int[16], mins = new int[16];
    private int n;

    public void push(int x) {
        if (n == vals.length) {
            vals = java.util.Arrays.copyOf(vals, n * 2);
            mins = java.util.Arrays.copyOf(mins, n * 2);
        }
        vals[n] = x;
        mins[n] = n == 0 ? x : Math.min(x, mins[n - 1]);
        n++;
    }

    public int pop() {
        int v = top();
        n--;
        return v;
    }

    public int top() {
        if (n == 0) throw new NoSuchElementException("empty");
        return vals[n - 1];
    }

    public int min() {
        if (n == 0) throw new NoSuchElementException("empty");
        return mins[n - 1];
    }

    public int size() { return n; }
}
