package q02.base;

import java.util.*;

// One row of input: lender gave bookId to borrower, borrower kept it for 'days'.
// Names are trimmed here, once, so "Ace " and "Ace" are the same person everywhere.
record LendingEvent(String lender, String borrower, String bookId, int days) {
    LendingEvent {
        lender = lender.trim();
        borrower = borrower.trim();
        bookId = bookId.trim();
        if (days < 0) throw new IllegalArgumentException("days cannot be negative: " + days);
    }
}

// Running result for one person: total fine + books it was charged for (in order, no duplicates)
class FineSummary {
    private int total;
    private final Set<String> books = new LinkedHashSet<>();   // keeps first-fined order, drops repeats

    void add(String bookId, int fine) {
        total += fine;
        books.add(bookId);
    }

    int total() { return total; }

    // Output format: <person> : <totalFine> : <bookId>, <bookId>
    String format(String person) { return person + " : " + total + " : " + String.join(", ", books); }
}

// Applies the library rules to a list of events
class FineCalculator {
    static final String LIBRARY = "LIB";
    private static final int ALLOWED_DAYS = 10;
    private static final int FINE_PER_DAY = 1;

    private final Map<String, Integer> daysOut = new HashMap<>();              // book -> days in current cycle
    private final Map<String, FineSummary> fines = new LinkedHashMap<>();     // person -> summary, first-payer order

    // One pass over all events: O(number of events)
    Map<String, FineSummary> process(List<LendingEvent> events) {
        for (LendingEvent e : events) {
            if (e.lender().equalsIgnoreCase(LIBRARY)) {
                daysOut.put(e.bookId(), e.days());                 // new cycle starts from the library
            } else if (e.borrower().equalsIgnoreCase(LIBRARY)) {
                returnBook(e.lender(), e.bookId());                // book comes back: settle the fine
            } else {
                daysOut.merge(e.bookId(), e.days(), Integer::sum); // passed on: keep adding days
            }
        }
        return fines;
    }

    // The person returning the book pays for every day over the limit
    private void returnBook(String person, String bookId) {
        Integer total = daysOut.remove(bookId);                    // remove -> the cycle is closed
        if (total == null) throw new IllegalStateException(bookId + " returned but was never lent");
        int fine = Math.max(0, total - ALLOWED_DAYS) * FINE_PER_DAY;
        if (fine > 0) fines.computeIfAbsent(person, p -> new FineSummary()).add(bookId, fine);
    }
}

public class LibraryDemo {
    static void print(List<LendingEvent> events) {
        Map<String, FineSummary> fines = new FineCalculator().process(events);
        if (fines.isEmpty()) System.out.println("(no fines)");
        fines.forEach((person, s) -> System.out.println(s.format(person)));
        System.out.println();
    }

    static LendingEvent ev(String l, String b, String book, int d) { return new LendingEvent(l, b, book, d); }

    public static void main(String[] args) {
        // Example 1 -> Bob : 2 : B1
        print(List.of(ev("LIB", "Ace", "B1", 5), ev("Ace", "Bob", "B1", 7), ev("Bob", "LIB", "B1", 0)));

        // Example 2 -> Bob : 6 : B1, B3 / Ace : 7 : B4   (note the trailing spaces in "Chi " and "Ace ")
        print(List.of(
                ev("LIB", "Ace", "B1", 5), ev("Ace", "Bob", "B1", 7), ev("Bob", "LIB", "B1", 0),
                ev("LIB", "Dave", "B2", 1), ev("Dave", "Eve", "B2", 3), ev("Eve", "LIB", "B2", 0),
                ev("LIB", "Bob", "B3", 14), ev("Bob", "LIB", "B3", 0),
                ev("LIB", "Ace", "B4", 5), ev("Ace", "Bob", "B4", 3), ev("Bob", "Chi", "B4", 4), ev("Chi ", "Ace", "B4", 5),
                ev("LIB", "Ace", "B5", 15),
                ev("Ace ", "LIB", "B4", 0)));

        // Example 3: same book, two cycles -> Ace : 3 : B1
        print(List.of(ev("LIB", "Ace", "B1", 12), ev("Ace", "LIB", "B1", 0),
                ev("LIB", "Ace", "B1", 11), ev("Ace", "LIB", "B1", 0),
                ev("LIB", "Dan", "B2", 3), ev("Dan", "Eve", "B2", 9)));

        // Example 4: exactly 10 days -> no fine
        print(List.of(ev("LIB", "Ace", "B1", 10), ev("Ace", "LIB", "B1", 0)));
    }
}
