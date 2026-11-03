package practice.d20261014.g;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

/** GIVEN - do not edit. A weekly window: local start/end time on each of the listed days. It belongs to the day it STARTS on. */
public record Window(Set<DayOfWeek> days, LocalTime start, LocalTime end) {
    public Window {
        days = Set.copyOf(days);
        if (days.isEmpty()) throw new IllegalArgumentException("no days");
        if (start.equals(end)) throw new IllegalArgumentException("start == end is ambiguous; use two rules for 24h");
    }
    public boolean crossesMidnight() { return end.isBefore(start); }
}
