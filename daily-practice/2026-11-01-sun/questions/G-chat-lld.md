# G. LLD of F1: in-memory chat core (30 min: sketch 8, code 22)

`g/` : given `ChatService` (read its Javadoc, it is the spec), `Message`, `NotMemberException`, `UnknownConversationException`. Implement `InMemoryChatService`.

Requirements: gap-free per-conversation `seq` assigned atomically; idempotent send per `(conversation, sender, clientMsgId)` returning the original message; paged `fetch` since a seq; monotonic read marker bounded by the last seq; unread count that ignores the user's own messages; membership checks; thread safety under parallel sends from many threads (tests use 8 threads x 500 sends and 32 parallel retries of one message).
Sketch first in `my-designs/G-classes-sketch.md`: classes, the lock granularity (global vs per conversation), the data structure that makes `fetch` O(limit), and what you would persist.
