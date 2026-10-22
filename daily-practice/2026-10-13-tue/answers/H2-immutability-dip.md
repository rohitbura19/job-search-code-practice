# Answers: H2-immutability-dip

(Spoilers. Open only after your attempt.)

**Reference**
- Not immutable. `final` protects the reference, not the object. Break 1: the caller keeps the list it passed in and later calls `guests.add("eve")`; the booking changed. Break 2: `booking.getCheckIn().setTime(0)` (mutable `Date`), or `booking.getGuests().add(...)`.
- Fix:
```java
public final class Booking {
    private final String id; private final List<String> guests; private final LocalDate checkIn;   // java.time is immutable
    public Booking(String id, List<String> guests, LocalDate checkIn) {
        this.id = Objects.requireNonNull(id);
        this.guests = List.copyOf(guests);                // defensive copy on the way in, truly immutable list
        this.checkIn = Objects.requireNonNull(checkIn);
    }
    public List<String> getGuests() { return guests; }    // safe: List.copyOf result cannot be modified
    public LocalDate getCheckIn() { return checkIn; }
}
public record Booking(String id, List<String> guests, LocalDate checkIn) {
    public Booking { Objects.requireNonNull(id); guests = List.copyOf(guests); Objects.requireNonNull(checkIn); }
}
```
If you must keep `Date`, copy it in the constructor and in the getter (`new Date(d.getTime())`).
- `OrderService` breaks the **Dependency Inversion Principle**: the high-level policy depends on concrete low-level classes it constructs itself. Pain points: it cannot be unit-tested without a real DB and SMTP server; swapping the mailer (SES) or the store means editing the service. Refactor:
```java
interface OrderRepository { void save(Order o); }
interface Notifier { void send(String to, String msg); }
class OrderService {
    private final OrderRepository repo; private final Notifier notifier;
    OrderService(OrderRepository repo, Notifier notifier) { this.repo = repo; this.notifier = notifier; }   // constructor injection
    void place(Order o) { repo.save(o); notifier.send(o.customerEmail(), "Placed"); }
}
```
Spring injects the implementations; tests pass fakes. DIP is the principle, dependency injection is the technique.
- Encapsulation: hiding state and enforcing invariants behind methods (`Account.withdraw` checks the balance, nobody sets `balance` directly). Abstraction: exposing what something does and hiding how (`PaymentGateway.charge(...)` hides whether it is Stripe or Razorpay).
- Strategy: swap the whole algorithm through composition at runtime (`ShippingCostStrategy`). Template Method: fixed skeleton in a base class with overridable steps (`AbstractImporter.run() { read(); validate(); save(); }`), chosen at compile time through inheritance. Prefer Strategy by default (composition, easier to test, can change at runtime); use Template Method when the sequence must be enforced and only a few steps vary.
