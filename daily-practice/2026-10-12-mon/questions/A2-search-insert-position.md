> Code: `a2/SearchInsert.java`. Test: `SearchInsertTest`.

### A2. Search Insert Position (Binary search, lower bound)
Sorted array of distinct ints and a `target`. Return the index if found, otherwise the index where it would be inserted.
`[1,3,5,6], 5 -> 2`; `[1,3,5,6], 2 -> 1`; `[1,3,5,6], 7 -> 4`.

Interviewer follow-up: allow duplicates and return the first occurrence. Where does `(lo+hi)/2` break? Apply the same template to "minimum ship capacity to deliver in D days" (binary search on the answer).
