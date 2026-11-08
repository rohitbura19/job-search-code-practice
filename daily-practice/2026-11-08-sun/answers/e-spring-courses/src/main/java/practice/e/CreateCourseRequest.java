package practice.e;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateCourseRequest(
        @NotBlank @Size(max = 100) String title,
        @Min(1) @Max(500) int capacity,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate) {

    /** Cross-field rule; null-safe because the @NotNull checks report missing dates separately. */
    @AssertTrue(message = "endDate must not be before startDate")
    public boolean isDatesOrdered() { return startDate == null || endDate == null || !endDate.isBefore(startDate); }
}
