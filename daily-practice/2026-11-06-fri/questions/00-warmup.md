# Warm-up (10 min, blank page)

1. State the loop invariant of `mergeSorted` when filling from the back. Why can the write index never overtake the unread part of `a`?
2. `ArrayList.remove(int)` vs `remove(Object)`: which one runs for `list.remove(Integer.valueOf(3))`, and what does `for (x : list) list.remove(x)` do?
3. Why is `mid = (lo + hi) / 2` unsafe, and what is the safe form? Where did you see it today?
4. Name the three guarantees of an optimistic-lock `@Version` column and what SQL Hibernate emits on update.
