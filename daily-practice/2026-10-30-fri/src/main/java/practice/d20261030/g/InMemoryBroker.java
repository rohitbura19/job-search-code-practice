package practice.d20261030.g;

import java.util.List;

public class InMemoryBroker implements MessageBroker {
    public InMemoryBroker() {}

    @Override public void createTopic(String topic, int partitions) { throw new UnsupportedOperationException("TODO"); }
    @Override public Record publish(String topic, String key, String value) { throw new UnsupportedOperationException("TODO"); }
    @Override public void join(String group, String consumerId, String topic) { throw new UnsupportedOperationException("TODO"); }
    @Override public void leave(String group, String consumerId, String topic) { throw new UnsupportedOperationException("TODO"); }
    @Override public List<Integer> assignment(String group, String consumerId, String topic) { throw new UnsupportedOperationException("TODO"); }
    @Override public List<Record> poll(String group, String consumerId, String topic, int max) { throw new UnsupportedOperationException("TODO"); }
    @Override public void commit(String group, String consumerId, String topic, int partition, long nextOffset) { throw new UnsupportedOperationException("TODO"); }
    @Override public long committed(String group, String topic, int partition) { throw new UnsupportedOperationException("TODO"); }
    @Override public long lag(String group, String topic) { throw new UnsupportedOperationException("TODO"); }
}
