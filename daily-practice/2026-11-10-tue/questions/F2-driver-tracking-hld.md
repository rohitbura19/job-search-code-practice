# F2 - Live driver location tracking for food delivery (HLD, 12 min out loud + 5 min review)

Design the "watch your courier on the map" feature: couriers' phones send GPS pings, customers with an active order see the courier moving and an ETA that updates.

Scale hints: 1M couriers online at peak, a ping every 4 s, 3M customers watching at peak, p99 end-to-end latency under 3 s, battery and mobile data matter.

Cover: requirements, ping ingest API/protocol (batching, compression, offline gaps), latest-location store versus history, fan-out to watchers (WebSocket/SSE, one channel per order, backpressure and throttling when nothing moves), ETA service (routing + traffic, when to recompute), geo-indexing for matching, privacy (only during the order, then purge), failure modes and trade-offs.
