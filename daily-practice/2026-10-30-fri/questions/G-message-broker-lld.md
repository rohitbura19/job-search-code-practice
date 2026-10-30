# G. LLD of F1: in-memory partitioned log with consumer groups (30 min: sketch 8, code 22)

`MessageBroker` (read the Javadoc, it is the contract), `Record` and `BrokerException` are given. Implement `g/InMemoryBroker`.
Sketch first in `my-designs/G-classes-sketch.md`: what data per topic, what per (group, topic), which lock, how the assignment is derived.

Test: `InMemoryBrokerTest` (hash partitioning with a negative `hashCode`, null-key round robin, at-least-once polling, commit rules, rebalance on join/leave, stale commit after rebalance, 8 concurrent publishers with gapless offsets, 3 concurrent consumers with no duplicates and no loss).
Trap list: `Math.abs(hashCode)`; recomputing the assignment from a stale member list; committing for a partition you no longer own; returning a live view of the log.
