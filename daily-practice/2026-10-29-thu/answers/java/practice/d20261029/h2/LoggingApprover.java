package practice.d20261029.h2;

import java.util.List;

public class LoggingApprover implements Approver {
    private final Approver inner;
    private final List<String> log;

    public LoggingApprover(Approver inner, List<String> log) { this.inner = inner; this.log = log; }

    @Override public ApprovalResult handle(Expense e) {
        ApprovalResult r = inner.handle(e);
        log.add(e.id() + ":" + r.by() + ":" + (r.approved() ? "approved" : "rejected"));
        return r;
    }
}
