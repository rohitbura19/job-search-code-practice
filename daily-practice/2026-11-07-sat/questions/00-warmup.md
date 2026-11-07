# Warm-up (10 min, blank page)

1. Climbing stairs: write the recurrence, then explain why it is Fibonacci and why `int` overflows at n = 46 (so the signature uses `long`).
2. Linked lists: why does a dummy head remove the "delete the first node" special case? State the invariant of the fast/slow runner when `fast` is n nodes ahead.
3. Why can `ArrayDeque` not hold `null` while `LinkedList` can? Where would that bite in a BFS serializer?
4. `@OneToMany(mappedBy = ...)`: which side owns the foreign key, and what happens if you only add to the inverse side's list without setting the owning side?
