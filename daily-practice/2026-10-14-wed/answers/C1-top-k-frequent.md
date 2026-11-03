# Answers: C1-top-k-frequent

(Spoilers. Open only after your attempt.)

**Reference**
```java
@SuppressWarnings("unchecked")
static int[] topKFrequent(int[] nums, int k) {
    Map<Integer, Integer> cnt = new HashMap<>();
    for (int x : nums) cnt.merge(x, 1, Integer::sum);
    List<Integer>[] bucket = new List[nums.length + 1];          // index = frequency (max possible = n)
    for (var e : cnt.entrySet()) {
        if (bucket[e.getValue()] == null) bucket[e.getValue()] = new ArrayList<>();
        bucket[e.getValue()].add(e.getKey());
    }
    int[] res = new int[k]; int idx = 0;
    for (int f = bucket.length - 1; f >= 1 && idx < k; f--)       // walk from the highest frequency down
        if (bucket[f] != null) for (int x : bucket[f]) if (idx < k) res[idx++] = x;
    return res;
}
```
Verified: `[1, 2]` and `[1]`. O(n) time, O(n) space. Heap version: keep a min-heap of size k keyed by count, O(n log k), best when k is small and you want streaming-friendly code. Quickselect: average O(n), worst O(n²), mutates the array. Pick the heap in production for readability unless profiling says otherwise. Stream with bounded memory: Misra-Gries / Space-Saving (k counters, guarantees every element above n/k is present) or Count-Min Sketch plus a small heap; both are approximate with known error. Distributed: NO, a global top K item can be absent from every local top K (it is just below the cut on each machine). Either send each machine's counts partitioned by hash of the key (so each key's total lands on one machine, then take top K per partition and merge), or use a mergeable sketch.
