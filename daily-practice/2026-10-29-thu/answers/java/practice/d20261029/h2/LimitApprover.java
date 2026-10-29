package practice.d20261029.h2;

import java.util.Optional;

public class LimitApprover extends AbstractApprover {
    private final String name;
    private final long limitPaise;

    public LimitApprover(String name, long limitPaise) { this.name = name; this.limitPaise = limitPaise; }

    @Override protected Optional<ApprovalResult> decide(Expense e) {
        return e.amountPaise() <= limitPaise ? Optional.of(ApprovalResult.approved(name)) : Optional.empty();
    }
}
