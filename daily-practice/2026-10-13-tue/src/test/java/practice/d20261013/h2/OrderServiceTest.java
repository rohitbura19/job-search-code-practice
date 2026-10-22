package practice.d20261013.h2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** The point of DIP: the service is testable with plain fakes, no database, no SMTP server. */
class OrderServiceTest {

    @Test @DisplayName("place saves first, then notifies the customer with 'Placed'")
    void saveThenNotify() {
        List<String> log = new ArrayList<>();
        OrderRepository repo = o -> log.add("save " + o.id());
        Notifier notifier = (to, msg) -> log.add("send " + to + " " + msg);
        new OrderService(repo, notifier).place(new Order("o1", "ann@example.com"));
        assertEquals(List.of("save o1", "send ann@example.com Placed"), log);
    }

    @Test @DisplayName("if save fails the exception propagates and NOTHING is sent")
    void saveFails() {
        List<String> sent = new ArrayList<>();
        OrderRepository repo = o -> { throw new IllegalStateException("db down"); };
        Notifier notifier = (to, msg) -> sent.add(to);
        OrderService svc = new OrderService(repo, notifier);
        assertThrows(IllegalStateException.class, () -> svc.place(new Order("o1", "ann@example.com")));
        assertTrue(sent.isEmpty(), "no mail for an order that was not saved");
    }

    @Test @DisplayName("collaborators are required")
    void nulls() {
        assertThrows(NullPointerException.class, () -> new OrderService(null, (t, m) -> { }));
        assertThrows(NullPointerException.class, () -> new OrderService(o -> { }, null));
    }
}
