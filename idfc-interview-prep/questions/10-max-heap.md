# Q10. Implement a Max Heap from Scratch

**Duration:** 20 minutes · **Language:** Java · **Source:** IDFC L1 round, per LeetCode ("implement max heap")

## Problem Statement

Implement a max heap of `int` backed by an array that grows as needed. **Don't use** `PriorityQueue` or any `Collections` sort.

```java
class MaxHeap {
    MaxHeap()                          // initial capacity 16, doubles when full
    static MaxHeap heapify(int[] a)    // build in O(n) with bottom-up sift-down; don't modify the caller's array
    void push(int x)                   // O(log n)
    int pop()                          // remove and return the max; empty -> NoSuchElementException
    int peek()                         // empty -> NoSuchElementException
    int size()
    boolean isEmpty()
}
```

## Examples

```
h = new MaxHeap()
push 5, 3, 8, 1, 9
peek() -> 9
pop() -> 9, pop() -> 8
size() -> 3

heapify([4, 10, 3, 5, 1]) then pop until empty -> 10, 5, 4, 3, 1
heapify([7, 7, 7]) -> pop -> 7, 7, 7           // duplicates
new MaxHeap().pop() -> NoSuchElementException
push 1..100000 then pop all -> strictly descending   // growth plus speed
```

## Points to explain

- Index arithmetic: `parent = (i-1)/2`, `left = 2i+1`, `right = 2i+2`.
- Why `heapify` is **O(n)**, not O(n log n): most nodes are near the bottom and only sift down a few levels.
- `siftDown` must choose the **larger** child. Watch the case where only a left child exists.
- Handling `Integer.MIN_VALUE` and `Integer.MAX_VALUE`, which have no overflow risk because values are only compared.

## Follow-ups

1. Make it generic: `Heap<T>` taking a `Comparator<T>`. A min heap is then just a reversed comparator.
2. `kthLargest(int[] a, int k)` with a min heap of size k: O(n log k).
3. Heap sort in place.
4. `remove(x)` or `decreaseKey`. Why do you need an index map for O(log n)?
5. A thread-safe version: `synchronized` vs `ReentrantLock` vs `PriorityBlockingQueue`.
