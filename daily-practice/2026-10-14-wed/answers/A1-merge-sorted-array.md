# Answers: A1-merge-sorted-array

(Spoilers. Open only after your attempt.)

**Reference**
```java
static void merge(int[] a, int m, int[] b, int n) {
    int i = m - 1, j = n - 1, k = m + n - 1;           // three pointers, all from the end
    while (j >= 0)                                      // when b is exhausted, a's prefix is already in place
        a[k--] = (i >= 0 && a[i] > b[j]) ? a[i--] : b[j--];
}
```
Verified: `[1,2,2,3,5,6]`, and `m = 0` gives `[1,2,3]`. O(m+n) time, O(1) space. The trick: the largest values go to the end, where there is free space, so nothing unread is overwritten. Filling from the front would clobber unread elements of `a`. Without spare room, allocate the output, or use a gap-method merge. External merge: stream both files with buffered readers, write the output sequentially, memory is O(buffers). k lists: min-heap of the k heads, O(N log k).
