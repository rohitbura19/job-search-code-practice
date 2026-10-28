package practice.d20261028.h2;

import java.util.Iterator;

/** Decorates an Iterator with peek(). Must work on one-shot iterators (do not buffer the whole source). */
public class PeekingIterator<T> implements Iterator<T> {
    public PeekingIterator(Iterator<T> source) { throw new UnsupportedOperationException("TODO"); }

    /** The element next() would return, without consuming it. NoSuchElementException when exhausted. */
    public T peek() { throw new UnsupportedOperationException("TODO"); }

    @Override public boolean hasNext() { throw new UnsupportedOperationException("TODO"); }
    @Override public T next() { throw new UnsupportedOperationException("TODO"); }
}
