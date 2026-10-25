> Code: `src/main/java/practice/d20261025/a2/MinStack.java`. Test: `MinStackTest`.

# A2. Min Stack (15 min)

`push`, `pop`, `top`, `min`, `size` all in O(1). `pop`, `top` and `min` on an empty stack throw `NoSuchElementException`. `pop` returns the removed value.

Example: push -2, 0, -3 -> `min() = -3`; `pop() = -3`; `top() = 0`; `min() = -2`.

Constraints: 2 million pushes must finish well within 2 s; values are any `int` (including `MIN_VALUE`, duplicates of the minimum).

Say out loud: why a single "current min" field is not enough; the invariant of the parallel min array; the one-array variant that stores `x - min` (what overflows? what is the decoding rule on pop?).
