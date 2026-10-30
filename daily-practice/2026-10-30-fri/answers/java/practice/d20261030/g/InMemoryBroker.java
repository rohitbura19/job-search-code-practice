package practice.d20261030.g;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * One monitor guards everything: simplest correct design (publish, poll and commit are short, in-memory). Per topic: a list of
 * partition logs (ArrayList, offset == index) and a round-robin counter. Per (group, topic): the sorted member set and a long[]
 * of committed offsets. The assignment is computed on demand from the member set, so a rebalance is just a set change (O(members)).
 * publish O(1), poll O(max + partitions), commit O(members) for the ownership check.
 */
public class InMemoryBroker implements MessageBroker {
    private static final class Topic {
        final List<List<Record>> logs = new ArrayList<>();
        int rr;
        Topic(int n) { for (int i = 0; i < n; i++) logs.add(new ArrayList<>()); }
    }

    private static final class Group {
        final TreeSet<String> members = new TreeSet<>();
        final long[] committed;
        Group(int partitions) { committed = new long[partitions]; }
    }

    private final Map<String, Topic> topics = new HashMap<>();
    private final Map<String, Group> groups = new HashMap<>();

    @Override public synchronized void createTopic(String topic, int partitions) {
        if (topic == null || topic.isBlank()) throw new BrokerException("blank topic");
        if (partitions < 1) throw new BrokerException("partitions < 1");
        if (topics.putIfAbsent(topic, new Topic(partitions)) != null) throw new BrokerException("topic exists: " + topic);
    }

    private Topic topic(String name) {
        Topic t = topics.get(name);
        if (t == null) throw new BrokerException("unknown topic: " + name);
        return t;
    }

    private Group group(String group, String topic, Topic t) {
        return groups.computeIfAbsent(group + '\0' + topic, k -> new Group(t.logs.size()));
    }

    private List<Integer> owned(Group g, String consumer, int partitions) {
        if (!g.members.contains(consumer)) throw new BrokerException("not a member: " + consumer);
        List<String> ms = new ArrayList<>(g.members);
        int idx = ms.indexOf(consumer);
        List<Integer> out = new ArrayList<>();
        for (int p = idx; p < partitions; p += ms.size()) out.add(p);
        return out;
    }

    @Override public synchronized Record publish(String topic, String key, String value) {
        Topic t = topic(topic);
        if (value == null) throw new BrokerException("null value");
        int n = t.logs.size();
        int p = key != null ? Math.floorMod(key.hashCode(), n) : t.rr++ % n;
        List<Record> log = t.logs.get(p);
        Record r = new Record(topic, p, log.size(), key, value);
        log.add(r);
        return r;
    }

    @Override public synchronized void join(String group, String consumerId, String topic) {
        group(group, topic, topic(topic)).members.add(consumerId);
    }

    @Override public synchronized void leave(String group, String consumerId, String topic) {
        Group g = groups.get(group + '\0' + topic);
        if (g != null) g.members.remove(consumerId);
    }

    @Override public synchronized List<Integer> assignment(String group, String consumerId, String topic) {
        Topic t = topic(topic);
        return owned(group(group, topic, t), consumerId, t.logs.size());
    }

    @Override public synchronized List<Record> poll(String group, String consumerId, String topic, int max) {
        if (max < 1) throw new BrokerException("max < 1");
        Topic t = topic(topic);
        Group g = group(group, topic, t);
        List<Record> out = new ArrayList<>();
        for (int p : owned(g, consumerId, t.logs.size())) {
            List<Record> log = t.logs.get(p);
            for (long off = g.committed[p]; off < log.size() && out.size() < max; off++) out.add(log.get((int) off));
            if (out.size() == max) break;
        }
        return out;
    }

    @Override public synchronized void commit(String group, String consumerId, String topic, int partition, long nextOffset) {
        Topic t = topic(topic);
        Group g = group(group, topic, t);
        if (!owned(g, consumerId, t.logs.size()).contains(partition)) throw new BrokerException("partition not owned: " + partition);
        if (nextOffset < g.committed[partition]) throw new BrokerException("commit goes backwards");
        if (nextOffset > t.logs.get(partition).size()) throw new BrokerException("commit beyond log end");
        g.committed[partition] = nextOffset;
    }

    @Override public synchronized long committed(String group, String topic, int partition) {
        Topic t = topic(topic);
        if (partition < 0 || partition >= t.logs.size()) throw new BrokerException("unknown partition: " + partition);
        return group(group, topic, t).committed[partition];
    }

    @Override public synchronized long lag(String group, String topic) {
        Topic t = topic(topic);
        Group g = group(group, topic, t);
        long lag = 0;
        for (int p = 0; p < t.logs.size(); p++) lag += t.logs.get(p).size() - g.committed[p];
        return lag;
    }
}
