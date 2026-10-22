> Code: `src/main/java/practice/d20261013/b1/OrderAnalytics.java`, `Lru.java` (`OrderLine` is given). Test: `OrderAnalyticsTest`.

### B1. Order analytics + LRU (Collectors, sorting, LinkedHashMap)
```java
record OrderLine(String orderId, String customerId, String sku, int qty, double price, String city) {}
```
Given `List<OrderLine> lines`:
1. Top 3 `customerId`s by total revenue (`qty * price`), ties broken by `customerId` ascending.
2. `Map<String, String>`: city (sorted) -> comma separated distinct SKUs (sorted).
3. `Map<String, Long>` of line count per SKU, ordered by count descending then SKU, and the order must be preserved in the returned map.
4. The `orderId` of the first line with `qty > 10`, or `"NONE"`, without calling `Optional.get()`.

Interviewer follow-up: implement an LRU cache in 6 lines with a JDK class. Is it thread-safe? `Comparable` vs `Comparator`? `ArrayList` vs `LinkedList` in real life? When do you pick `CopyOnWriteArrayList`? `List.of` vs `Collections.unmodifiableList`? Why is `Optional` a bad field type or method parameter?
