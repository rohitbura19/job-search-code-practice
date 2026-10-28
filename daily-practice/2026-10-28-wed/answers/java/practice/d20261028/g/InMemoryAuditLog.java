package practice.d20261028.g;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Events live in an ArrayList (index = seq - 1) so afterSeq is a direct index jump. Secondary indexes map actor and resource to the
 * ascending list of event indexes; a query walks the shortest applicable list from the first index >= afterSeq (binary search)
 * and filters the remaining predicates. `at` is monotonic, so the walk stops at the first event with at >= to, and `from` is a
 * binary search over the same list. Writers and readers share one monitor: simple and correct; a ReadWriteLock helps read-heavy loads.
 * append O(1) amortised (+1 hash); query O(log n + scanned within the chosen list).
 */
public class InMemoryAuditLog implements AuditLog {
    private final Clock clock;
    private final List<AuditEvent> events = new ArrayList<>();
    private final Map<String, List<Integer>> byActor = new HashMap<>();
    private final Map<String, List<Integer>> byResource = new HashMap<>();

    public InMemoryAuditLog(Clock clock) { this.clock = clock; }

    @Override public synchronized AuditEvent append(String actor, String action, String resource) {
        for (String s : new String[] {actor, action, resource}) {
            if (s == null || s.isBlank()) throw new IllegalArgumentException("actor, action and resource must be non-blank");
        }
        AuditEvent last = events.isEmpty() ? null : events.get(events.size() - 1);
        Instant now = clock.instant();
        if (last != null && now.isBefore(last.at())) now = last.at();
        long seq = events.size() + 1L;
        String prev = last == null ? AuditChain.GENESIS : last.hash();
        AuditEvent e = new AuditEvent(seq, actor, action, resource, now, prev, AuditChain.hash(seq, actor, action, resource, now, prev));
        events.add(e);
        byActor.computeIfAbsent(actor, k -> new ArrayList<>()).add(events.size() - 1);
        byResource.computeIfAbsent(resource, k -> new ArrayList<>()).add(events.size() - 1);
        return e;
    }

    @Override public synchronized List<AuditEvent> query(AuditQuery q) {
        if (q.limit() < 1) throw new IllegalArgumentException("limit must be >= 1");
        List<Integer> src = null;
        if (q.actor() != null) src = byActor.getOrDefault(q.actor(), List.of());
        if (q.resource() != null) {
            List<Integer> r = byResource.getOrDefault(q.resource(), List.of());
            if (src == null || r.size() < src.size()) src = r;
        }
        long minIndex = Math.max(0, q.afterSeq()); // seq > afterSeq  <=>  index >= afterSeq
        int fromIndex = q.from() == null ? 0 : firstAtOrAfter(q.from());
        long start = Math.max(minIndex, fromIndex);
        List<AuditEvent> out = new ArrayList<>();
        if (start >= events.size()) return out;
        if (src == null) {
            for (int i = (int) start; i < events.size() && out.size() < q.limit(); i++) {
                if (!take(events.get(i), q, out)) break;
            }
        } else {
            int pos = Collections.binarySearch(src, (int) start);
            if (pos < 0) pos = -pos - 1;
            for (int p = pos; p < src.size() && out.size() < q.limit(); p++) {
                if (!take(events.get(src.get(p)), q, out)) break;
            }
        }
        return Collections.unmodifiableList(out);
    }

    /** Returns false when the walk can stop (past `to`). */
    private boolean take(AuditEvent e, AuditQuery q, List<AuditEvent> out) {
        if (q.to() != null && !e.at().isBefore(q.to())) return false;
        if ((q.actor() == null || q.actor().equals(e.actor())) && (q.resource() == null || q.resource().equals(e.resource()))) out.add(e);
        return true;
    }

    private int firstAtOrAfter(Instant t) {
        int lo = 0, hi = events.size();
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (events.get(mid).at().isBefore(t)) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    @Override public synchronized int size() { return events.size(); }
}
