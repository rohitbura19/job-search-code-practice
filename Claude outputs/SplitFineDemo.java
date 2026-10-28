package q02.fu1_split_fine;

import java.util.*;

// FOLLOW-UP 1: "Split the fine between the people who held the book late, not only the returner."
// Idea: keep the chain of holders for the open cycle. On return, walk the chain:
// day 1..10 are free; every later day is charged to whoever held the book on that day.
// Example B4: Ace days 1-5, Bob 6-8, Chi 9-12, Ace 13-17 -> Chi pays 2 (days 11,12), Ace pays 5.

record LendingEvent(String lender, String borrower, String bookId, int days) {
    LendingEvent {
        lender = lender.trim(); borrower = borrower.trim(); bookId = bookId.trim();
        if (days < 0) throw new IllegalArgumentException("days cannot be negative");
    }
}

// One person's stretch with the book
record Holding(String person, int days) {}

class FineSummary {
    private int total;
    private final Set<String> books = new LinkedHashSet<>();

    void add(String bookId, int fine) { total += fine; books.add(bookId); }
    String format(String person) { return person + " : " + total + " : " + String.join(", ", books); }
}

class SplitFineCalculator {
    private static final String LIBRARY = "LIB";
    private static final int ALLOWED_DAYS = 10;

    private final Map<String, List<Holding>> chain = new HashMap<>();         // book -> holders this cycle
    private final Map<String, FineSummary> fines = new LinkedHashMap<>();

    Map<String, FineSummary> process(List<LendingEvent> events) {
        for (LendingEvent e : events) {
            if (e.lender().equalsIgnoreCase(LIBRARY)) {
                List<Holding> c = new ArrayList<>();
                c.add(new Holding(e.borrower(), e.days()));
                chain.put(e.bookId(), c);                                    // new cycle
            } else if (e.borrower().equalsIgnoreCase(LIBRARY)) {
                settle(e.bookId());
            } else {
                chain.get(e.bookId()).add(new Holding(e.borrower(), e.days())); // next holder in the chain
            }
        }
        return fines;
    }

    // Walk the holders in order; charge each one only for their days after day 10
    private void settle(String bookId) {
        int daysSoFar = 0;
        for (Holding h : chain.remove(bookId)) {
            int start = daysSoFar, end = daysSoFar + h.days();             // this holder's days: (start, end]
            int lateDays = Math.max(0, end - Math.max(start, ALLOWED_DAYS)); // part of (start,end] beyond day 10
            if (lateDays > 0) fines.computeIfAbsent(h.person(), p -> new FineSummary()).add(bookId, lateDays);
            daysSoFar = end;
        }
    }
}

public class SplitFineDemo {
    static LendingEvent ev(String l, String b, String book, int d) { return new LendingEvent(l, b, book, d); }

    public static void main(String[] args) {
        List<LendingEvent> example2 = List.of(
                ev("LIB", "Ace", "B1", 5), ev("Ace", "Bob", "B1", 7), ev("Bob", "LIB", "B1", 0),
                ev("LIB", "Dave", "B2", 1), ev("Dave", "Eve", "B2", 3), ev("Eve", "LIB", "B2", 0),
                ev("LIB", "Bob", "B3", 14), ev("Bob", "LIB", "B3", 0),
                ev("LIB", "Ace", "B4", 5), ev("Ace", "Bob", "B4", 3), ev("Bob", "Chi", "B4", 4), ev("Chi ", "Ace", "B4", 5),
                ev("LIB", "Ace", "B5", 15),
                ev("Ace ", "LIB", "B4", 0));

        new SplitFineCalculator().process(example2).forEach((p, s) -> System.out.println(s.format(p)));
        // Bob : 6 : B1, B3   (B1: Bob held days 6-12 -> 2;  B3: 4)
        // Chi : 2 : B4       (Chi held days 9-12 -> days 11,12)
        // Ace : 5 : B4       (Ace held days 13-17)
    }
}
