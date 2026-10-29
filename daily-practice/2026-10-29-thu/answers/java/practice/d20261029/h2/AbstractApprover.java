package practice.d20261029.h2;

import java.util.Optional;

/**
 * Template method: handle() fixes the algorithm (ask decide(), else forward), subclasses supply only the decision, so no link can
 * forget to forward or can forward twice. `then` returns its argument, so the chain is built left to right in one expression.
 * Each handle() is O(links); a link is stateless, so one chain can be shared across threads.
 */
public abstract class AbstractApprover implements Approver {
    private Approver next;

    public <T extends AbstractApprover> T then(T next) { this.next = next; return next; }

    @Override public final ApprovalResult handle(Expense expense) {
        return decide(expense).orElseGet(() -> next == null
                ? ApprovalResult.rejected("chain-end", "no approver for " + expense.amountPaise())
                : next.handle(expense));
    }

    protected abstract Optional<ApprovalResult> decide(Expense expense);
}
