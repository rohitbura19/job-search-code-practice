# F2. HLD: online presence and "last seen" for a chat app (12 min out loud + 5 min self-review)

Show a green dot / "last seen 5 min ago" for contacts in a messaging app with 200M monthly users, 30M concurrent connections. A user sees the status of up to a few hundred contacts in the open chat list.

Cover: requirements and non-goals (privacy settings, exact seconds?), scale (heartbeats/s, fan-out of a status change), API (connect/heartbeat/subscribe, pull vs push), data model (TTL keys, where "last seen" persists), how a crash is detected (missed heartbeats), why you must NOT fan out every flap to every contact, batching and subscription only for visible contacts, multi-device, regional failover, hot users (celebrity with 10M contacts).
