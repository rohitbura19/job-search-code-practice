package practice.d20261028.h2;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Decorator with a one-element look-ahead buffer. All operations O(1); the source is read at most one element ahead, and only on demand. */
public class PeekingIterator<T> implements Iterator<T> {
    private final Iterator<T> source;
    private T buffered;
    private boolean has;

    public PeekingIterator(Iterator<T> source) { this.source = source; }

    public T peek() {
        if (!has) {
            if (!source.hasNext()) throw new NoSuchElementException();
            buffered = source.next();
            has = true;
        }
        return buffered;
    }

    @Override public boolean hasNext() { return has || source.hasNext(); }

    @Override public T next() {
        if (has) {
            T t = buffered;
            buffered = null; // do not retain the element
            has = false;
            return t;
        }
        return source.next();
    }
}
