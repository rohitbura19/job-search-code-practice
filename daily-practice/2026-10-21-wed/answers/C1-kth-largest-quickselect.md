# Answers: C1-kth-largest-quickselect

(Spoilers. Open only after your attempt.)

**Reference**
```java
static int kthLargest(int[] a, int k) {                      // 1-based k; reorders a
    int target = a.length - k;                               // where the answer sits in ASCENDING order
    int lo = 0, hi = a.length - 1; Random rnd = new Random();
    while (lo < hi) {
        int pivot = a[lo + rnd.nextInt(hi - lo + 1)];        // random pivot: no input is bad on purpose
        int lt = lo, i = lo, gt = hi;                        // [lo,lt) < pivot   [lt,i) == pivot   (gt,hi] > pivot
        while (i <= gt) {
            if (a[i] < pivot) swap(a, lt++, i++);
            else if (a[i] > pivot) swap(a, i, gt--);         // do NOT advance i: the swapped-in value is unexamined
            else i++;
        }
        if (target < lt) hi = lt - 1; else if (target > gt) lo = gt + 1; else return a[target];   // inside the "== pivot" block: done
    }
    return a[lo];
}
static int kthHeap(int[] a, int k) { PriorityQueue<Integer> pq = new PriorityQueue<>(); for (int x : a) { pq.add(x); if (pq.size() > k) pq.poll(); } return pq.peek(); }
```
Verified: `5`, `4`, `1` for `([1], 1)` and `7` for `([7,7,7,7], 3)`; 5,000 random arrays (length 1 to 12, values in `-3..4`, so duplicates are common) matched sort-and-index AND the heap version; a sorted array of 2,000,000 elements with `k = 10` finished quickly (a random pivot has no bad input; a fixed last-element pivot would be quadratic here). All-equal input of 20,000 elements: a Lomuto-style partition (pivot = last element) made **19,518,499** comparisons, while the 3-way partition finishes in one pass (about 20,000 comparisons). Costs: sort O(n log n) time; a heap of size `k` is O(n log k) time and O(k) space (best for a stream or a small `k`, and it does not modify the input); Quickselect is expected O(n) time, O(1) extra space, worst case O(n²). Why linear: each round keeps a random part of the array, about half on average, so the expected work is n + n/2 + n/4 + ... = 2n. A pivot is "bad" only when it is near an end, which has probability 2/n per round for a random pivot. Lomuto puts everything `<= pivot` on one side, so an array of equal values splits as n-1 and 0 each time: quadratic; the 3-way (Dutch national flag: the same idea as sort colors) puts all equal elements in the middle and never revisits them. Median-of-medians guarantees O(n) worst case but has a large constant; know the name and that `introselect` (C++ `nth_element`) switches to it only when the recursion goes bad. Huge data: sample to estimate the value around the k-th rank, count how many items fall below, inside and above the sampled bracket (one pass), then keep only the middle bracket (a "histogram then refine" approach used by databases and distributed selects); or use `k`-sized heaps per machine and merge. A stream with a small `k`: the heap. To keep the caller's array: select on a copy (O(n) extra space). Common mistakes: advancing `i` after swapping with `gt`, mixing 0- and 1-based `k`, returning `a[lo]` before narrowing correctly, and recursing on both sides (that is Quicksort, not Quickselect).
