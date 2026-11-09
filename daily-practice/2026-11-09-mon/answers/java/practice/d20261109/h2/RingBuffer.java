package practice.d20261109.h2;

import java.util.*;

/**
 * Array + head index + size. Slot of logical index i is (head + i) % capacity, so add/remove are O(1) with no shifting.
 * modCount (like ArrayList) makes iterators fail-fast: the iterator remembers the count at creation and compares on each next().
 * equals/hashCode are defined over the logical sequence (same contract as List): two buffers with different capacity or head
 * offset but equal contents are equal, and equal objects have equal hash codes.
 */
public final class RingBuffer<E> implements Iterable<E> {
    private final Object[] data;
    private int head, size, modCount;

    public RingBuffer(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("capacity must be >= 1");
        data = new Object[capacity];
    }

    public Optional<E> add(E e) {
        Objects.requireNonNull(e);
        modCount++;
        if (size == data.length) {
            @SuppressWarnings("unchecked") E old = (E) data[head];
            data[head] = e;
            head = (head + 1) % data.length;
            return Optional.of(old);
        }
        data[(head + size++) % data.length] = e;
        return Optional.empty();
    }

    public E removeOldest() {
        if (size == 0) throw new NoSuchElementException();
        modCount++;
        @SuppressWarnings("unchecked") E old = (E) data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        size--;
        return old;
    }

    @SuppressWarnings("unchecked")
    public E get(int i) {
        Objects.checkIndex(i, size);
        return (E) data[(head + i) % data.length];
    }

    public int size() { return size; }
    public int capacity() { return data.length; }
    public boolean isEmpty() { return size == 0; }

    public void clear() {
        modCount++;
        Arrays.fill(data, null);
        head = 0;
        size = 0;
    }

    @Override public Iterator<E> iterator() {
        return new Iterator<>() {
            final int expected = modCount;
            int next = 0;
            @Override public boolean hasNext() { return next < size; }
            @Override public E next() {
                if (expected != modCount) throw new ConcurrentModificationException();
                if (next >= size) throw new NoSuchElementException();
                return get(next++);
            }
        };
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RingBuffer<?> other) || other.size != size) return false;
        for (int i = 0; i < size; i++) if (!get(i).equals(other.get(i))) return false;
        return true;
    }

    @Override public int hashCode() {
        int h = 1;
        for (int i = 0; i < size; i++) h = 31 * h + get(i).hashCode();
        return h;
    }

    @Override public String toString() {
        StringJoiner j = new StringJoiner(", ", "[", "]");
        for (int i = 0; i < size; i++) j.add(String.valueOf(get(i)));
        return j.toString();
    }
}
