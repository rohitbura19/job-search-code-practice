# F1. Distributed lock service with fencing tokens (HLD, 12 min out loud + 5 min self-review)

Feature of a big platform: many service instances must run a critical section (a nightly settlement job, a per-account migration, a leader duty) with at most one instance at a time. Design a lock/lease service that other teams call.

Cover: API (`acquire(resource, owner, ttl)`, `renew`, `release`, `current`), the lease and why locks need an expiry, **fencing tokens** and what the protected resource must do with them (a paused client with an expired lease must not corrupt data), where state lives (a Raft-backed store such as etcd/ZooKeeper vs a single Redis vs a database row with `UPDATE ... WHERE expires_at < now()`), clock skew (lease time measured by the server, never the client), failure modes (leader failover, client GC pause, network partition, double release), fairness / waiting (polling vs watch), and numbers: 5k locks/s, 100k live leases, p99 acquire < 20 ms.
Say out loud: why Redlock-style locks without fencing are not safe for correctness (only for efficiency), and what you would use if the work is idempotent anyway.
