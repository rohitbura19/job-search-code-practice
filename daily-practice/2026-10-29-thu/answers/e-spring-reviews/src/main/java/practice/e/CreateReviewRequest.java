package practice.e;

import jakarta.validation.constraints.*;

/** @NotBlank rejects null, "" and "   " (strings only); @Min/@Max on an int cannot see null, but a missing JSON number becomes 0 and fails @Min(1). */
public record CreateReviewRequest(
        @NotBlank @Size(max = 40) String author,
        @Min(1) @Max(5) int rating,
        @NotBlank @Size(max = 500) String text) {}
