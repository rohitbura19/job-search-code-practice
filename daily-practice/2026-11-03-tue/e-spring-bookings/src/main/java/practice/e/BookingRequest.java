package practice.e;

import java.time.Instant;

// TODO: Bean Validation on the components.
//  room: not blank, at most 40 chars. startAt, endAt: not null. bookedBy: not blank and a well-formed e-mail.
//  Cross-field rule: endAt must be after startAt. Add a boolean method annotated with @AssertTrue (message "endAt must be after startAt")
//  that returns true when either value is null (the @NotNull violation reports that case already).
public record BookingRequest(String room, Instant startAt, Instant endAt, String bookedBy) {
}
