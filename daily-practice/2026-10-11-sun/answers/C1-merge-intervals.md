# Answers: C1-merge-intervals

(Spoilers. Open only after your attempt.)

**Reference**
- Sort by start, sweep, extend the last merged end if `start <= lastEnd`.
- O(n log n) time, O(n) output. Streaming: `TreeMap<start,end>` with floor/ceiling lookups, O(log n) per insert. Meeting rooms: sort starts and ends separately and two-pointer, or a min-heap of end times.
```java
public int[][] merge(int[][] a) {
    Arrays.sort(a, Comparator.comparingInt(x -> x[0]));
    List<int[]> out = new ArrayList<>();
    for (int[] cur : a) {
        if (out.isEmpty() || out.get(out.size() - 1)[1] < cur[0]) out.add(cur);
        else out.get(out.size() - 1)[1] = Math.max(out.get(out.size() - 1)[1], cur[1]);
    }
    return out.toArray(new int[0][]);
}
```
