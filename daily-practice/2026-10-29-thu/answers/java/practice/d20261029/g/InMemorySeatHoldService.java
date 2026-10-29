package practice.d20261029.g;

import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import practice.d20261029.g.HoldException.Reason;

/**
 * One lock per event (the EventBook monitor); a hold/confirm touches one event only, so events never contend.
 * Expiry is lazy: every call first "sweeps" the event: a min-heap on expiresAt pops expired holds and frees their seats,
 * so the cost is amortised O(log h) per hold and no background thread is needed (a scheduler is an optimisation).
 * holdId -> EventBook is a ConcurrentHashMap so confirm/release find the right lock without a global one.
 * hold O(k log h), confirm O(k), state O(1) amortised, availableCount O(1) (a counter maintained on every transition).
 */
public class InMemorySeatHoldService implements SeatHoldService {
    private final Clock clock;
    private final HoldConfig config;
    private final Map<String, EventBook> events = new HashMap<>();
    private final Map<String, EventBook> bookByHold = new ConcurrentHashMap<>();
    private final AtomicLong holdSeq = new AtomicLong(), bookingSeq = new AtomicLong();

    private static final class EventBook {
        final Map<String, SeatState> state = new HashMap<>();
        final Map<String, String> holderOfSeat = new HashMap<>();      // seat -> holdId while HELD
        final Map<String, Hold> active = new HashMap<>();
        final Map<String, Booking> confirmed = new HashMap<>();        // holdId -> booking
        final Set<String> expired = new HashSet<>();
        final PriorityQueue<Hold> byExpiry = new PriorityQueue<>(Comparator.comparing(Hold::expiresAt));
        int available;
    }

    public InMemorySeatHoldService(Clock clock, HoldConfig config, Map<String, Set<String>> seatsByEvent) {
        this.clock = clock;
        this.config = config;
        seatsByEvent.forEach((id, seats) -> {
            EventBook b = new EventBook();
            for (String s : seats) b.state.put(s, SeatState.AVAILABLE);
            b.available = seats.size();
            events.put(id, b);
        });
    }

    private EventBook book(String eventId) {
        EventBook b = events.get(eventId);
        if (b == null) throw new HoldException(Reason.UNKNOWN_EVENT, "unknown event " + eventId);
        return b;
    }

    /** caller holds the book's monitor */
    private void sweep(EventBook b, Instant now) {
        while (!b.byExpiry.isEmpty() && !b.byExpiry.peek().expiresAt().isAfter(now)) {
            Hold h = b.byExpiry.poll();
            if (b.active.remove(h.holdId()) != null) {
                free(b, h);
                b.expired.add(h.holdId());
            }
        }
    }

    private void free(EventBook b, Hold h) {
        for (String s : h.seats()) { b.state.put(s, SeatState.AVAILABLE); b.holderOfSeat.remove(s); b.available++; }
    }

    @Override public Hold hold(String eventId, Set<String> seatIds, String userId) {
        if (seatIds.isEmpty()) throw new IllegalArgumentException("no seats");
        if (seatIds.size() > config.maxSeatsPerHold()) throw new HoldException(Reason.TOO_MANY_SEATS, "max " + config.maxSeatsPerHold());
        EventBook b = book(eventId);
        synchronized (b) {
            Instant now = clock.instant();
            sweep(b, now);
            for (String s : seatIds) {
                SeatState st = b.state.get(s);
                if (st == null) throw new HoldException(Reason.UNKNOWN_SEAT, "unknown seat " + s);
            }
            for (String s : seatIds)
                if (b.state.get(s) != SeatState.AVAILABLE) throw new HoldException(Reason.SEAT_UNAVAILABLE, "seat " + s + " is taken");
            Hold h = new Hold("H" + holdSeq.incrementAndGet(), eventId, Set.copyOf(seatIds), userId, now.plus(config.ttl()));
            for (String s : seatIds) { b.state.put(s, SeatState.HELD); b.holderOfSeat.put(s, h.holdId()); b.available--; }
            b.active.put(h.holdId(), h);
            b.byExpiry.add(h);
            bookByHold.put(h.holdId(), b);
            return h;
        }
    }

    @Override public Booking confirm(String holdId, String userId) {
        EventBook b = bookByHold.get(holdId);
        if (b == null) throw new HoldException(Reason.NOT_FOUND, "no hold " + holdId);
        synchronized (b) {
            sweep(b, clock.instant());
            Booking done = b.confirmed.get(holdId);
            if (done != null) {
                if (!done.userId().equals(userId)) throw new HoldException(Reason.NOT_OWNER, "not your hold");
                return done;
            }
            Hold h = b.active.get(holdId);
            if (h == null) throw new HoldException(b.expired.contains(holdId) ? Reason.EXPIRED : Reason.NOT_FOUND, "hold " + holdId + " is not active");
            if (!h.userId().equals(userId)) throw new HoldException(Reason.NOT_OWNER, "not your hold");
            b.active.remove(holdId);
            for (String s : h.seats()) { b.state.put(s, SeatState.BOOKED); b.holderOfSeat.remove(s); }
            Booking bk = new Booking("B" + bookingSeq.incrementAndGet(), holdId, h.eventId(), h.seats(), userId);
            b.confirmed.put(holdId, bk);
            return bk;
        }
    }

    @Override public void release(String holdId, String userId) {
        EventBook b = bookByHold.get(holdId);
        if (b == null) return;
        synchronized (b) {
            sweep(b, clock.instant());
            Hold h = b.active.get(holdId);
            if (h == null) return;
            if (!h.userId().equals(userId)) throw new HoldException(Reason.NOT_OWNER, "not your hold");
            b.active.remove(holdId);
            free(b, h);
        }
    }

    @Override public SeatState state(String eventId, String seatId) {
        EventBook b = book(eventId);
        synchronized (b) {
            sweep(b, clock.instant());
            SeatState st = b.state.get(seatId);
            if (st == null) throw new HoldException(Reason.UNKNOWN_SEAT, "unknown seat " + seatId);
            return st;
        }
    }

    @Override public int availableCount(String eventId) {
        EventBook b = book(eventId);
        synchronized (b) {
            sweep(b, clock.instant());
            return b.available;
        }
    }
}
