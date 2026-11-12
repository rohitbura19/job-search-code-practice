package s07_trips;

public record Trip(String driver, String city, int km, long fareCents, boolean cancelled) {}
