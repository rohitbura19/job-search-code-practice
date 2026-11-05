# F2. Like / reaction counters for posts of a social app (HLD, 12 min + 5 min review)

Design the "like" button and its count on every post.

- 500 M users, 100 k likes/s peak, celebrity posts get 50 k likes/s alone; reads are 100x writes (feed shows counts for 20 posts per page).
- A user can like and unlike (idempotent: liking twice counts once), and sees whether THEY liked a post. Several reaction types (like, love, haha...).
- Counts may be slightly stale; a user's own action must show immediately (read-your-writes).
- "Who liked this" list, paginated, newest first.

Cover: requirements and numbers, APIs, data model (relationship table vs counter), hot-key mitigation (sharded counters, write batching), consistency of count vs relationship rows, caching, scaling, failure modes, trade-offs. Fill `my-designs/F2-reaction-counters.md`.
