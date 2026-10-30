package practice.d20261030.g;

import java.util.List;

/**
 * A single-process Kafka-like broker: topics split into partitions (append-only logs), consumer groups with committed offsets.
 * Thread-safe. Every violation below throws BrokerException.
 */
public interface MessageBroker {

    /** partitions >= 1, topic name not blank and not yet existing. */
    void createTopic(String topic, int partitions);

    /**
     * Append to one partition and return the stored Record. Unknown topic or null value -> exception.
     * Partition: key != null -> Math.floorMod(key.hashCode(), partitions) (same key, same partition, in order);
     * key == null -> round-robin over the topic's partitions 0, 1, 2, ... (one counter per topic, starting at 0).
     * Offsets are per partition, start at 0 and have no gaps, even with concurrent publishers.
     */
    Record publish(String topic, String key, String value);

    /** Add the consumer to the group's membership of the topic (idempotent). Unknown topic -> exception. Triggers a rebalance. */
    void join(String group, String consumerId, String topic);

    /** Remove the consumer (no-op if it is not a member). Triggers a rebalance. Committed offsets stay with the group. */
    void leave(String group, String consumerId, String topic);

    /**
     * Partitions owned by the consumer, ascending. Members of (group, topic) sorted by consumerId (String.compareTo);
     * partition p belongs to members.get(p % members.size()). Not a member -> exception.
     */
    List<Integer> assignment(String group, String consumerId, String topic);

    /**
     * Up to `max` (>= 1) records from the consumer's partitions, visiting partitions ascending, each from the GROUP's committed
     * offset (see commit) to the end of that partition's log, in offset order, stopping when max is reached. Does not change any state:
     * polling again without committing returns the same records (at-least-once). Not a member -> exception.
     */
    List<Record> poll(String group, String consumerId, String topic, int max);

    /**
     * Set the group's committed offset of the partition to nextOffset (the offset of the next record to read).
     * Exceptions: the consumer does not currently own the partition (stale after a rebalance), nextOffset < committed (no going back),
     * nextOffset > log end. nextOffset == committed is allowed.
     */
    void commit(String group, String consumerId, String topic, int partition, long nextOffset);

    /** The group's committed offset of the partition; 0 if it never committed. Unknown topic or partition -> exception. */
    long committed(String group, String topic, int partition);

    /** Sum over all partitions of (log end offset - committed offset) for the group. Unknown topic -> exception. */
    long lag(String group, String topic);
}
