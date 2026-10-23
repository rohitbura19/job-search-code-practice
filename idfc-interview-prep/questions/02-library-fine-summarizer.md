# Q02. Library Fine Summarizer

**Duration:** 25 minutes · **Language:** Java · **Source:** IDFC sample sheet

## Problem Statement

A community library lends books to people. Sometimes a borrower lends the book on to someone else before it comes back to the library.

You are given an ordered list of lending events. Each event is `(lender, borrower, bookId, daysWithBorrower)`. `"LIB"` means the library.

Rules:

1. A book is allowed out for **10 days in total**, counted from the moment the library lends it until it is returned to `LIB`. This total is the sum of `daysWithBorrower` across the whole chain.
2. The fine is **₹1 for each day beyond 10**.
3. The **person who returns the book to `LIB` pays the whole fine**.
4. If a book hasn't been returned yet, no fine is charged.
5. A book can be lent out again after it is returned. Each lending cycle is fined separately.

For every person who has paid a fine, print the total fine and the books it was collected for.

## Expected Output Format

```
<person> : <totalFine> : <bookId>, <bookId>, ...
```

- Print people in the order they **first paid** a fine, and books in the order they were fined.
- Leave out anyone whose total fine is 0.
- If a person is fined for the same book more than once, list the book only once.

## Examples

**Example 1**
```
("LIB","Ace","B1",5), ("Ace","Bob","B1",7), ("Bob","LIB","B1",0)
Output:
Bob : 2 : B1
```
5 + 7 = 12 days, which is 2 days over. Bob returned the book, so Bob pays 2.

**Example 2**
```
("LIB","Ace","B1",5), ("Ace","Bob","B1",7), ("Bob","LIB","B1",0),
("LIB","Dave","B2",1), ("Dave","Eve","B2",3), ("Eve","LIB","B2",0),
("LIB","Bob","B3",14), ("Bob","LIB","B3",0),
("LIB","Ace","B4",5), ("Ace","Bob","B4",3), ("Bob","Chi","B4",4), ("Chi ","Ace","B4",5),
("LIB","Ace","B5",15),
("Ace ","LIB","B4",0)
Output:
Bob : 6 : B1, B3
Ace : 7 : B4
```
- B1: 12 days, 2 over, Bob pays 2.
- B2: 4 days, no fine.
- B3: 14 days, 4 over, Bob pays 4.
- B4: 5+3+4+5 = 17 days, 7 over, Ace pays 7.
- B5: not returned yet, so no fine yet.

Note: `"Chi "` and `"Ace "` have trailing spaces. You must trim them, or Ace's fine is credited to a different person.

**Example 3: the same book in two cycles**
```
("LIB","Ace","B1",12), ("Ace","LIB","B1",0), ("LIB","Ace","B1",11), ("Ace","LIB","B1",0),
("LIB","Dan","B2",3), ("Dan","Eve","B2",9)
Output:
Ace : 3 : B1
```

**Example 4: exactly 10 days**
```
("LIB","Ace","B1",10), ("Ace","LIB","B1",0)
Output: (nothing)
```

## Expected Design

- `record LendingEvent(String lender, String borrower, String bookId, int days)`, with names normalised (trimmed) when the record is built.
- `Map<String, Integer> daysOut` for each book in an open cycle. Optionally, `Map<String, String> currentHolder` to validate that the lender actually holds the book.
- `LinkedHashMap<String, FineSummary>` keeps the order in which people first paid. `FineSummary` holds the total and a `LinkedHashSet<String>` of books.
- `FineCalculator`, with `ALLOWED_DAYS = 10` and `FINE_PER_DAY = 1` as constants or configuration. Keep printing separate from calculating.
- Single pass: O(E) time, O(B + P) space.

## Edge cases to discuss

A lender who doesn't hold the book (invalid chain: throw or skip?) · an event for a book the library never lent · negative days · a non-zero `days` on the return row · `"lib"` in lower case · an empty input list.

## Follow-ups

1. Split the fine between the people who held the book late, not only the returner.
2. Fines with slabs, for example ₹1/day for days 11–20 and ₹2/day after that. Which design pattern fits?
3. Events arrive as a stream and an API needs to show the fines so far at any moment.
4. Show books that are currently overdue and not yet returned, and who is holding each one.
