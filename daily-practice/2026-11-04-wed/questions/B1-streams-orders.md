# B1. Streams and collectors on orders (15 min)

> Code: `src/main/java/practice/d20261104/b1/OrderStreams.java` (record `Order` given). Test: `OrderStreamsTest`.

Seven small methods, each with an exact ordering contract (see the Javadoc): `totalByCustomer` (TreeMap, `long` sums), `topCustomers` (total desc, then name), `largestOrderIdPerCategory` (cents desc, earlier date, smaller id), `revenueByMonth` (`YearMonth` keys ascending), `customersPerCategory` (`"books: ann,bob; toys: cy"`), `repeatCustomers` (orders on at least two different dates), `longestDailyStreak` (consecutive calendar days).

Practise: `groupingBy` with a map factory and a downstream collector, `summingLong`, `collectingAndThen`, `minBy`, `mapping` + `toCollection(TreeSet::new)`, `Map.Entry.comparingByValue().reversed().thenComparing(...)`, `distinct().sorted()`.
Constraints: up to 200 000 orders; `cents` up to `Integer.MAX_VALUE`.
