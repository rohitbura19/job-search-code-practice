# Answers: D1-median-from-data-stream

(Spoilers. Open only after your attempt.)

**Reference**
- Max-heap `low` holds the smaller half, min-heap `high` holds the larger half. Keep `low.size() == high.size()` or `low.size() == high.size() + 1`. Median is `low.peek()` or the average of both tops.
```java
class MedianFinder {
    private final PriorityQueue<Integer> low  = new PriorityQueue<>(Collections.reverseOrder());
    private final PriorityQueue<Integer> high = new PriorityQueue<>();
    public void addNum(int num) {
        low.offer(num);
        high.offer(low.poll());                       // keep every low <= every high
        if (high.size() > low.size()) low.offer(high.poll());   // rebalance
    }
    public double findMedian() {
        return low.size() > high.size() ? low.peek() : (low.peek() + (double) high.peek()) / 2.0;
    }
}
```
- add O(log n), median O(1), space O(n).
- Small value range: counting buckets array of 101 plus two outlier counters, median by walking the cumulative counts, O(1) memory and O(100) query.
- Sliding window median: two heaps with lazy deletion (a `HashMap` of pending removals) or two `TreeMap`/multiset structures, O(log k) per move.
- p99 at scale: HDR histogram or t-digest, mergeable across hosts, bounded memory, approximate with known error.
