package practice.e;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record CreateSlotRequest(@NotBlank @Size(max = 40) String stationId,
                                @NotNull @Future Instant startsAt,
                                @NotNull @Min(1) @Max(50) Integer capacity) {}   // Integer: a missing value is null -> @NotNull fires; int would become 0
