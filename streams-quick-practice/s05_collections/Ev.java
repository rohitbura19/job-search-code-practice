package s05_collections;

/** GIVEN - an energy event. note may be null. */
public record Ev(String id, String site, String type, int kwh, String note) {}
