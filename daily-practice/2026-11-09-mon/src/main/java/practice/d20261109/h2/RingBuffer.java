package practice.d20261109.h2;

import java.util.Iterator;
import java.util.Optional;

/**
 * Fixed-capacity FIFO that overwrites the oldest element when full. Backed by an array and a head index (no shifting).
 * Not thread-safe. Contract (RingBufferTest checks all of it):
 *  - capacity < 1 -> IllegalArgumentException; null elements -> NullPointerException.
 *  - add(e): appends as newest; if full, evicts the oldest and returns it, else Optional.empty().
 *  - removeOldest(): removes and returns the oldest; NoSuchElementException when empty.
 *  - get(i): i = 0 is the oldest; out of range -> IndexOutOfBoundsException.
 *  - iterator(): oldest to newest. FAIL-FAST: after the iterator was created, any add / removeOldest / clear makes the next
 *    next() throw ConcurrentModificationException. next() past the end -> NoSuchElementException. remove() -> UnsupportedOperationException.
 *    Several iterators may be active at once, independently.
 *  - equals/hashCode/toString use the elements in order only (capacity and internal head position are irrelevant): toString "[a, b, c]".
 *  - clear() also drops references so the GC can reclaim the elements.
 */
public final class RingBuffer<E> implements Iterable<E> {
    public RingBuffer(int capacity) { throw new UnsupportedOperationException("TODO"); }

    public Optional<E> add(E e) { throw new UnsupportedOperationException("TODO"); }
    public E removeOldest() { throw new UnsupportedOperationException("TODO"); }
    public E get(int i) { throw new UnsupportedOperationException("TODO"); }
    public int size() { throw new UnsupportedOperationException("TODO"); }
    public int capacity() { throw new UnsupportedOperationException("TODO"); }
    public boolean isEmpty() { throw new UnsupportedOperationException("TODO"); }
    public void clear() { throw new UnsupportedOperationException("TODO"); }

    @Override public Iterator<E> iterator() { throw new UnsupportedOperationException("TODO"); }
    @Override public boolean equals(Object o) { throw new UnsupportedOperationException("TODO"); }
    @Override public int hashCode() { throw new UnsupportedOperationException("TODO"); }
    @Override public String toString() { throw new UnsupportedOperationException("TODO"); }
}
