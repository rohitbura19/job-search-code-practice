# F1 - Threaded comments with voting (HLD, 12 min out loud + 5 min self-review) - `my-designs/F1-comments.md`

A feature of a big social/news app: users comment on posts, reply to comments (nested), and up/down-vote comments. Design the backend.

- Functional: add comment / reply, edit and delete (soft), vote (one vote per user per comment, changeable), read a post's thread sorted by New or Top, load more replies lazily, comment counts on the post card.
- Non-functional: 50M posts, hottest post has 1M comments and 20k votes/s; reads outnumber writes 100:1; a vote must be visible to the voter immediately and to others within seconds; no lost votes, no double counting on retries.
- Cover: storage model for the tree (adjacency list vs materialised path vs closure table), how Top ranking is computed and cached (score decay / Wilson interval), vote counters at 20k/s on one row, pagination (cursor, not offset) of top-level comments and of replies, deletes that keep the thread shape, abuse limits, moderation hooks.
