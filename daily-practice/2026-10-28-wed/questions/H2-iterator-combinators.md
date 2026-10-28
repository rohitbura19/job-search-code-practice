> Code: `src/main/java/practice/d20261028/h2/PeekingIterator.java`, `Iterators.java`. Test: `IteratorsTest`. **30 min.**

# H2. Iterator pattern: look-ahead decorator and lazy combinators (OOP)

1. `PeekingIterator<T>`: a decorator over any `Iterator<T>` adding `peek()`. It must not drain the source (it may be infinite or one-shot) and `hasNext()` on a fresh wrapper must not pull anything. Elements may be `null`.
2. `Iterators.filter`, `interleave`, `flatten`, `windows`: lazy combinators returning plain `Iterator`s. Rules: read each source element at most once, read nothing before it is needed, `hasNext()` can be called any number of times without losing an element, `next()` past the end throws `NoSuchElementException`.

Hint to find the shape: one private base class "compute the next element ahead, cache it, clear it on `next()`" removes most of the duplicated state handling.

On paper: (a) why does `Iterable` exist separately from `Iterator` (what does `for-each` need)? (b) what is a fail-fast iterator and what does `ConcurrentModificationException` actually check? (c) `Iterator.remove()` on your `filter` iterator: why unsupported is the honest answer? (d) where would a `Spliterator` be better than an `Iterator`?
