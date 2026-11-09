# Warm-up (10 min, blank page)

1. Boyer-Moore voting: why does the surviving candidate have to be the majority element, and what breaks when no majority exists?
2. Binary search on the answer: which two properties must `feasible(x)` have, and why is `lo + (hi - lo) / 2` with `hi = mid` (not `mid - 1`) the safe loop for "smallest feasible"?
3. Kadane: write the recurrence for max-ending-here; for max product why do you need to carry the minimum too?
4. Merge sort counting: why must ties emit the LEFT element first when counting strictly smaller elements to the right?
5. `@ElementCollection`: does it have its own entity id, and what does Hibernate do to the collection table when one element of a `List` without `@OrderColumn` is removed?
