package practice.d20261028.g;

import java.time.Clock;
import java.util.List;

public class InMemoryAuditLog implements AuditLog {
    public InMemoryAuditLog(Clock clock) { throw new UnsupportedOperationException("TODO"); }

    @Override public AuditEvent append(String actor, String action, String resource) { throw new UnsupportedOperationException("TODO"); }
    @Override public List<AuditEvent> query(AuditQuery q) { throw new UnsupportedOperationException("TODO"); }
    @Override public int size() { throw new UnsupportedOperationException("TODO"); }
}
