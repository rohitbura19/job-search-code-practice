> Code: `d1/MedianFinder.java` (two heaps) and `d1/SmallRangeMedian.java` (the 0..100 follow-up). Test: `MedianFinderTest`. Sliding-window median and p99 at scale are out loud.

### D1. Find Median from Data Stream (Two heaps)
Design `addNum(int)` and `findMedian()`. After adding `2, 3, 4` the median is 3; after `2, 3` it is 2.5.

Interviewer follow-up: 99% of numbers are in 0..100 — can you do better? Median over a sliding window instead of all history? How would you track p99 latency of an API at scale without storing every value?
