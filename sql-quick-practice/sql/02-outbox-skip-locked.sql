-- Transactional outbox relay: native query with row locks  (from daily-practice/2026-10-14-wed, E)
-- Table: outbox_event(id BIGINT PK, aggregate_id VARCHAR, payload TEXT, created_at TIMESTAMP, sent_at TIMESTAMP NULL)
--
-- Q1. Several relay pods poll the same table. Return the next :n UNSENT rows (sent_at IS NULL) in id order,
--     locked so that other pods running the same query at the same time SKIP these rows instead of waiting.
--     (PostgreSQL / MySQL 8 syntax.)



-- Q2. After publishing a batch to Kafka, mark those rows as sent in ONE statement (ids in :ids).



-- Talk: why SKIP LOCKED and not plain FOR UPDATE? What happens if the pod dies after publishing but before Q2 commits
--       (at-least-once delivery, so consumers must be idempotent)? Which index makes Q1 cheap?
