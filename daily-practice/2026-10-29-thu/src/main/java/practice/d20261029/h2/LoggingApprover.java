package practice.d20261029.h2;

import java.util.List;

/**
 * Decorator (not a chain link): delegates to `inner`, then appends "<id>:<by>:approved" or "<id>:<by>:rejected" to a log.
 * The log list is passed in (the caller reads it); the decorator must not change the result.
 */
public class LoggingApprover implements Approver {
    public LoggingApprover(Approver inner, List<String> log) { throw new UnsupportedOperationException("TODO"); }

    @Override public ApprovalResult handle(Expense expense) { throw new UnsupportedOperationException("TODO"); }
}
