# C1. Heaps: k-th largest in a stream, k closest points, merge k sorted arrays (20 min)

> Code: `src/main/java/practice/d20261102/c1/Heaps.java`. Test: `HeapsTest`. **20 min.**

1. `KthLargest(k, nums)` with `add(val)`: the k-th largest element so far (duplicates count). Example k=3, nums=[4,5,8,2]: add(3)=4, add(5)=5, add(10)=5, add(9)=8, add(4)=8.
2. `kClosest(points, k)`: the k points nearest to the origin, sorted by (squared distance, x, y). Coordinates up to +-10^5: squared distance needs `long`.
3. `mergeSorted(arrays)`: merge k sorted arrays (some empty), O(N log k).

State why a min-heap of size k (not a max-heap of everything) is the right tool for 1, and which heap direction is right for 2 and why.
