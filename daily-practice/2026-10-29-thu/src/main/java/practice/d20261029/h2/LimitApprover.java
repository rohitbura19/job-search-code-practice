package practice.d20261029.h2;

import java.util.Optional;

/** Approves (by its name) any expense up to and INCLUDING limitPaise; otherwise passes it on. */
public class LimitApprover extends AbstractApprover {
    public LimitApprover(String name, long limitPaise) { throw new UnsupportedOperationException("TODO"); }

    @Override protected Optional<ApprovalResult> decide(Expense expense) { throw new UnsupportedOperationException("TODO"); }
}
