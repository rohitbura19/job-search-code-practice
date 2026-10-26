package practice.d20261026.g;

import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class InMemoryChunkStore implements ChunkStore {
    private final Map<String, ConcurrentSkipListMap<Integer, byte[]>> data = new ConcurrentHashMap<>();

    @Override public void put(String uploadId, int index, byte[] bytes) {
        data.computeIfAbsent(uploadId, k -> new ConcurrentSkipListMap<>()).put(index, bytes.clone());
    }

    @Override public Optional<byte[]> get(String uploadId, int index) {
        var m = data.get(uploadId);
        return m == null ? Optional.empty() : Optional.ofNullable(m.get(index)).map(byte[]::clone);
    }

    @Override public SortedSet<Integer> indexes(String uploadId) {
        var m = data.get(uploadId);
        return m == null ? new java.util.TreeSet<>() : new java.util.TreeSet<>(m.keySet());
    }

    @Override public void deleteAll(String uploadId) { data.remove(uploadId); }
}
