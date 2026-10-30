# F1. HLD: a durable pub/sub log with consumer groups (Kafka-lite) (12 min out loud + 5 min self-review)

Design the internal messaging backbone of a large app (orders, payments, notifications publish; many services consume).

Functional: topics with partitions; producers publish with an optional key (same key = same order); consumers in a group split partitions; each group sees every message; replay from an offset; retention by time/size.
Non-functional: 500k msgs/s peak, p99 publish < 20 ms, no acknowledged message lost, per-key ordering, consumers can be slow or crash.

Cover: partition and replica layout, leader election and ISR, acks and durability, offset storage, rebalancing, ordering vs throughput, backpressure, poison messages / DLQ, retention and compaction, hot keys, monitoring (lag). Write in `my-designs/F1-message-broker.md`. No tests (G is the single-node core of this).
