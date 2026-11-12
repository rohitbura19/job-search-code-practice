package s23_transactions;

import java.util.List;
import java.util.Map;

/** One stream pipeline per method, no loops over the data. Result maps are TreeMaps (sorted keys) unless stated. */
public final class TxnStreams {
    private TxnStreams() {}

    /** Sum of cents per account. */
    public static Map<String, Long> totalByAccount(List<Txn> txns) { throw new UnsupportedOperationException("TODO"); }

    /** Per account: the n categories with the lowest total cents (biggest spend first); ties by category name ascending. */
    public static Map<String, List<String>> topSpendCategories(List<Txn> txns, int n) { throw new UnsupportedOperationException("TODO"); }

    /** For each (account, category) the latest txn by date (ties: larger cents wins), ordered by account then category. */
    public static List<Txn> latestPerAccountAndCategory(List<Txn> txns) { throw new UnsupportedOperationException("TODO"); }

    /** Count of txns with abs(cents) >= threshold under key true, others under false. Both keys always present. */
    public static Map<Boolean, Long> countLargeAndSmall(List<Txn> txns, long threshold) { throw new UnsupportedOperationException("TODO"); }

    /** "[A=300, B=-50]": totals in account order using Collectors.joining; "[]" when empty. */
    public static String summary(List<Txn> txns) { throw new UnsupportedOperationException("TODO"); }
}
