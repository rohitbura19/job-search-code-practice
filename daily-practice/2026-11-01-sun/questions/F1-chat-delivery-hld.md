# F1. HLD: chat message delivery (12 min out loud + 5 min self-review)

Feature of a messaging app: send a message in a 1:1 or group conversation (up to 500 members) and have every member's devices receive it in order, with sent / delivered / read receipts. 50 M DAU, 20 B messages per day at peak, p99 send-to-receive < 500 ms for online users, offline users get messages on reconnect.

Cover: requirements and non-goals (no media, no E2E encryption design), APIs (send, sync since seq, ack, read marker), data model (per-conversation monotonic `seq`, idempotent `clientMsgId`), connection layer (WebSocket gateway, how a message finds the right gateway node), fan-out for large groups, ordering and exactly-once-looking delivery on at-least-once transport, offline sync and push notifications, storage (partitioning by conversation, hot conversations), receipts at scale, failure modes (gateway crash, duplicate sends, clock skew), metrics.
Template: `my-designs/F1-chat-delivery.md`.
