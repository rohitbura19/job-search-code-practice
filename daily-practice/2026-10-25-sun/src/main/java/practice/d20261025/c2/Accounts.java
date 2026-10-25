package practice.d20261025.c2;

import java.util.List;

public final class Accounts {
    private Accounts() {}

    /**
     * Each account is [name, email1, email2, ...]. Two accounts belong to the same person if they share at least one email (transitively).
     * Different people may share a name. Return one list per person: [name, emails sorted ascending, distinct...], and order the persons
     * by their smallest email. Accounts that share an email have the same name. Inputs are not modified. An account with only a name and no emails stays on its own, as [name].
     */
    public static List<List<String>> merge(List<List<String>> accounts) { throw new UnsupportedOperationException("TODO"); }

    /** Follow-up: the minimum number of edges to remove so the "same email" graph of n accounts has no cycle, i.e. count redundant unions. */
    public static int redundantLinks(List<List<String>> accounts) { throw new UnsupportedOperationException("TODO"); }
}
