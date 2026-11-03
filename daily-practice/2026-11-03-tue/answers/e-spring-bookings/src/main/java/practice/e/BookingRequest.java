package practice.e;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;

/** @AssertTrue on an isXxx() method is a cross-field rule; it must tolerate nulls because validators run independently. */
public record BookingRequest(
        @NotBlank @Size(max = 40) String room,
        @NotNull Instant startAt,
        @NotNull Instant endAt,
        @NotBlank @Email String bookedBy) {

    @AssertTrue(message = "endAt must be after startAt")
    public boolean isRangeValid() { return startAt == null || endAt == null || endAt.isAfter(startAt); }
}
