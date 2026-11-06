package practice.d20261106.h2;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class EventBusTest {
    interface Animal {}
    record Dog(String n) implements Animal {}
    record Cat(String n) implements Animal {}

    @Test void deliversBySubtypeInOrder() {
        EventBus bus = new EventBus();
        List<String> log = new ArrayList<>();
        bus.subscribe(Dog.class, d -> log.add("dog:" + d.n()));
        bus.subscribe(Animal.class, a -> log.add("animal:" + a));
        bus.subscribe(Object.class, o -> log.add("any"));
        assertEquals(3, bus.publish(new Dog("rex")));
        assertEquals(2, bus.publish(new Cat("tom")));
        assertEquals(List.of("dog:rex", "animal:Dog[n=rex]", "any", "animal:Cat[n=tom]", "any"), log);
    }

    @Test void cancelIsIdempotentAndStopsDelivery() {
        EventBus bus = new EventBus();
        AtomicInteger n = new AtomicInteger();
        Subscription s = bus.subscribe(String.class, x -> n.incrementAndGet());
        assertEquals(1, bus.publish("a"));
        s.cancel();
        s.cancel();
        assertEquals(0, bus.publish("b"));
        assertEquals(1, n.get());
    }

    @Test void throwingHandlerDoesNotBlockOthers() {
        EventBus bus = new EventBus();
        List<String> log = new ArrayList<>();
        bus.subscribe(String.class, s -> { throw new IllegalStateException("boom"); });
        bus.subscribe(String.class, log::add);
        assertEquals(1, bus.publish("x"));
        assertEquals(List.of("x"), log);
    }

    @Test void deadEventWhenNobodyListens() {
        EventBus bus = new EventBus();
        List<Object> dead = new ArrayList<>();
        bus.subscribe(DeadEvent.class, d -> dead.add(d.event()));
        assertEquals(0, bus.publish(42));
        assertEquals(List.of(42), dead);
        EventBus lonely = new EventBus();
        assertEquals(0, lonely.publish(new DeadEvent("x")), "dead events do not recurse");
        bus.subscribe(Integer.class, i -> {});
        bus.publish(7);
        assertEquals(List.of(42), dead, "an event with a handler is not dead");
    }

    @Test void handlersMayChangeSubscriptionsWhilePublishing() {
        EventBus bus = new EventBus();
        List<String> log = new ArrayList<>();
        Subscription[] second = new Subscription[1];
        bus.subscribe(String.class, s -> { log.add("first"); second[0].cancel(); bus.subscribe(String.class, t -> log.add("late")); });
        second[0] = bus.subscribe(String.class, s -> log.add("second"));
        assertEquals(1, bus.publish("a"));
        assertEquals(List.of("first"), log, "cancelled before its turn, new subscriber not called for this event");
        bus.publish("b");
        assertEquals(List.of("first", "first", "late"), log);
    }

    @Test void nestedPublishFromHandler() {
        EventBus bus = new EventBus();
        List<String> log = new ArrayList<>();
        bus.subscribe(Integer.class, i -> { log.add("int"); bus.publish("from-int"); });
        bus.subscribe(String.class, log::add);
        bus.publish(1);
        assertEquals(List.of("int", "from-int"), log);
    }

    @Test void nullsRejected() {
        EventBus bus = new EventBus();
        assertThrows(NullPointerException.class, () -> bus.publish(null));
        assertThrows(NullPointerException.class, () -> bus.subscribe(null, x -> {}));
        assertThrows(NullPointerException.class, () -> bus.subscribe(String.class, null));
    }

    @Test void concurrentPublishAndSubscribe() throws Exception {
        EventBus bus = new EventBus();
        AtomicInteger hits = new AtomicInteger();
        bus.subscribe(Integer.class, i -> hits.incrementAndGet());
        ExecutorService pool = Executors.newFixedThreadPool(8);
        List<Future<?>> fs = new ArrayList<>();
        for (int t = 0; t < 8; t++) fs.add(pool.submit(() -> {
            for (int i = 0; i < 5_000; i++) {
                Subscription s = bus.subscribe(Integer.class, x -> {});
                bus.publish(i);
                s.cancel();
            }
        }));
        for (Future<?> f : fs) f.get(20, TimeUnit.SECONDS);
        pool.shutdown();
        assertEquals(40_000, hits.get());
    }
}
