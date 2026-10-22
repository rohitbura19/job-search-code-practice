> Code: `src/main/java/practice/d20261013/h2/Booking.java`, `BookingRecord.java`, `OrderService.java` (`Order`, `OrderRepository`, `Notifier` are given). Tests: `BookingTest`, `OrderServiceTest`. Questions 4 and 5 are paper.

### H2. OOPS: Encapsulation that really encapsulates, and Dependency Inversion
```java
public final class Booking {
    private final String id;
    private final List<String> guests;
    private final Date checkIn;
    public Booking(String id, List<String> guests, Date checkIn) {
        this.id = id; this.guests = guests; this.checkIn = checkIn;
    }
    public List<String> getGuests() { return guests; }
    public Date getCheckIn() { return checkIn; }
}

class OrderService {
    private final MySqlOrderRepository repo = new MySqlOrderRepository();
    private final SmtpMailer mailer = new SmtpMailer();
    void place(Order o) { repo.save(o); mailer.send(o.customerEmail(), "Placed"); }
}
```
1. Every field is `private final` and the class is `final`. Is `Booking` immutable? Show two ways to break it.
2. Fix it (two styles: classic class, and a `record`).
3. What principle does `OrderService` break? Name two concrete pain points, then refactor.
4. Encapsulation vs abstraction: one sentence each, and one backend example of each.
5. Strategy vs Template Method: how do they differ and when would you pick each?
