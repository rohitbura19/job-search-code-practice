# H2 - OOP: `RingBuffer<E>` implements `Iterable<E>` (20 min) - `h2/RingBuffer.java`

A fixed-capacity FIFO that overwrites the oldest element when full, backed by an array and a head index. The class Javadoc is the contract: `add` returns the evicted element, `removeOldest`, `get(i)`, a FAIL-FAST iterator (oldest to newest, `ConcurrentModificationException` after any structural change, `remove()` unsupported), `equals/hashCode/toString` based on the logical sequence only, `clear()` drops references. Test: `RingBufferTest` (includes a random comparison with `ArrayDeque`).
