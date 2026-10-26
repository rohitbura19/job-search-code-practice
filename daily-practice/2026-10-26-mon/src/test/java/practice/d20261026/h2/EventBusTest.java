package practice.d20261026.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EventBusTest {
    interface Event {}
    record OrderPlaced(String id) implements Event {}
    record OrderCancelled(String id) implements Event {}

    List<String> log = new ArrayList<>();
    List<Throwable> errors = new ArrayList<>();
    EventBus bus;

    @BeforeEach void setUp() { bus = new EventBus((e, t) -> errors.add(t)); }

    @Test void deliversByTypeInSubscriptionOrder() {
        bus.subscribe(OrderPlaced.class, e -> log.add("first:" + e.id()));
        bus.subscribe(Event.class, e -> log.add("any:" + e));
        bus.subscribe(OrderCancelled.class, e -> log.add("cancel:" + e.id()));
        bus.subscribe(Object.class, e -> log.add("object"));
        assertEquals(3, bus.publish(new OrderPlaced("1")));
        assertEquals(List.of("first:1", "any:OrderPlaced[id=1]", "object"), log);
        log.clear();
        assertEquals(3, bus.publish(new OrderCancelled("2")));
        assertEquals(List.of("any:OrderCancelled[id=2]", "cancel:2", "object"), log);
        assertEquals(1, bus.publish("a string"));
    }

    @Test void noSubscribersAndNullEvent() {
        assertEquals(0, bus.publish(new OrderPlaced("1")));
        assertThrows(IllegalArgumentException.class, () -> bus.publish(null));
        assertThrows(NullPointerException.class, () -> bus.subscribe(null, e -> {}));
        assertThrows(NullPointerException.class, () -> bus.subscribe(String.class, null));
    }

    @Test void aFailingHandlerDoesNotStopTheOthers() {
        bus.subscribe(String.class, s -> log.add("a"));
        bus.subscribe(String.class, s -> { throw new IllegalStateException("boom"); });
        bus.subscribe(String.class, s -> log.add("c"));
        assertEquals(2, bus.publish("x"));
        assertEquals(List.of("a", "c"), log);
        assertEquals(1, errors.size());
        assertEquals("boom", errors.get(0).getMessage());
    }

    @Test void cancelStopsDeliveryAndIsIdempotent() {
        Subscription s = bus.subscribe(String.class, x -> log.add("h"));
        assertTrue(s.isActive());
        assertEquals(1, bus.publish("1"));
        s.cancel();
        s.cancel();
        assertFalse(s.isActive());
        assertEquals(0, bus.publish("2"));
        assertEquals(List.of("h"), log);
    }

    @Test void cancellingALaterHandlerDuringDispatchSkipsIt() {
        Subscription[] later = new Subscription[1];
        bus.subscribe(String.class, x -> { log.add("A"); later[0].cancel(); });
        later[0] = bus.subscribe(String.class, x -> log.add("B"));
        bus.subscribe(String.class, x -> log.add("C"));
        assertEquals(2, bus.publish("x"));
        assertEquals(List.of("A", "C"), log);
    }

    @Test void aHandlerMayCancelItselfAndSubscriptionsDuringDispatchWaitForTheNextPublish() {
        Subscription[] self = new Subscription[1];
        self[0] = bus.subscribe(String.class, x -> {
            log.add("once");
            self[0].cancel();
            bus.subscribe(String.class, y -> log.add("late:" + y));
        });
        assertEquals(1, bus.publish("1"));
        assertEquals(List.of("once"), log);
        assertEquals(1, bus.publish("2"));
        assertEquals(List.of("once", "late:2"), log);
    }

    @Test void reentrantPublishIsDepthFirst() {
        bus.subscribe(Integer.class, n -> { log.add("int" + n); if (n > 0) bus.publish("s" + n); });
        bus.subscribe(String.class, s -> log.add(s));
        bus.subscribe(Integer.class, n -> log.add("int2:" + n));
        bus.publish(1);
        assertEquals(List.of("int1", "s1", "int2:1"), log);
    }

    @Test void manySubscribeCancelCyclesDoNotLeakOrSlowDown() {
        for (int i = 0; i < 20_000; i++) bus.subscribe(String.class, x -> {}).cancel();
        Subscription keep = bus.subscribe(String.class, x -> log.add("k"));
        assertEquals(1, bus.publish("x"));
        assertTrue(keep.isActive());
    }
}
