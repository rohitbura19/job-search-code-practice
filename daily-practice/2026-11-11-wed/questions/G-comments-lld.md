# G - Threaded comments LLD (30 min: sketch 8, code 22) - `g/CommentService.java`, sketch in `my-designs/G-classes-sketch.md`

The LLD of F1 as an in-memory, thread-safe component. `Comment` (immutable snapshot) and `CommentSort` are given; implement `CommentService`. The full contract is in the class Javadoc; summary:

- `add` with validation (blank/too long body, parent exists, same post, parent not deleted, depth cap), `vote` as a SET of +1/-1/0 per user (idempotent, no self-votes, none on deleted comments), soft `delete` by the author only (replies survive).
- `topLevel(postId, NEW|TOP, offset, limit)`, `replies(commentId)`, `countVisible(postId)`.
- A comment is VISIBLE if it is not deleted or still has a non-deleted descendant. Aim for an O(depth) visibility test (hint: a per-node counter of live comments in its subtree, kept up to date on add and delete).
- Tests include a concurrency test (2000 distinct voters on 8 threads) and a random add/delete model check.
