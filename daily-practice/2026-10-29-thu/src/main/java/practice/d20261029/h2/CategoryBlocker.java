package practice.d20261029.h2;

import java.util.Optional;
import java.util.Set;

/** Rejects (by "policy", reason "category blocked: <category>") expenses of a blocked category, case-insensitively; passes the rest on. */
public class CategoryBlocker extends AbstractApprover {
    public CategoryBlocker(Set<String> blocked) { throw new UnsupportedOperationException("TODO"); }

    @Override protected Optional<ApprovalResult> decide(Expense expense) { throw new UnsupportedOperationException("TODO"); }
}
