package practice.d20261029.h2;

import java.util.Optional;

/**
 * Chain of responsibility + template method. `handle` is FINAL: ask `decide`; an empty Optional means "not mine, pass it on" to the
 * next link; with no next link the result is rejected("chain-end", "no approver for <amountPaise>").
 */
public abstract class AbstractApprover implements Approver {
    // TODO: the next link (may be null)

    /** Set the next link and return it, so a chain reads `a.then(b).then(c)`. Returns the SAME object that was passed in. */
    public <T extends AbstractApprover> T then(T next) { throw new UnsupportedOperationException("TODO"); }

    @Override public final ApprovalResult handle(Expense expense) { throw new UnsupportedOperationException("TODO"); }

    /** Empty = pass to the next link. */
    protected abstract Optional<ApprovalResult> decide(Expense expense);
}
