package q02.fu2_slab_fines;

import java.util.*;

// FOLLOW-UP 2: "Fines with slabs: Rs 1/day for days 11-20, Rs 2/day after that. Which pattern?"
// Answer: STRATEGY pattern. The calculator asks a FinePolicy "what is the fine for N days?".
// New rules = new policy class; the calculator code never changes (open/closed principle).

record LendingEvent(String lender, String borrower, String bookId, int days) {
    LendingEvent {
        lender = lender.trim(); borrower = borrower.trim(); bookId = bookId.trim();
    }
}

// The strategy interface
interface FinePolicy {
    int fineFor(int totalDays);
}

// Original rule: Rs 1 per day after 10 days
class FlatFinePolicy implements FinePolicy {
    public int fineFor(int totalDays) { return Math.max(0, totalDays - 10); }
}

// Slab rule: free up to 10, Rs 1/day for 11-20, Rs 2/day from day 21
class SlabFinePolicy implements FinePolicy {
    public int fineFor(int totalDays) {
        int slab1 = Math.max(0, Math.min(totalDays, 20) - 10);   // days 11..20
        int slab2 = Math.max(0, totalDays - 20);                  // days 21..
        return slab1 * 1 + slab2 * 2;
    }
}

class FineSummary {
    private int total;
    private final Set<String> books = new LinkedHashSet<>();

    void add(String bookId, int fine) { total += fine; books.add(bookId); }
    String format(String person) { return person + " : " + total + " : " + String.join(", ", books); }
}

// Same as base, but the fine rule is injected through the constructor
class FineCalculator {
    private static final String LIBRARY = "LIB";
    private final FinePolicy policy;
    private final Map<String, Integer> daysOut = new HashMap<>();
    private final Map<String, FineSummary> fines = new LinkedHashMap<>();

    FineCalculator(FinePolicy policy) { this.policy = policy; }

    Map<String, FineSummary> process(List<LendingEvent> events) {
        for (LendingEvent e : events) {
            if (e.lender().equalsIgnoreCase(LIBRARY)) daysOut.put(e.bookId(), e.days());
            else if (e.borrower().equalsIgnoreCase(LIBRARY)) {
                int fine = policy.fineFor(daysOut.remove(e.bookId()));   // <- the only change
                if (fine > 0) fines.computeIfAbsent(e.lender(), p -> new FineSummary()).add(e.bookId(), fine);
            }
            else daysOut.merge(e.bookId(), e.days(), Integer::sum);
        }
        return fines;
    }
}

public class SlabFineDemo {
    static LendingEvent ev(String l, String b, String book, int d) { return new LendingEvent(l, b, book, d); }

    public static void main(String[] args) {
        List<LendingEvent> events = List.of(
                ev("LIB", "Ace", "B1", 17), ev("Ace", "LIB", "B1", 0),               // 17 days
                ev("LIB", "Bob", "B2", 15), ev("Bob", "Eve", "B2", 10), ev("Eve", "LIB", "B2", 0)); // 25 days

        System.out.println("Flat:");
        new FineCalculator(new FlatFinePolicy()).process(events).forEach((p, s) -> System.out.println(s.format(p)));
        // Ace : 7 : B1     Eve : 15 : B2

        System.out.println("Slab:");
        new FineCalculator(new SlabFinePolicy()).process(events).forEach((p, s) -> System.out.println(s.format(p)));
        // Ace : 7 : B1     Eve : 20 : B2   (10 days x Rs 1 + 5 days x Rs 2)
    }
}
