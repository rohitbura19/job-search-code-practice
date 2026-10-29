package practice.d20261029.h2;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class CategoryBlocker extends AbstractApprover {
    private final Set<String> blocked = new HashSet<>();

    public CategoryBlocker(Set<String> blocked) { for (String b : blocked) this.blocked.add(b.toLowerCase(Locale.ROOT)); }

    @Override protected Optional<ApprovalResult> decide(Expense e) {
        return blocked.contains(e.category().toLowerCase(Locale.ROOT))
                ? Optional.of(ApprovalResult.rejected("policy", "category blocked: " + e.category()))
                : Optional.empty();
    }
}
