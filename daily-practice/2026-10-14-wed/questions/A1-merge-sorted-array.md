> Code: `src/main/java/practice/d20261014/a1/MergeSortedArray.java`. Test: `MergeSortedArrayTest`.

### A1. Merge Sorted Array (in place, fill from the back)
`nums1` has length `m + n`; its first `m` slots hold a sorted array and the last `n` are zeros. `nums2` has `n` sorted values. Merge `nums2` into `nums1` in place, sorted. `[1,2,3,0,0,0], m=3, [2,5,6], n=3 -> [1,2,2,3,5,6]`.

Interviewer follow-up: why can't you fill from the front without extra space? What if `nums1` had no spare room (you get two sorted lists)? Two sorted files of 50 GB each and 1 GB RAM — how do you merge them? Merge k sorted lists?
